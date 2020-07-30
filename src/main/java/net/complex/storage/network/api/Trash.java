package net.complex.storage.network.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Trash {

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

    public static List<ItemStack> getItems(Inventory inv){
        List<ItemStack> itemStack = new ArrayList<ItemStack>();
        for (int i = 0; i < inv.size(); i++){
            if (!inv.getStack(i).isEmpty()){
                itemStack.add(inv.getStack(i));
            }
        }
        return itemStack;
    }

    public static BlockPos getDoubleBlockPos(ChestBlock chestBlock, BlockState blockState, World world, BlockPos pos){
        DoubleBlockPos chestPos = (DoubleBlockPos) chestBlock.getBlockEntitySource(blockState, world, pos, false).apply(POS_RETRIEVER).orElse((DoubleBlockPos) null);
        if (pos.equals(chestPos)) return chestPos.pos;
        else return chestPos;
    }
}