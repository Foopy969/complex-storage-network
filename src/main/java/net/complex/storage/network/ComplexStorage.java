package net.complex.storage.network;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ComplexStorage implements ModInitializer {

    public static final TestItem TEST_ITEM = new TestItem(new Item.Settings().group(ItemGroup.MISC));
    public static final Dataduct DATA_DUCT = new Dataduct(FabricBlockSettings.of(Material.REDSTONE_LAMP).hardness(1.0f));
    public static final BlockEntityType<DataductEntity> DATA_DUCT_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "complexstorage", BlockEntityType.Builder.create(DataductEntity::new, DATA_DUCT).build(null));

	@Override
	public void onInitialize() {
        Registry.register(Registry.BLOCK, new Identifier("complexstorage", "data_duct"), DATA_DUCT);
        Registry.register(Registry.ITEM, new Identifier("complexstorage", "data_duct"), new BlockItem(DATA_DUCT, new Item.Settings().group(ItemGroup.MISC)));
        Registry.register(Registry.ITEM, new Identifier("complexstorage", "test_item"), TEST_ITEM);
	}
}
