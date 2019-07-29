package net.kyrptonaught.cmdkeybind.config;

import blue.endless.jankson.Comment;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ConfigOptions {

    public boolean enabled = true;

    @Comment("Global delay(Milliseconds) between command execution")
    public int globalDelay = 500;

    public List<ConfigMacro> macros = new ArrayList<>();

    public static class ConfigMacro {
        @Comment("Macro keybinding")
        public String keyName;
        @Comment("Command to execute")
        public String command;
        @Comment("Individual delay, added onto global delay. -1 ignores delay")
        public int delay;

        public ConfigMacro() {
            this.keyName = InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_O).getName();
            this.command = "/help";
            this.delay = 0;
        }
    }
}
