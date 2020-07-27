package net.complex.storage.network.item;

import java.util.Optional;

import net.complex.storage.network.api.DoubleBlockPos;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.DoubleBlockProperties.PropertySource;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TestItem extends Item {

    static DoubleBlockProperties.PropertyRetriever<ChestBlockEntity, Optional<BlockPos>> POS_RETRIEVER;

    static {
        POS_RETRIEVER = new DoubleBlockProperties.PropertyRetriever<ChestBlockEntity, Optional<BlockPos>>() {
            public Optional<BlockPos> getFromBoth(ChestBlockEntity chestBlockEntity, ChestBlockEntity chestBlockEntity2) {
               return Optional.of(new DoubleBlockPos(chestBlockEntity.getPos(), chestBlockEntity2.getPos()));
            }
   
            public Optional<BlockPos> getFrom(ChestBlockEntity chestBlockEntity) {
               return Optional.of(chestBlockEntity.getPos());
            }
   
            public Optional<BlockPos> getFallback() {
               return Optional.empty();
            }
         };
    }

    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        playerEntity.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);
        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        
        if (!world.isClient) {
            BlockPos pos = context.getBlockPos();
            PlayerEntity player = context.getPlayer();
            Block block = world.getBlockState(pos).getBlock();

            if (block instanceof ChestBlock){
                ChestBlock chestBlock = (ChestBlock) world.getBlockState(pos).getBlock();
                PropertySource<? extends ChestBlockEntity> propertySource = chestBlock.getBlockEntitySource(world.getBlockState(pos), world, pos, false);
                BlockPos chestPos = propertySource.apply(POS_RETRIEVER).orElse((BlockPos) null);

                if (chestPos == null){
                    player.sendMessage(new LiteralText("Not a chest"), false);
                }else if (chestPos instanceof DoubleBlockPos){
                    DoubleBlockPos doubleChestPos = (DoubleBlockPos)chestPos;
                    player.sendMessage(new LiteralText(String.format("X: %d, Y: %d, Z: %d", doubleChestPos.getX(), doubleChestPos.getY(), doubleChestPos.getZ())), false);
                    player.sendMessage(new LiteralText(String.format("X: %d, Y: %d, Z: %d", doubleChestPos.pos2.getX(), doubleChestPos.pos2.getY(), doubleChestPos.pos2.getZ())), false);
                }else {
                    player.sendMessage(new LiteralText(String.format("X: %d, Y: %d, Z: %d", chestPos.getX(), chestPos.getY(), chestPos.getZ())), false);
                }
            }else{
                player.sendMessage(new LiteralText("Not a chest"), false);
            }
        }
        return ActionResult.SUCCESS;
    }
}