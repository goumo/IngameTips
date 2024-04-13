package com.goumo.ingametips;

import com.goumo.ingametips.client.UnlockedTipManager;
import com.goumo.ingametips.item.DebugItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(IngameTips.MOD_ID)
public class IngameTips {
    public static final String MOD_ID = "ingametips";
    public static final File CONFIG = new File(FMLPaths.CONFIGDIR.get().toFile(), "ingametips");
    public static final File TIPS = new File(CONFIG, "tips");
    public static final File UNLCOKED_FILE = new File(CONFIG, "unlocked_tips.json");
    private static final Logger LOGGER = LogManager.getLogger();

    public static final UnlockedTipManager unlockedTipManager = new UnlockedTipManager();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    static {
        ITEMS.register("debug_item", DebugItem::new);
    }

    public IngameTips() {
        if (TIPS.mkdir()) {
            LOGGER.info("Config path created");
        }
        unlockedTipManager.loadFromFile();
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}