package net.kyrptonaught.cmdkeybind.MacroTypes;

public class ToggledRepeating extends RepeatingMacro {

    private boolean toggledOn = false;

    public ToggledRepeating(String key, String keyMod, String command, int delay) {
        super(key, keyMod, command, delay);
    }

    boolean prevTriggered = false;

    protected boolean isTriggered(long hndl) {
        boolean isTriggered = super.isTriggered(hndl);
        if (prevTriggered && !isTriggered) toggledOn = !toggledOn;
        prevTriggered = isTriggered;
        return toggledOn;
    }
}
