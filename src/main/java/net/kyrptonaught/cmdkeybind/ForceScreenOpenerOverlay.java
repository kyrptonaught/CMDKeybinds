package net.kyrptonaught.cmdkeybind;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class ForceScreenOpenerOverlay extends Overlay {
    private final Overlay oldOverlay;
    private final MinecraftClient client;
    private final Screen screenToOpen;

    public ForceScreenOpenerOverlay(MinecraftClient client, Screen screenToOpen) {
        this.client = client;
        this.oldOverlay = client.getOverlay();
        this.screenToOpen = screenToOpen;
    }

    @Override
    public boolean pausesGame() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (client.currentScreen == null) {
            client.setScreen(screenToOpen);
            client.setOverlay(oldOverlay);
        }
    }
}
