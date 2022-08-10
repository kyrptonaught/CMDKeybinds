package net.kyrptonaught.cmdkeybind;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.kyrptonaught.cmdkeybind.MacroTypes.*;
import net.kyrptonaught.cmdkeybind.config.ConfigOptions;
import net.kyrptonaught.cmdkeybind.config.MacroScreenFactory;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;
import net.kyrptonaught.kyrptconfig.keybinding.DisplayOnlyKeyBind;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class CmdKeybindMod implements ClientModInitializer {
    public static final String MOD_ID = "cmdkeybind";
    public static ConfigManager.SingleConfigManager config = new ConfigManager.SingleConfigManager(MOD_ID, new ConfigOptions());

    public static List<BaseMacro> macros = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        config.load();
        if (getConfig().macros.size() == 0) addEmptyMacro();
        buildMacros();
        ClientTickEvents.START_WORLD_TICK.register(clientWorld -> {
            if (MinecraftClient.getInstance().currentScreen == null) {
                if (getConfig().openMacroScreenKeybind.isKeybindPressed()) {
                    MinecraftClient.getInstance().setScreen(MacroScreenFactory.buildScreen(null));
                    return;
                }
                long hndl = MinecraftClient.getInstance().getWindow().getHandle();
                long curTime = System.currentTimeMillis();
                for (BaseMacro macro : macros)
                    macro.tick(hndl, MinecraftClient.getInstance().player, curTime);
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("cmdkeybinds")
                        .then(ClientCommandManager.literal("showconfig")
                                .executes(context -> {
                                    //Semi hacky solution to get the screen to open. This workaround avoids a mixin.
                                    //The chat window auto closes any open screen after command execution. Opening a screen would immediately close it.
                                    MinecraftClient.getInstance().setOverlay(new ForceScreenOpenerOverlay(MinecraftClient.getInstance(), MacroScreenFactory.buildScreen(null)));
                                    return Command.SINGLE_SUCCESS;
                                }))));

        KeyBindingHelper.registerKeyBinding(new DisplayOnlyKeyBind(
                "key.cmdkeybind.config.openmacrokeybind",
                "key.cmdkeybind.config.title",
                getConfig().openMacroScreenKeybind,
                setKey -> CmdKeybindMod.config.save()
        ));
    }


    public static ConfigOptions getConfig() {
        return (ConfigOptions) config.getConfig();
    }

    public static void buildMacros() {
        macros.clear();
        ConfigOptions options = getConfig();
        if (options.enabled)
            for (ConfigOptions.ConfigMacro macro : options.macros) {
                if (macro.macroType == null) macro.macroType = BaseMacro.MacroType.SingleUse;
                switch (macro.macroType) {
                    case Delayed -> macros.add(new DelayedMacro(macro.keyName, macro.keyModName, macro.command, macro.delay));
                    case Repeating -> macros.add(new RepeatingMacro(macro.keyName, macro.keyModName, macro.command, macro.delay));
                    case SingleUse -> macros.add(new SingleMacro(macro.keyName, macro.keyModName, macro.command));
                    case DisplayOnly -> macros.add(new DisplayMacro(macro.keyName, macro.keyModName, macro.command));
                    case ToggledRepeating -> macros.add(new ToggledRepeating(macro.keyName, macro.keyModName, macro.command, macro.delay));
                    case RunNTimes -> macros.add(new RunNTimesMacro(macro.keyName, macro.keyModName, macro.command, macro.delay, macro.repetitions));
                }
            }
    }

    public static void addEmptyMacro() {
        getConfig().macros.add(new ConfigOptions.ConfigMacro());
        buildMacros();
        config.save();
    }
}
