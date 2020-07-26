package net.complex.storage.network.block;

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
        Block tempBlock;
        BlockPos tempPos;
        player.sendMessage(new LiteralText("------START-----"), false);
        for (Direction direction : Direction.values()){
            tempPos = pos.offset(direction);
            tempBlock = world.getBlockState(tempPos).getBlock();
            if (tempBlock instanceof CableBlock){
                for (Inventory inventory : ((CableBlock)tempBlock).getConnectedInventories(world, tempPos, null, 64)){
                    player.sendMessage(new LiteralText(inventory.toString()), false);
                }
            }
        }
        return ActionResult.PASS;
    }
}