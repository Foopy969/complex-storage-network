package net.complex.storage.network.item;

import net.complex.storage.network.gui.TestGui;
import net.complex.storage.network.gui.TestScreen;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TestItem extends Item {

    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!world.isClient) {
            MinecraftClient.getInstance().openScreen(new TestScreen(new TestGui()));
        }
        MinecraftClient.getInstance().openScreen(new TestScreen(new TestGui()));
        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        
        if (!world.isClient) {
            BlockPos pos = context.getBlockPos();
            PlayerEntity player = context.getPlayer();
            Block block = world.getBlockState(pos).getBlock();
        }

        return ActionResult.SUCCESS;
    }
}