package net.complex.storage.network.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.complex.storage.network.api.Trash;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MasterBlock extends Block{

    public MasterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            try {
                setMergedInv(world, pos);
            } catch (Exception e) {
                player.sendMessage(new LiteralText(e.getMessage()), false);
                e.printStackTrace();
            } finally {

            }
            return ActionResult.CONSUME;
        }
        return ActionResult.SUCCESS;
    }

    public Inventory setMergedInv(World world, BlockPos pos) throws Exception {
        Set<BlockPos> poss = new HashSet<BlockPos>();
        List<ItemStack> itemStack = new ArrayList<ItemStack>();
        poss.add(pos);

        for (Inventory item : CableBlock.getConnectedInvs(world, pos, poss)){
            itemStack.addAll(Trash.getItems(item));
        }
        return new SimpleInventory((ItemStack[]) itemStack.toArray());
    }
}