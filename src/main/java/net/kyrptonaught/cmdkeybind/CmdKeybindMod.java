package net.kyrptonaught.cmdkeybind;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.kyrptonaught.cmdkeybind.MacroTypes.*;
import net.kyrptonaught.cmdkeybind.config.ConfigManager;
import net.kyrptonaught.cmdkeybind.config.ConfigOptions;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class CmdKeybindMod implements ClientModInitializer {
    public static final String MOD_ID = "cmdkeybind";
    public static ConfigManager config = new ConfigManager();

    public static List<BaseMacro> macros = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        config.loadConfig();
        if (config.getConfig().macros.size() == 0) addEmptyMacro();
        buildMacros();
        ClientTickCallback.EVENT.register(e ->
        {
            if (e.currentScreen == null) {
                long hndl = MinecraftClient.getInstance().getWindow().getHandle();
                long curTime = System.currentTimeMillis();
                for (BaseMacro macro : macros)
                    macro.tick(hndl, e.player, curTime);
            }
        });

    }

    public static void buildMacros() {
        macros.clear();
        ConfigOptions options = config.getConfig();
        if (options.enabled)
            for (ConfigOptions.ConfigMacro macro : options.macros) {
                if (macro.macroType == null) macro.macroType = BaseMacro.MacroType.SingleUse;
                switch (macro.macroType) {
                    case Delayed:
                        macros.add(new DelayedMacro(macro.keyName, macro.keyModName, macro.command, macro.delay));
                        break;
                    case Repeating:
                        macros.add(new RepeatingMacro(macro.keyName, macro.keyModName, macro.command, macro.delay));
                        break;
                    case SingleUse:
                        macros.add(new SingleMacro(macro.keyName, macro.keyModName, macro.command));
                        break;
                    case DisplayOnly:
                        macros.add(new DisplayMacro(macro.keyName, macro.keyModName, macro.command));
                        break;
                }
            }
    }

    public static void addEmptyMacro() {
        config.getConfig().macros.add(new ConfigOptions.ConfigMacro());
        buildMacros();
        config.saveConfig();
    }
}
