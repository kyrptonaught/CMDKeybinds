package net.kyrptonaught.cmdkeybind.scripting.modules;

import net.minecraft.client.MinecraftClient;

public class Command {

    public void runCommand(String command){
        MinecraftClient.getInstance().player.sendChatMessage(command);
    }
}
