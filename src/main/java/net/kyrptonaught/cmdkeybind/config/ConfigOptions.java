package net.kyrptonaught.cmdkeybind.config;

import blue.endless.jankson.Comment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ConfigOptions {
    @Comment("Array of all macros")
    public List<ConfigKeyBind> macros = new ArrayList<>();

    public static class ConfigKeyBind {
        @Comment("Macro keybinding")
        public String keyName;
        @Comment("Command to execute")
        public String command;

        private transient InputUtil.KeyCode keyCode;

        public ConfigKeyBind() {
            command = "/help";
            this.keyName = getName(0, GLFW.GLFW_KEY_O);
            this.keyCode = InputUtil.fromName(keyName);

        }

        public void updateKey(String key) {
            this.keyName = key;
            this.keyCode = InputUtil.fromName(keyName);
        }

        public static String getName(int type, int code) {
            InputUtil.Type temp = type == 0 ? InputUtil.Type.KEYSYM : InputUtil.Type.MOUSE;
            return temp.createFromCode(code).getName();
        }

        public boolean isTriggered(long hndl) {
            if (keyCode.getCategory() == InputUtil.Type.MOUSE)
                return GLFW.glfwGetMouseButton(hndl, keyCode.getKeyCode()) == 1;
            return GLFW.glfwGetKey(hndl, keyCode.getKeyCode()) == 1;
        }

        public void trigger(ClientPlayerEntity player) {
            player.sendChatMessage(this.command);
        }
    }
}
