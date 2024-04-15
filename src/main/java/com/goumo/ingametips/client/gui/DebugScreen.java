package com.goumo.ingametips.client.gui;

import com.goumo.ingametips.client.UnlockedTipManager;
import com.goumo.ingametips.client.gui.widget.IconButton;
import com.goumo.ingametips.client.util.TipDisplayUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class DebugScreen extends Screen {
    public DebugScreen() {
        super(new TextComponent(""));
    }

    @Override
    protected void init() {
        this.addRenderableWidget(new IconButton((int) (this.width*0.5-25), (int) (this.height*0.4), IconButton.ICON_TRASH_CAN, 0xFFC6FCFF, new TranslatableComponent("tip.gui.clear_cache"), (b) -> {
            TipDisplayUtil.clearCache();
        }));
        this.addRenderableWidget(new IconButton((int) (this.width*0.5-5), (int) (this.height*0.4), IconButton.ICON_CROSS, 0xFFC6FCFF, new TranslatableComponent("tip.gui.clear_queue"), (b) -> {
            TipDisplayUtil.clearRenderQueue();
        }));
        this.addRenderableWidget(new IconButton((int) (this.width*0.5+15), (int) (this.height*0.4), IconButton.ICON_HISTORY, 0xFFFF5340, new TranslatableComponent("tip.gui.reset_unlock"), (b) -> {
            UnlockedTipManager.manager.createFile();
        }));
    }

    @Override
    public void render(PoseStack ps, int mouseX, int mouseY, float frameTime) {
        fill(ps, (int) (this.width*0.5-30), (int) (this.height*0.4-5), (int) (this.width*0.5+30), (int) (this.height*0.4+15), 0x80000000);
        super.render(ps, mouseX, mouseY, frameTime);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
