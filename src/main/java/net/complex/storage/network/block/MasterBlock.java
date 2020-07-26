package net.complex.storage.network.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
        for (Direction direction : Direction.values()){
            tempBlock = world.getBlockState(pos.offset(direction)).getBlock();
            if (tempBlock instanceof CableBlock){
                player.sendMessage(new LiteralText("Found " + ((CableBlock)tempBlock).getConnectedInventories(world, pos, null, 16).size() + " chests."), false);
            }
        }
        return ActionResult.PASS;
     }
}