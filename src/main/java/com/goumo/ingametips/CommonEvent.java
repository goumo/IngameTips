package com.goumo.ingametips;

import com.goumo.ingametips.network.Networking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class CommonEvent {
    @SubscribeEvent
    public static void onPlayerPickupItem(PlayerEvent.ItemPickupEvent event) {
        if (!event.getPlayer().level.isClientSide() && event.getStack().getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation(IngameTips.MOD_ID, "test_item"))) {
            Networking.send((ServerPlayer) event.getPlayer(), "default");
        }
    }

}