package net.kyrptonaught.cmdkeybind.scripting.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class PlayerData {
    public int getX() {
        return MinecraftClient.getInstance().player.getBlockPos().getX();
    }

    public int getY() {
        return MinecraftClient.getInstance().player.getBlockPos().getY();
    }

    public int getZ() {
        return MinecraftClient.getInstance().player.getBlockPos().getZ();
    }

    public void addChatMessage(String message) {
        MinecraftClient.getInstance().player.addChatMessage(new LiteralText(message), false);
    }
}
