package net.complex.storage.network.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
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

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(NORTH).add(SOUTH).add(EAST).add(WEST).add(UP).add(DOWN);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        updateModel(world, pos, state);
        world.updateNeighborsAlways(pos, this);
        return;
    }
    
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        updateModel(world, pos, state);
        return;
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

    public void updateModel(World world, BlockPos pos, BlockState state){
        for(Map.Entry<Direction, EnumProperty<ConnectType>> entry : FACING_TO_PROPERTY_MAP.entrySet()){
            //when manuel disconnect with a wrench maybe
            if (state.get(entry.getValue()) == ConnectType.DISCONNECT) continue;
            state = state.with(entry.getValue(), getConnect(world.getBlockState(pos.offset(entry.getKey()))));
        }
        world.setBlockState(pos, state);
        return;
    }

    public List<Inventory> getConnectedInventories(World world, BlockPos to, BlockPos from, int depth){
        BlockPos tempPos;
        BlockState tempBlockState;
        List<Inventory> fetchedInventories = new ArrayList<Inventory>();
        //max depth reached
        if (depth == 0) return fetchedInventories;

        for(Direction direction : Direction.values()){
            tempPos = to.offset(direction);
            //skip backtrack
            if (tempPos.equals(from)) continue;
            tempBlockState = world.getBlockState(tempPos);

            if (isChest(tempBlockState)){
                fetchedInventories.add(ChestBlock.getInventory((ChestBlock) tempBlockState.getBlock(), tempBlockState, world, tempPos, false));
            }else if (isCable(tempBlockState)){
                fetchedInventories.addAll(getConnectedInventories(world, tempPos, to, depth - 1));
            }
        }
        return fetchedInventories;
    }

    public static ConnectType getConnect(BlockState entry){
        if (entry == null) return ConnectType.NONE;
        if (isChest(entry)) return ConnectType.INVENTORY;
        else if (isCable(entry)) return ConnectType.CABLE;
        else return ConnectType.NONE;
    }

    public static boolean isChest(BlockState blockState){
        if (blockState.getBlock() instanceof ChestBlock) return true;
        else return false;
    }

    public static boolean isCable(BlockState blockState){
        if (blockState.getBlock() instanceof CableBlock) return true;
        else return false;
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