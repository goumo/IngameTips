package com.goumo.ingametips;

import com.goumo.ingametips.item.DebugItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;

@Mod(IngameTips.MOD_ID)
public class IngameTips {
    public static final String MOD_ID = "ingametips";
    public static final File CONFIG = new File(FMLPaths.CONFIGDIR.get().toFile(), "ingametips");
    public static final File TIPS = new File(CONFIG, "tips");
    public static final File UNLCOKED_FILE = new File(CONFIG, "unlocked_tips.json");

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public IngameTips() {
        ITEMS.register("debug_item", DebugItem::new);
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}