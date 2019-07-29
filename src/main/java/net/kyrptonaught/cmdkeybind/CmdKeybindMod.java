package net.kyrptonaught.cmdkeybind;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.kyrptonaught.cmdkeybind.config.ConfigManager;
import net.kyrptonaught.cmdkeybind.config.ConfigOptions;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class CmdKeybindMod implements ClientModInitializer {
    public static final String MOD_ID = "cmdkeybind";
    public static ConfigManager config = new ConfigManager();

    public static List<Macro> macros = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        config.loadConfig();
        buildMacros();
        ClientTickCallback.EVENT.register(e ->
        {
            if (e.currentScreen == null) {
                long hndl = MinecraftClient.getInstance().window.getHandle();
                for (Macro macro : macros)
                    if (macro.isTriggered(hndl)) {
                        if (macro.canExecute())
                            macro.execute(e.player);
                    } else macro.resetTimer();
            }
        });
    }

    public static void buildMacros() {
        macros.clear();
        ConfigOptions options = config.getConfig();
        if (options.enabled)
            for (ConfigOptions.ConfigMacro macro : options.macros) {
                macros.add(new Macro(macro.keyName, macro.command, macro.delay == -1 ? 0 : macro.delay + options.globalDelay));
            }
    }

    public static void addEmptyMacro() {
        config.getConfig().macros.add(new ConfigOptions.ConfigMacro());
        buildMacros();
        config.saveConfig();
    }
}
