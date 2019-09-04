package net.kyrptonaught.cmdkeybind.MacroTypes;


import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public abstract class BaseMacro {
    public enum MacroType {
        Delayed, Repeating, SingleUse, DisplayOnly
    }

    private InputUtil.KeyCode keyCode, keyMod;
    protected String command;

    BaseMacro(String key, String keyMod, String command) {
        this.keyCode = InputUtil.fromName(key);
        this.keyMod = InputUtil.fromName(keyMod);
        this.command = command;

    }

    public void tick(long hndl, ClientPlayerEntity player, long currentTime) {
    }

    boolean isTriggered(long hndl) {
        boolean modTriggered = true;
        if (keyMod.getKeyCode() != -1) {
            if (keyMod.getCategory() == InputUtil.Type.MOUSE)
                modTriggered = GLFW.glfwGetMouseButton(hndl, keyMod.getKeyCode()) == 1;
            else modTriggered = GLFW.glfwGetKey(hndl, keyMod.getKeyCode()) == 1;
        }

        if (keyCode.getCategory() == InputUtil.Type.MOUSE)
            return modTriggered && GLFW.glfwGetMouseButton(hndl, keyCode.getKeyCode()) == 1;
        return modTriggered && GLFW.glfwGetKey(hndl, keyCode.getKeyCode()) == 1;
    }
}