package com.wilyfox.steamworks.setup;

import com.wilyfox.steamworks.blocks.BeetrootFarmer.BeetrootFarmerTileEntity;
import com.wilyfox.steamworks.blocks.CactusFarmer.CactusFarmerTileEntity;
import com.wilyfox.steamworks.blocks.CarrotFarmer.CarrotFarmerTileEntity;
import com.wilyfox.steamworks.blocks.MelonFarmer.MelonFarmerTileEntity;
import com.wilyfox.steamworks.blocks.PotatoFarmer.PotatoFarmerTileEntity;
import com.wilyfox.steamworks.blocks.PumpkinFarmer.PumpkinFarmerTileEntity;
import com.wilyfox.steamworks.blocks.ShugarcaneFarmer.ShugarcaneFarmerTileEntity;
import com.wilyfox.steamworks.blocks.TreeFarmer.TreeFarmerTileEntity;
import com.wilyfox.steamworks.blocks.WheatFarmer.WheatFarmerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

import static net.minecraft.tileentity.TileEntityType.*;

public class ModTileEntityTypes {
//    public static final RegistryObject<TileEntityType<StorageTileEntity>> STORAGE = register("storage", StorageTileEntity::new, ModBlocks.STORAGE_BLOCK);
    public static final RegistryObject<TileEntityType<WheatFarmerTileEntity>> WHEAT_FARMER = register("wheat_farmer", WheatFarmerTileEntity::new, ModBlocks.WHEAT_FARMER_BLOCK);
    public static final RegistryObject<TileEntityType<CarrotFarmerTileEntity>> CARROT_FARMER = register("carrot_farmer", CarrotFarmerTileEntity::new, ModBlocks.CARROT_FARMER_BLOCK);
    public static final RegistryObject<TileEntityType<PotatoFarmerTileEntity>> POTATO_FARMER = register("potato_farmer", PotatoFarmerTileEntity::new, ModBlocks.POTATO_FARMER_BLOCK);
    public static final RegistryObject<TileEntityType<BeetrootFarmerTileEntity>> BEETROOT_FARMER = register("beetroot_farmer", BeetrootFarmerTileEntity::new, ModBlocks.BEETROOT_FARMER_BLOCK);
    public static final RegistryObject<TileEntityType<ShugarcaneFarmerTileEntity>> SHUGARCANE_FARMER = register("shugarcane_farmer", ShugarcaneFarmerTileEntity::new, ModBlocks.SHUGARCANE_FARMER_BLOCK);
    public static final RegistryObject<TileEntityType<CactusFarmerTileEntity>> CACTUS_FARMER = register("cactus_farmer", CactusFarmerTileEntity::new, ModBlocks.CACTUS_FARMER_BLOCK);
    public static final RegistryObject<TileEntityType<PumpkinFarmerTileEntity>> PUMPKIN_FARMER = register("pumpkin_farmer", PumpkinFarmerTileEntity::new, ModBlocks.PUMPKIN_FARMER_BLOCK);
    public static final RegistryObject<TileEntityType<MelonFarmerTileEntity>> MELON_FARMER = register("melon_farmer", MelonFarmerTileEntity::new, ModBlocks.MELON_FARMER_BLOCK);
    public static final RegistryObject<TileEntityType<TreeFarmerTileEntity>> TREE_FARMER = register("tree_farmer", TreeFarmerTileEntity::new, ModBlocks.TREE_FARMER_BLOCK);

    static void register() {}

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block) {
        return Registration.TILE_ENTITIES.register(name, () -> {
            return Builder.of(factory, block.get()).build(null);
        });
    }
}
