package net.complex.storage.network;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ComplexStorage implements ModInitializer {

    public static final TestItem TEST_ITEM = new TestItem(new Item.Settings().group(ItemGroup.MISC));

	@Override
	public void onInitialize() {
        System.out.println("Hello Fabric world!");
        
        Registry.register(Registry.ITEM, new Identifier("complexstorage", "test_item"), TEST_ITEM);
	}
}
