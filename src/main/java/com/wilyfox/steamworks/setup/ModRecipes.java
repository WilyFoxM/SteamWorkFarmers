package com.wilyfox.steamworks.setup;

import com.wilyfox.steamworks.SteamWorks;
import com.wilyfox.steamworks.crafting.recipe.StorageRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModRecipes {
    public static class Types {
        public static final IRecipeType<StorageRecipe> STORAGE = IRecipeType.register(SteamWorks.MODID + "storage");
    }

    public static class Serializers {
        public static final RegistryObject<IRecipeSerializer<StorageRecipe>> STORAGE = register("storage", StorageRecipe.Serializer::new);
        private static <T extends IRecipe<?>>RegistryObject<IRecipeSerializer<T>> register(String name, Supplier<IRecipeSerializer<T>> serializer) {
            return Registration.RECIPE_SERIALIZERS.register(name, serializer);
        }
    }

    static void register() {

    }
}
