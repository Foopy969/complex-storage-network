package net.complex.storage.network.block;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MasterBlock extends Block {

    public MasterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            Block tempBlock;
            BlockPos tempPos;
            Set<BlockPos> tempSet;
            player.sendMessage(new LiteralText("------START-----"), false);
            for (Direction direction : Direction.values()){
                tempPos = pos.offset(direction);
                tempBlock = world.getBlockState(tempPos).getBlock();
                if (tempBlock instanceof CableBlock){
                    tempSet = ((CableBlock)tempBlock).getConnectedInventories(world, tempPos, new HashSet<BlockPos>());
                    player.sendMessage(new LiteralText(Integer.toString(tempSet.size())), false);
                }
            }
        }
        return ActionResult.SUCCESS;
    }
}