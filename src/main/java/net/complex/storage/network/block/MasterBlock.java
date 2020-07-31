package net.complex.storage.network.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.complex.storage.network.ComplexStorage;
import net.complex.storage.network.api.Trash;
import net.complex.storage.network.gui.TestScreen;
import net.complex.storage.network.gui.TestScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MasterBlock extends Block {

    public MasterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (!world.isClient) {
            Inventory inv = new SimpleInventory(0);
            try {
                inv = getMergedInv(world, pos);
            } catch (Exception e) {
                player.sendMessage(new LiteralText(e.getMessage()), false);
                e.printStackTrace();
            } finally {
                MinecraftClient.getInstance().openScreen(TestScreen.create(inv, player.inventory));
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.SUCCESS;
    }

    public Inventory getMergedInv(World world, BlockPos pos) throws Exception {
        Set<BlockPos> poss = new HashSet<BlockPos>(Arrays.asList(pos));
        List<ItemStack> itemStacks = new ArrayList<ItemStack>();
        int index = -1;

        for (Inventory items : CableBlock.getConnectedInvs(world, pos, poss, true)) {
            for (ItemStack item : Trash.getItems(items)) {
                //item merge section, fix bug if found
                if (itemStacks.stream().anyMatch(x -> x.getItem().equals(item.getItem()))) {
                    index = IntStream.range(0, itemStacks.size()).filter(i -> itemStacks.get(i).getItem().equals(item.getItem())).findFirst().orElse(-1);
                    itemStacks.get(index).setCount(itemStacks.get(index).getCount() + item.getCount());
                }else{
                    itemStacks.add(new ItemStack(item.getItem(), item.getCount()));
                }
            }
        }
        return new SimpleInventory(itemStacks.toArray(new ItemStack[0]));
    }
}