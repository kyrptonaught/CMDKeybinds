package net.kyrptonaught.cmdkeybind;

import blue.endless.jankson.Comment;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ConfigOptions {
    @Comment("Array of all macros")
    public List<MacroKeyBind> macros = new ArrayList<>();

    public static class MacroKeyBind {
        @Comment("Macro keybinding")
        private String keyName;
        @Comment("Command to execute")
        public String command;
        @Comment("Input source -- Keyboard: KEYSYM / Mouse: MOUSE")
        public InputUtil.Type inputType;
        private transient int keyCode = -1;

        public MacroKeyBind(String cmd, int keyName, InputUtil.Type type) {
            command = cmd;
            this.inputType = type;
            this.keyName = getName(type, keyName);

        }

        public MacroKeyBind() {
            this("/help", GLFW.GLFW_KEY_F2, InputUtil.Type.KEYSYM);
        }

        public void updateKey(String key, InputUtil.Type type) {
            this.inputType = type;
            this.keyName = key;
            this.keyCode = InputUtil.fromName(keyName).getKeyCode();
        }

        int getKeyCode() {
            if (keyCode == -1) keyCode = InputUtil.fromName(keyName).getKeyCode();
            return keyCode;
        }

        String getKeyName() {
            return keyName;
        }

        public static String getName(InputUtil.Type type, int code) {
            return type.createFromCode(code).getName();
        }
    }
}
