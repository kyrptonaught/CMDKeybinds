package net.kyrptonaught.cmdkeybind.MacroTypes;

import net.minecraft.client.network.ClientPlayerEntity;

public class RunNTimesMacro extends BaseMacro {
    private final int delay;
    private long sysTimePressed = 0;
    private long currentTime;

    private final int totalRuns;
    private int runsLeft = 0;
    private boolean isRepeating = false;

    public RunNTimesMacro(String key, String keyMod, String command, int delay, int totalRuns) {
        super(key, keyMod, command);
        this.delay = delay;
        this.totalRuns = totalRuns;
    }

    @Override
    public void tick(long hndl, ClientPlayerEntity player, long currentTime) {
        this.currentTime = currentTime;

        if (!isRepeating && wasPressed()) {
            isRepeating = true;
            runsLeft = totalRuns;
            sysTimePressed = 0;
        }

        if (isRepeating) {
            if (canExecute()) {
                if (runsLeft > 0) {
                    execute(player);
                    runsLeft--;
                } else if (!wasPressed) {
                    isRepeating = false;
                }
            }
        }
        super.tick(hndl, player, currentTime);
    }

    protected boolean canExecute() {
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