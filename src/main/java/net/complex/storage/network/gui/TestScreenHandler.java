package net.complex.storage.network.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class TestScreenHandler extends ScreenHandler {

    protected TestScreenHandler(ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
        // TODO Auto-generated constructor stub
    }

    public TestScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(type, syncId);
        inventory.onOpen(playerInventory.player);
  
        for (int i = 0; i < inventory.size(); i++) {
            this.addSlot(new Slot(inventory, i, 8 + (i % 9) * 18, 18 + (i / 9) * 18));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
   
    public static TestScreenHandler create(Inventory inventory, PlayerInventory playerInventory) {
        return new TestScreenHandler(null, 0, playerInventory, inventory);
    }
}