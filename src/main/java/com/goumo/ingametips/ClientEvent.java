package com.goumo.ingametips;

import com.goumo.ingametips.client.TipElement;
import com.goumo.ingametips.client.TipHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void displayFileReadError(TickEvent event) {
        if (TipHandler.readError) {
            TipElement ele = new TipElement();
            ele.replaceToError(TipHandler.UNLOCKED_FILEPATH, "load");
            TipHandler.displayTip(ele, true);
            TipHandler.readError = false;
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        TipHandler.clearRenderQueue();
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        TipHandler.clearRenderQueue();
    }
}
