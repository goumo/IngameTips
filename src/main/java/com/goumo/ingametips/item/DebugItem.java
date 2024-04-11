package com.goumo.ingametips.item;

import com.goumo.ingametips.client.TipHandler;
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
        if (level.isClientSide()) {
            TipHandler.openDebugScreen();
        }
        return super.use(level, player, hand);
    }
}
