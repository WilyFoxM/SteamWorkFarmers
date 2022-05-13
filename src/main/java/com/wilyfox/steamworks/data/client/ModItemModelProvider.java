package com.wilyfox.steamworks.data.client;

import com.wilyfox.steamworks.SteamWorks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SteamWorks.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("copper_ore", modLoc("block/copper_ore"));
        withExistingParent("copper_block", modLoc("block/copper_block"));

        ModelFile ItemGenerated = getExistingFile(mcLoc("item/generated"));

        builder(ItemGenerated, "copper_ingot");
    }

    private ItemModelBuilder builder(ModelFile ItemGenerated, String name) {
        return getBuilder(name).parent(ItemGenerated).texture("layer0", "item/".concat(name));
    }
}