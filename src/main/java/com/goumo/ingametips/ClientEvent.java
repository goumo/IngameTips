package com.goumo.ingametips;

import com.goumo.ingametips.client.TipElement;
import com.goumo.ingametips.client.UnlockedTipManager;
import com.goumo.ingametips.client.util.TipDisplayUtil;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void displayFileReadError(ScreenOpenEvent event) {
        if (event.getScreen() instanceof TitleScreen && !UnlockedTipManager.error.isEmpty()) {
            TipElement ele = new TipElement();
            ele.replaceToError(IngameTips.UNLCOKED_FILE, UnlockedTipManager.error);
            TipDisplayUtil.displayTip(ele, true);
            UnlockedTipManager.error = "";
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        TipDisplayUtil.clearRenderQueue();
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        TipDisplayUtil.clearRenderQueue();
    }
}
