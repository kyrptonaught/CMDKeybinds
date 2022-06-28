package net.kyrptonaught.cmdkeybind.MacroTypes;

import net.minecraft.client.network.ClientPlayerEntity;

public class RepeatAfterReleaseMacro extends BaseMacro {
    private final int delay;
    private final int times;
    private int count;
    private long sysTimePressed = 0;
    private long currentTime;
    private boolean wasTriggered;

    public RepeatAfterReleaseMacro(String key, String keyMod, String command, int delay, int times) {
        super(key, keyMod, command);
        this.delay = delay;
        this.times = times;
    }

    @Override
    public void tick(long hndl, ClientPlayerEntity player, long currentTime) {
        this.currentTime = currentTime;
        if (isTriggered(hndl)) {
            wasTriggered = true;
            count = 0;
            if (canExecute()) {
                execute(player);
                count = 1;
            }
        } else if (wasTriggered) {
            if (count > times)
                wasTriggered = false;
            else if (canExecute()) {
                execute(player);
                count++;
            }
        } else {
            sysTimePressed = 0;
        }
    }

    private boolean canExecute() {
        if (delay > 0) {
            if (sysTimePressed == 0) return true;
            return currentTime - sysTimePressed > delay;
        }
        return true;
    }

    protected void execute(ClientPlayerEntity player) {
        sysTimePressed = currentTime;
        super.execute(player);
    }
}
