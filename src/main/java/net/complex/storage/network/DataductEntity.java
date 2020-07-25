package net.complex.storage.network;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;

public class DataductEntity extends BlockEntity {

    private String type = "data_duct";

    public int host = 0;

    public DataductEntity() {
        super(ComplexStorage.DATA_DUCT_ENTITY);
    }
    
    public void setHost(int Hex){
        host = Hex;
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
   
        // Save the current value of the number to the tag
        tag.putString("type", type);
        tag.putInt("host", host);
   
        return tag;
     }
}