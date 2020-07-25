package net.complex.storage.network;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Dataduct extends Block implements BlockEntityProvider {

    private static final Vec3i[] NearbyPoslist = { new Vec3i(1, 0, 0), new Vec3i(-1, 0, 0), new Vec3i(0, 1, 0),
            new Vec3i(0, -1, 0), new Vec3i(0, 0, 1), new Vec3i(0, 0, -1) };

    public Dataduct(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new DataductEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (!world.isClient) {
            player.sendMessage(new LiteralText("Debug1"), false);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        for (int i = 0; i < 6; i++){
            if (world.getBlockState(pos.add(NearbyPoslist[i])).equals(state)){
                CompoundTag tag = new CompoundTag();;
				world.getPlayers().get(0).sendMessage(new LiteralText(world.getBlockEntity(pos.add(NearbyPoslist[i])).toTag(tag).get("number").asString()), false);
                world.getPlayers().get(0).sendMessage(new LiteralText("Connected"), false);
            }
        }
        return;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        return;
     }
}