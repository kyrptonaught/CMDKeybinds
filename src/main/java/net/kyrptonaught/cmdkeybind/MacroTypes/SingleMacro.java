package net.kyrptonaught.cmdkeybind.MacroTypes;


import net.minecraft.client.network.ClientPlayerEntity;

public class SingleMacro extends BaseMacro {
    private boolean keyWasPressed;

    public SingleMacro(String key, String command) {
        super(key, command);
    }

    public void tick(long hndl, ClientPlayerEntity player, long currentTime) {
        if (isTriggered(hndl)) {
            if (!keyWasPressed)
                execute(player);
        } else keyWasPressed = false;
    }

    private void execute(ClientPlayerEntity player) {
        keyWasPressed = true;
        player.sendChatMessage(this.command);
    }
}
