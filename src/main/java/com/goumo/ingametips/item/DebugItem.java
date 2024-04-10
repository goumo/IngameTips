package com.goumo.ingametips.item;

import com.goumo.ingametips.client.TipHandler;
import com.goumo.ingametips.client.gui.TipListScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugItem extends Item {
    public DebugItem(){
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide() && Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.isCrouching()) {
                TipHandler.resetTipAnimation();
                TipHandler.clearCache();
                TipHandler.clearRenderQueue();
                TipListScreen.select = "";
                TipHandler.resetUnlockedFile();
                TipHandler.loadUnlockedFromFile();
                player.sendMessage(new TextComponent("clear!"), player.getUUID());
            } else {
                TipListScreen.select = "test";
            }
        }
        return super.use(level, player, hand);
    }
}
