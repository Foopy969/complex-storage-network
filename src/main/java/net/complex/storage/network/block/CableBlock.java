package net.complex.storage.network.block;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import blue.endless.jankson.annotation.Nullable;
import net.complex.storage.network.api.DoubleBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CableBlock extends Block {

    public static final EnumProperty<ConnectType> NORTH = EnumProperty.of("north", ConnectType.class);
    public static final EnumProperty<ConnectType> SOUTH = EnumProperty.of("south", ConnectType.class);
    public static final EnumProperty<ConnectType> EAST = EnumProperty.of("east", ConnectType.class);
    public static final EnumProperty<ConnectType> WEST = EnumProperty.of("west", ConnectType.class);
    public static final EnumProperty<ConnectType> UP = EnumProperty.of("up", ConnectType.class);
    public static final EnumProperty<ConnectType> DOWN = EnumProperty.of("down", ConnectType.class);

    public static final Map<Direction, EnumProperty<ConnectType>> FACING_TO_PROPERTY_MAP = Map.of(
        Direction.NORTH, NORTH,
        Direction.SOUTH, SOUTH,
        Direction.EAST, EAST,
        Direction.WEST, WEST,
        Direction.UP, UP,
        Direction.DOWN, DOWN
    );

    public static final Map<Direction, VoxelShape> FACING_TO_SHAPE_MAP = Map.of(
        Direction.NORTH, VoxelShapes.cuboid(0.6, 0.6, 0, 0.4, 0.4, 0.4),
        Direction.SOUTH, VoxelShapes.cuboid(0.6, 0.6, 0.6, 0.4, 0.4, 1),
        Direction.EAST, VoxelShapes.cuboid(0.6, 0.6, 0.6, 1, 0.4, 0.4),
        Direction.WEST, VoxelShapes.cuboid(0, 0.6, 0.6, 0.4, 0.4, 0.4),
        Direction.UP, VoxelShapes.cuboid(0.6, 0.6, 0.6, 0.4, 1, 0.4),
        Direction.DOWN, VoxelShapes.cuboid(0.6, 0, 0.6, 0.4, 0.4, 0.4)
    );

    public CableBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
        .with(NORTH, ConnectType.NONE).with(SOUTH, ConnectType.NONE)
        .with(EAST, ConnectType.NONE).with(WEST, ConnectType.NONE)
        .with(UP, ConnectType.NONE).with(DOWN, ConnectType.NONE));
    }

    static DoubleBlockProperties.PropertyRetriever<ChestBlockEntity, Optional<BlockPos>> POS_RETRIEVER;

    static {
        POS_RETRIEVER = new DoubleBlockProperties.PropertyRetriever<ChestBlockEntity, Optional<BlockPos>>() {
            public Optional<BlockPos> getFromBoth(ChestBlockEntity chestBlockEntity, ChestBlockEntity chestBlockEntity2) {
               return Optional.of(new DoubleBlockPos(chestBlockEntity.getPos(), chestBlockEntity2.getPos()));
            }
   
            public Optional<BlockPos> getFrom(ChestBlockEntity chestBlockEntity) {
               return Optional.of(chestBlockEntity.getPos());
            }
   
            public Optional<BlockPos> getFallback() {
               return Optional.empty();
            }
         };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(NORTH).add(SOUTH).add(EAST).add(WEST).add(UP).add(DOWN);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        updateModel(world, pos, state);
        world.updateNeighborsAlways(pos, this);
    }
    
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        updateModel(world, pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext entityPos) {
        VoxelShape CENTER_SHAPE = VoxelShapes.cuboid(0.6, 0.6, 0.6, 0.4, 0.4, 0.4);

        for(Map.Entry<Direction, EnumProperty<ConnectType>> entry : FACING_TO_PROPERTY_MAP.entrySet()){
            if (state.get(entry.getValue()).isConnected()){
                CENTER_SHAPE = VoxelShapes.combine(CENTER_SHAPE, FACING_TO_SHAPE_MAP.get(entry.getKey()), BooleanBiFunction.OR);
            }
        }
        return CENTER_SHAPE.simplify();
    }

    public static void updateModel(World world, BlockPos pos, BlockState state){
        for(Map.Entry<Direction, EnumProperty<ConnectType>> entry : FACING_TO_PROPERTY_MAP.entrySet()){
            //when manuel disconnect with a wrench maybe
            if (state.get(entry.getValue()) == ConnectType.DISCONNECT) continue;
            state = state.with(entry.getValue(), getConnect(world.getBlockState(pos.offset(entry.getKey()))));
        }
        world.setBlockState(pos, state);
    }

    public static Set<Inventory> getConnectedInvs(World world, BlockPos to, Set<BlockPos> from) throws Exception {
        BlockPos pos;
        BlockState blockState;
        Block block;
        ChestBlock chestBlock;
        Inventory inv;
        Set<Inventory> invs = new HashSet<Inventory>();

        for(Direction dir : Direction.values()){
            pos = to.offset(dir);
            //skip backtrack
            if (from.contains(pos)) continue;
            from.add(pos);
            blockState = world.getBlockState(pos);
            block = blockState.getBlock();

            if (block instanceof ChestBlock){
                chestBlock = (ChestBlock) block;
                inv = ChestBlock.getInventory(chestBlock, blockState, world, pos, false);
                invs.add(inv);
                if (inv instanceof DoubleInventory){
                    from.add(getDoubleBlockPos(chestBlock, blockState, world, pos));
                }
            }else if (block instanceof CableBlock){
                invs.addAll(getConnectedInvs(world, pos, from));
            }else if (block instanceof MasterBlock){
                throw new Exception("More than one master block in a storage network.");
            }
        }
        return invs;
    }

    public static BlockPos getDoubleBlockPos(ChestBlock chestBlock, BlockState blockState, World world, BlockPos pos){
        DoubleBlockPos chestPos = (DoubleBlockPos) chestBlock.getBlockEntitySource(blockState, world, pos, false).apply(POS_RETRIEVER).orElse((DoubleBlockPos) null);
        if (pos.equals(chestPos)) return chestPos.pos;
        else return chestPos;
    }

    public static ConnectType getConnect(BlockState blockState){
        if (blockState == null) return ConnectType.NONE;
        if (blockState.getBlock() instanceof ChestBlock) return ConnectType.INVENTORY;
        else if (blockState.getBlock() instanceof CableBlock || blockState.getBlock() instanceof MasterBlock) return ConnectType.CABLE;
        else return ConnectType.NONE;
    }

    public enum ConnectType implements StringIdentifiable{
        NONE, CABLE, INVENTORY, DISCONNECT;

        public boolean isConnected(){
            if (this.equals(CABLE) || this.equals(INVENTORY)) return true;
            else return false;
        }

        @Override
        public String asString() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}