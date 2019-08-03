package net.kyrptonaught.cmdkeybind.MacroTypes;


import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public abstract class Macro {
    public enum MacroType {
        Delayed, Repeating, SingleUse, DisplayOnly
    }

    private InputUtil.KeyCode keyCode;
    protected String command;

    public Macro(String key, String command) {
        this.keyCode = InputUtil.fromName(key);
        this.command = command;

    }

    public void tick(long hndl, ClientPlayerEntity player, long currentTime) {
    }

    protected boolean isTriggered(long hndl) {
        if (keyCode.getCategory() == InputUtil.Type.MOUSE)
            return GLFW.glfwGetMouseButton(hndl, keyCode.getKeyCode()) == 1;
        return GLFW.glfwGetKey(hndl, keyCode.getKeyCode()) == 1;
    }
}