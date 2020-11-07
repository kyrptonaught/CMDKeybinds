package net.kyrptonaught.cmdkeybind.MacroTypes;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public abstract class BaseMacro {
    public enum MacroType {
        Delayed, Repeating, SingleUse, DisplayOnly, ToggledRepeating
    }

    private InputUtil.Key keyCode, keyMod;
    protected String command;

    BaseMacro(String key, String keyMod, String command) {
        this.keyCode = InputUtil.fromTranslationKey(key);
        this.keyMod = InputUtil.fromTranslationKey(keyMod);
        this.command = command;

    }

    public void tick(long hndl, ClientPlayerEntity player, long currentTime) {
    }

    boolean isTriggered(long hndl) {
        boolean modTriggered = true;
        if (keyMod.getCode() != -1) {
            if (keyMod.getCategory() == InputUtil.Type.MOUSE)
                modTriggered = GLFW.glfwGetMouseButton(hndl, keyMod.getCode()) == 1;
            else modTriggered = GLFW.glfwGetKey(hndl, keyMod.getCode()) == 1;
        }

        if (keyCode.getCategory() == InputUtil.Type.MOUSE)
            return modTriggered && GLFW.glfwGetMouseButton(hndl, keyCode.getCode()) == 1;
        return modTriggered && GLFW.glfwGetKey(hndl, keyCode.getCode()) == 1;
    }

    protected void execute(ClientPlayerEntity player) {
        player.sendChatMessage(this.command);
    }
}