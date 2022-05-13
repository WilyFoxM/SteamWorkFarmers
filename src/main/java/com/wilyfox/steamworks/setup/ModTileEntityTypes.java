package com.wilyfox.steamworks.setup;

import com.wilyfox.steamworks.blocks.CarrotFarmer.CarrotFarmerTileEntity;
import com.wilyfox.steamworks.blocks.PotatoFarmer.PotatoFarmerTileEntity;
import com.wilyfox.steamworks.blocks.StorageTileEntity;
import com.wilyfox.steamworks.blocks.WheatFarmer.WheatFarmerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

import static net.minecraft.tileentity.TileEntityType.*;

public class ModTileEntityTypes {
    public static final RegistryObject<TileEntityType<StorageTileEntity>> STORAGE = register("storage", StorageTileEntity::new, ModBlocks.STORAGE_BLOCK);
    public static final RegistryObject<TileEntityType<WheatFarmerTileEntity>> WHEAT_FARMER = register("wheat_farmer", WheatFarmerTileEntity::new, ModBlocks.WHEAT_FARMER_BLOCK);
    public static final RegistryObject<TileEntityType<CarrotFarmerTileEntity>> CARROT_FARMER = register("carrot_farmer", CarrotFarmerTileEntity::new, ModBlocks.CARROT_FARMER_BLOCK);
    public static final RegistryObject<TileEntityType<PotatoFarmerTileEntity>> POTATO_FARMER = register("potato_farmer", PotatoFarmerTileEntity::new, ModBlocks.POTATO_FARMER_BLOCK);

    static void register() {}

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block) {
        return Registration.TILE_ENTITIES.register(name, () -> {
            return Builder.of(factory, block.get()).build(null);
        });
    }
}
