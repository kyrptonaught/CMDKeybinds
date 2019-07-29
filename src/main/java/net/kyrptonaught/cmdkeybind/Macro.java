package net.kyrptonaught.cmdkeybind;


import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Macro {
    private InputUtil.KeyCode keyCode;
    private String command;
    private int delay;
    private long triggeredTime = 0;

    public Macro(String key, String command, int delay) {
        this.keyCode = InputUtil.fromName(key);
        this.command = command;
        this.delay = delay;
    }

    public boolean isTriggered(long hndl) {
        if (keyCode.getCategory() == InputUtil.Type.MOUSE)
            return GLFW.glfwGetMouseButton(hndl, keyCode.getKeyCode()) == 1;
        return GLFW.glfwGetKey(hndl, keyCode.getKeyCode()) == 1;
    }

    public void resetTimer() {
        triggeredTime = 0;
    }

    public void execute(ClientPlayerEntity player) {
        triggeredTime = System.currentTimeMillis();
        player.sendChatMessage(this.command);
    }

    public boolean canExecute() {
        return delay <= 0 || triggeredTime == 0 || System.currentTimeMillis() - triggeredTime > delay;
    }
}
