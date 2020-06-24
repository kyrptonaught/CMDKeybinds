package net.kyrptonaught.cmdkeybind.MacroTypes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;

public class DisplayMacro extends BaseMacro {
    public DisplayMacro(String key,String keyMod, String command) {
        super(key,keyMod, command);
    }

    public void tick(long hndl, ClientPlayerEntity player, long currentTime) {
        if (isTriggered(hndl)) {
            execute(player);
        }
    }

    protected void execute(ClientPlayerEntity player) {
        MinecraftClient.getInstance().openScreen(new ChatScreen(this.command));
    }
}
