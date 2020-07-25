package net.complex.storage.network;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;

public class DataductEntity extends BlockEntity {

    private int number = 7;

    public DataductEntity() {
        super(ComplexStorage.DATA_DUCT_ENTITY);
    }
    
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
   
        // Save the current value of the number to the tag
        tag.putInt("number", number);
   
        return tag;
     }
}