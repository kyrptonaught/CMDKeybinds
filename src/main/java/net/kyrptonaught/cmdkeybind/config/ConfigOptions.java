package net.kyrptonaught.cmdkeybind.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.cmdkeybind.MacroTypes.BaseMacro;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ConfigOptions {

    public boolean enabled = true;

    public List<ConfigMacro> macros = new ArrayList<>();

    public static class ConfigMacro {
        @Comment("Macro keybinding")
        public String keyName;
        @Comment("Key modifier")
        public String keyModName;
        @Comment("Command to execute")
        public String command;
        @Comment("Type of Macro. Delayed, Repeating, SingleUse, DisplayOnly")
        public BaseMacro.MacroType macroType;
        @Comment("Delay(Milliseconds) used for the delay type.")
        public int delay;


        public ConfigMacro() {
            this.keyName = InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_KP_0).getName();
            this.keyModName = InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_UNKNOWN).getName();
            this.command = "/say Command Macros!";
            macroType = BaseMacro.MacroType.SingleUse;
            this.delay = 0;
        }
    }
}
