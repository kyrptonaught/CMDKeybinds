package net.kyrptonaught.cmdkeybind.MacroTypes;

import net.minecraft.client.network.ClientPlayerEntity;

public class DelayedMacro extends BaseMacro {
    private int delay;
    private long sysTimePressed = 0;
    private long currentTime;

    public DelayedMacro(String key, String keyMod, String command, int delay) {
        super(key, keyMod, command);
        this.delay = delay;
    }

    public void tick(long hndl, ClientPlayerEntity player, long currentTime) {
        this.currentTime = currentTime;
        if (isTriggered(hndl)) {
            if (canExecute())
                execute(player);
        }
        if (delayed())
            execute(player);

    }

    private boolean canExecute() {
        if (delay > 0) {
            if (sysTimePressed == 0) {
                sysTimePressed = currentTime;
            }
            return false;
        }
        return true;
    }

    private boolean delayed() {
        if (delay > 0 && sysTimePressed > 0) {
            return sysTimePressed + delay < currentTime;
        }
        return false;
    }

    protected void execute(ClientPlayerEntity player) {
        sysTimePressed = 0;
        super.execute(player);
    }
}
