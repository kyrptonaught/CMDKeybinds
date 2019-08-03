package net.kyrptonaught.cmdkeybind.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.cmdkeybind.MacroTypes.Macro;
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
        @Comment("Command to execute")
        public String command;
        @Comment("Type of Macro. Delayed, Repeating, SingleUse, DisplayOnly")
        public Macro.MacroType macroType;
        @Comment("Delay(Milliseconds) used for the delay type.")
        public int delay;


        public ConfigMacro() {
            this.keyName = InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_KP_0).getName();
            this.command = "/say Command Macros!";
            macroType = Macro.MacroType.SingleUse;
            this.delay = 0;
        }
    }
}
