package net.complex.storage.network.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class Trash {
    public static List<ItemStack> getItems(Inventory inv){
        List<ItemStack> itemStack = new ArrayList<ItemStack>();
        for (int i = 0; i < inv.size(); i++){
            if (!inv.getStack(i).isEmpty()){
                itemStack.add(inv.getStack(i));
            }
        }
        return itemStack;
    }
}