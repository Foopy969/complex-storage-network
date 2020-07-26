package net.complex.storage.network;

import java.util.ArrayList;
import java.util.List;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Dataduct extends Block implements BlockEntityProvider {

    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");


    public static final Vec3i[] TOUCHING_POS= {new Vec3i(1, 0, 0), new Vec3i(-1, 0, 0), new Vec3i(0, 1, 0), new Vec3i(0, -1, 0), new Vec3i(0, 0, 1), new Vec3i(0, 0, -1)};

    public Dataduct(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
        .with(NORTH, false)
        .with(SOUTH, false)
        .with(EAST, false)
        .with(WEST, false)
        .with(UP, false)
        .with(DOWN, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager
        .add(NORTH)
        .add(SOUTH)
        .add(EAST)
        .add(WEST)
        .add(UP)
        .add(DOWN);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new DataductEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return ActionResult.SUCCESS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        updateModel(world, pos, state);
        world.updateNeighborsAlways(pos, this);
        return;
    }

    public void updateModel(World world, BlockPos pos, BlockState state){
        world.setBlockState(pos, state
        .with(NORTH, getConnectability(world.getBlockState(pos.north())))
        .with(SOUTH, getConnectability(world.getBlockState(pos.south())))
        .with(EAST, getConnectability(world.getBlockState(pos.east())))
        .with(WEST, getConnectability(world.getBlockState(pos.west())))
        .with(UP, getConnectability(world.getBlockState(pos.up())))
        .with(DOWN, getConnectability(world.getBlockState(pos.down()))));
        return;
    }

    public List<Inventory> getConnectedInventories(World world, BlockPos to, BlockPos from){
        BlockPos tempPos;
        BlockState tempBlockState;
        List<Inventory> fetchedInventories = new ArrayList<Inventory>();
        for(Vec3i offset : TOUCHING_POS){
            tempPos = to.add(offset);
            if (tempPos.equals(from)) continue;
            tempBlockState = world.getBlockState(tempPos);

            if (hasInventory(tempBlockState)){
                fetchedInventories.add(ChestBlock.getInventory((ChestBlock) tempBlockState.getBlock(), tempBlockState, world, tempPos, false));
            }else if (isDataduct(tempBlockState)){
                fetchedInventories.addAll(getConnectedInventories(world, tempPos, to));
            }
        }
        return fetchedInventories;
    }

    public static boolean getConnectability(BlockState entry){
        if (entry == null) return false;
        if (!isDataduct(entry) && !hasInventory(entry)) return false;
        else return true;
    }

    public static boolean hasInventory(BlockState blockState){
        if (blockState.getBlock() instanceof ChestBlock) return true;
        else return false;
    }

    public static boolean isDataduct(BlockState blockState){
        if (Registry.BLOCK.getId(blockState.getBlock()).toString().equals("complexstorage:dataduct")) return true;
        else return false;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        updateModel(world, pos, state);
        return;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext entityPos) {
        VoxelShape CENTER_SHAPE = VoxelShapes.cuboid(0.6, 0.6, 0.6, 0.4, 0.4, 0.4);

        if (state.get(NORTH)) CENTER_SHAPE = VoxelShapes.union(CENTER_SHAPE, VoxelShapes.cuboid(0.6, 0.6, 0, 0.4, 0.4, 0.4));
        if (state.get(SOUTH)) CENTER_SHAPE = VoxelShapes.union(CENTER_SHAPE, VoxelShapes.cuboid(0.6, 0.6, 0.6, 0.4, 0.4, 1));
        if (state.get(EAST)) CENTER_SHAPE = VoxelShapes.union(CENTER_SHAPE, VoxelShapes.cuboid(0.6, 0.6, 0.6, 1, 0.4, 0.4));
        if (state.get(WEST)) CENTER_SHAPE = VoxelShapes.union(CENTER_SHAPE, VoxelShapes.cuboid(0, 0.6, 0.6, 0.4, 0.4, 0.4));
        if (state.get(UP)) CENTER_SHAPE = VoxelShapes.union(CENTER_SHAPE, VoxelShapes.cuboid(0.6, 0.6, 0.6, 0.4, 1, 0.4));
        if (state.get(DOWN)) CENTER_SHAPE = VoxelShapes.union(CENTER_SHAPE, VoxelShapes.cuboid(0.6, 0, 0.6, 0.4, 0.4, 0.4));

        return CENTER_SHAPE.simplify();
    }
}