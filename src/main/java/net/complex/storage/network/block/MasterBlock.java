package net.complex.storage.network.block;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.DoubleInventory;
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
            Block block;
            Set<BlockPos> poss = new HashSet<BlockPos>();
            Set<Inventory> invs = new HashSet<Inventory>();
            poss.add(pos);
            for (Direction direction : Direction.values()) {
                block = world.getBlockState(pos.offset(direction)).getBlock();
                if (block instanceof CableBlock) {
                    try {
                        int count = 0;
                        invs = CableBlock.getConnectedInvs(world, pos.offset(direction), poss);

                        for (Inventory inv : invs){
                            if (inv instanceof DoubleInventory) count++;
                        }
                        player.sendMessage(new LiteralText(String.format("DoubleChest: %d", count)), false);
                        player.sendMessage(new LiteralText(String.format("SingleChest: %d", invs.size() - count)), false);

                    } catch (Exception e) {
                        player.sendMessage(new LiteralText(e.getMessage()), false);
                        e.printStackTrace();
                    }
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    public Inventory getMergedInv(){
        
        return null;
    }
}