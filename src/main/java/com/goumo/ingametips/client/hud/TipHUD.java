package com.goumo.ingametips.client.hud;

import com.goumo.ingametips.IngameTips;
import com.goumo.ingametips.client.TipElement;
import com.goumo.ingametips.client.gui.EmptyScreen;
import com.goumo.ingametips.client.gui.widget.IconButton;
import com.goumo.ingametips.client.util.AnimationUtil;
import com.goumo.ingametips.client.util.GuiUtil;
import com.goumo.ingametips.client.util.Point;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.List;

public class TipHUD extends GuiComponent {
    private final Minecraft mc = Minecraft.getInstance();
    private final PoseStack ps;
    private final TipElement element;
    private final int lineSpace = 12;
    private final boolean alwaysVisible;

    private int descLines = 0;
    private int titleLines = 0;
    private int extendedWidth = 0;
    private int extendedHeight = 0;
    private boolean fadeIn = true;
    private boolean fadeOut = false;
    private boolean alwaysVisibleOverride = false;

    public boolean visible = true;

    public TipHUD(PoseStack matrixStack, TipElement element) {
        this.ps = matrixStack;
        this.element = element;
        this.alwaysVisible = element.alwaysVisible;
    }

    public void render(boolean isGUI) {
        if (!visible) return;

        float fadeProgress = 1.0F;
        float defaultBGAlpha = isGUI ? 0.75F : 0.3F;
        float BGAlpha = defaultBGAlpha;
        float fontAlpha = 1.0F;

        Point mainWindow = new Point(mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
        Point renderPos1 = new Point((int)(mainWindow.X * 0.7)-extendedWidth, (int)(mainWindow.Y * 0.3)-extendedHeight);
        Point renderPos2 = new Point((int)(mainWindow.X * 0.99F), lineSpace);

        //最大和最小宽度
        if (renderPos2.X - renderPos1.X > 360) {
            renderPos1.X = renderPos1.X - (360 - (renderPos2.X - renderPos1.X));
        } else if (renderPos2.X - renderPos1.X < 160) {
            renderPos1.X = renderPos1.X - (160 - (renderPos2.X - renderPos1.X));
        }

        //文本超出窗口时调整尺寸
        if ((descLines + titleLines+1)*lineSpace + renderPos1.Y > mainWindow.Y) {
            if (descLines >= element.contents.size() && renderPos1.Y > 40) {
                extendedHeight += 24;

            } else if ((descLines + titleLines+1)*lineSpace + renderPos1.Y > mainWindow.Y) {
                if (descLines >= element.contents.size() && renderPos1.X > mainWindow.X * 0.5) {
                    extendedWidth += 24;
                } else {
                    GuiUtil.drawWrapString(I18n.get("tip." + IngameTips.MOD_ID + ".too_long"), ps, mc.font,
                            8, 8, (int)(mainWindow.X*0.5F),
                            element.fontColor, lineSpace, true);
                }
            }
        }

        if (fadeOut) {
            float progress = 1-AnimationUtil.calcFadeIn(400, "TipFadeOut", false);
            fadeProgress = progress;

            if (progress == 0) {
                visible = false;
                return;
            } else {
                BGAlpha = defaultBGAlpha * progress;
                fontAlpha = Math.max(progress, 0.02F);
            }
        } else if (fadeIn) {
            float progress = AnimationUtil.calcFadeIn(400, "TipFadeIn", false);
            fadeProgress = progress;

            if (progress == 1.0F) {
                fadeIn = false;
            } else {
                BGAlpha = defaultBGAlpha * progress;
                fontAlpha = Math.max(progress, 0.02F);
            }
        }

        int BGColor = (int)(BGAlpha * 255.0F) << 24 | element.BGColor & 0x00FFFFFF;
        int fontColor = (int)(fontAlpha * 255.0F) << 24 | element.fontColor & 0x00FFFFFF;
        float yaw = 0;
        float pitch = 0;
        if (mc.player != null) {
            if (mc.isPaused()) {
                yaw   = mc.player.getViewYRot(mc.getFrameTime()) - mc.player.yBob;
                pitch = mc.player.getViewXRot(mc.getFrameTime()) - mc.player.xBob;
            } else {
                yaw   = mc.player.getViewYRot(mc.getFrameTime())
                        - Mth.lerp(mc.getFrameTime(), mc.player.yBobO, mc.player.yBob);
                pitch = mc.player.getViewXRot(mc.getFrameTime())
                        - Mth.lerp(mc.getFrameTime(), mc.player.xBobO, mc.player.xBob);
            }
        }

        ps.pushPose();
        ps.translate(-yaw*0.1F + fadeProgress*16 - 16, -pitch*0.1F, 1000);

        renderContent(element.contents, renderPos1, fontColor, renderPos2, BGColor);

        renderButton(renderPos2.X - 13, renderPos1.Y-1, fontColor);

        if (!isAlwaysVisible() && fadeProgress == 1.0F) {
            //进度条
            float lineProgress = 1-AnimationUtil.calcProgress(element.visibleTime, "TipVisibleTime", false);
            int x = renderPos1.X-4;
            int y = renderPos1.Y + (titleLines+1)*lineSpace;
            int x2 = renderPos2.X - x;

            if (lineProgress == 0) {
                fadeOut = true;
            } else {
                ps.pushPose();
                ps.translate(x, y, 0);
                ps.scale(lineProgress, 1, 1);
                fill(ps, 0, 0, x2, 1, fontColor);
                ps.popPose();
            }
        } else if (isAlwaysVisible() || fadeIn) {
            fill(ps, renderPos1.X - 4, renderPos1.Y + (titleLines+1)*lineSpace,
                    renderPos2.X, renderPos1.Y + (titleLines+1)*lineSpace + 1, fontColor);
        }
        ps.popPose();
    }

    private void renderContent(List<Component> texts, Point renderPos1, int fontColor, Point renderPos2, int BGColor) {
        int BGPosX = renderPos1.X - 4;
        int width = renderPos2.X- BGPosX;

        if (texts.size() > 1) {
            fill(ps, BGPosX, renderPos1.Y - 4, renderPos2.X,
                    renderPos2.Y*2 + renderPos1.Y + 4 + (descLines -1)*lineSpace, BGColor);
            descLines = 0;
            //标题
            int t = -1;
            t += GuiUtil.formatAndDraw(texts.get(0), ps, mc.font, renderPos1.X, renderPos1.Y,
                    width-16, fontColor, lineSpace, false);
            descLines += t;
            titleLines = t;
            //内容
            for (int dt = 1; dt < texts.size(); dt++) {
                descLines += GuiUtil.formatAndDraw(texts.get(dt), ps, mc.font,
                        renderPos1.X, descLines*lineSpace + renderPos1.Y+17,
                        width-8, fontColor, lineSpace, false);
            }
        } else {//只有标题
            fill(ps, BGPosX, renderPos1.Y - 4,
                renderPos2.X, renderPos2.Y + renderPos1.Y + (descLines)*lineSpace, BGColor);
            descLines = 0;

            int t = -1;
            t += GuiUtil.formatAndDraw(texts.get(0), ps, mc.font,
                    renderPos1.X, renderPos1.Y, width-16, fontColor, lineSpace, false);
            descLines += t;
            titleLines = t;
        }
    }

    private void renderButton(int x, int y, int color) {
        if (notFading() && (mc.screen != null || GuiUtil.isKeyDown(258))) {
            if (mc.screen == null) mc.setScreen(new EmptyScreen());

            if (GuiUtil.renderIconButton(ps, IconButton.ICON_CROSS, GuiUtil.getMouseX(), GuiUtil.getMouseY(), x, y, color, 0)) {
                if (notFading()) {
                    fadeOut = true;
                }
            }
            //标题超过 1 行时把锁定按钮从左边移动到下面
            if (titleLines > 1){
                if (!isAlwaysVisible() && GuiUtil.renderIconButton(ps, IconButton.ICON_LOCK, GuiUtil.getMouseX(), GuiUtil.getMouseY(), x, y+10, color, 0))
                    alwaysVisibleOverride = true;
            } else {
                if (!isAlwaysVisible() && GuiUtil.renderIconButton(ps, IconButton.ICON_LOCK, GuiUtil.getMouseX(), GuiUtil.getMouseY(), x-15, y, color, 0))
                    alwaysVisibleOverride = true;
            }
        } else {
            GuiUtil.renderIcon(ps, IconButton.ICON_CROSS, x, y, color);
        }
    }

    public boolean isAlwaysVisible() {
        return alwaysVisible || alwaysVisibleOverride;
    }

    public boolean notFading() {
        return !(fadeIn || fadeOut);
    }
}
