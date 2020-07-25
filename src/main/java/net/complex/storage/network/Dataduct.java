package net.complex.storage.network;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
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
        UpdateModel(world, pos, state);
        world.updateNeighborsAlways(pos, this);
        return;
    }

    public void UpdateModel(World world, BlockPos pos, BlockState state){
        world.setBlockState(pos, state
        .with(NORTH, GetConnectability(world, pos.north()))
        .with(SOUTH, GetConnectability(world, pos.south()))
        .with(EAST, GetConnectability(world, pos.east()))
        .with(WEST, GetConnectability(world, pos.west()))
        .with(UP, GetConnectability(world, pos.up()))
        .with(DOWN, GetConnectability(world, pos.down())));
        return;
    }

    public boolean GetConnectability(World world, BlockPos pos){
        BlockEntity Temp = world.getBlockEntity(pos);
        if (Temp == null) return false;
        if (Temp.getClass().getSimpleName() == "DataductEntity") return false;
        return true;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        UpdateModel(world, pos, state);
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