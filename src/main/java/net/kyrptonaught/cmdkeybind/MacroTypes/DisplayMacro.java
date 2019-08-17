package net.kyrptonaught.cmdkeybind.MacroTypes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;

public class DisplayMacro extends BaseMacro {
    public DisplayMacro(String key, String command) {
        super(key, command);
    }

    public void tick(long hndl, ClientPlayerEntity player, long currentTime) {
        if (isTriggered(hndl)) {
            execute(player);
        }
    }

    private void execute(ClientPlayerEntity player) {
        MinecraftClient.getInstance().openScreen(new ChatScreen(this.command));
    }
}
