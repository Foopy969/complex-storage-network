package net.complex.storage.network.api;

import net.minecraft.util.math.BlockPos;

public class DoubleBlockPos extends BlockPos {
    public BlockPos pos2;

    public DoubleBlockPos(BlockPos a, BlockPos b) {
        super(a);
        pos2 = b;
    }
    
}