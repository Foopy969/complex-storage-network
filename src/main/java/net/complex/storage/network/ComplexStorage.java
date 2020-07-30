package net.complex.storage.network;

import net.complex.storage.network.item.TestItem;
import net.complex.storage.network.block.CableBlock;
import net.complex.storage.network.block.MasterBlock;
import net.complex.storage.network.gui.TestScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ComplexStorage implements ModInitializer {

    public static final TestItem TEST_ITEM = new TestItem(new Item.Settings().group(ItemGroup.MISC));
    public static final CableBlock CABLE_BLOCK = new CableBlock(FabricBlockSettings.of(Material.GLASS));
    public static final MasterBlock MASTER_BLOCK = new MasterBlock(FabricBlockSettings.of(Material.STONE));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("complexstorage", "test_item"), TEST_ITEM);
        Registry.register(Registry.BLOCK, new Identifier("complexstorage", "basic_cable"), CABLE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("complexstorage", "basic_cable"),
                new BlockItem(CABLE_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
        Registry.register(Registry.BLOCK, new Identifier("complexstorage", "master_block"), MASTER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("complexstorage", "master_block"),
                new BlockItem(MASTER_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
    }
}
