package net.kyrptonaught.cmdkeybind.MacroTypes;


import net.minecraft.client.network.ClientPlayerEntity;

public class SingleMacro extends BaseMacro {
    private boolean keyWasPressed;

    public SingleMacro(String key, String keyMod, String command) {
        super(key, keyMod, command);
    }

    public void tick(long hndl, ClientPlayerEntity player, long currentTime) {
        if (isTriggered(hndl)) {
            if (!keyWasPressed)
                execute(player);
        } else keyWasPressed = false;
    }

    protected void execute(ClientPlayerEntity player) {
        keyWasPressed = true;
        super.execute(player);
    }
}
