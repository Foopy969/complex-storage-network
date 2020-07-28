package net.complex.storage.network.api;

import net.minecraft.util.math.BlockPos;

public class DoubleBlockPos extends BlockPos {
    public BlockPos pos;

    public DoubleBlockPos(BlockPos a, BlockPos b) {
        super(a);
        pos = b;
    }
    
}