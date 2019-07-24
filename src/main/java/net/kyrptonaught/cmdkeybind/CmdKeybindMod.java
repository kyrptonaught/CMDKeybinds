package net.kyrptonaught.cmdkeybind;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.kyrptonaught.cmdkeybind.config.ConfigManager;
import net.kyrptonaught.cmdkeybind.config.ConfigOptions;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class CmdKeybindMod implements ClientModInitializer {
    public static final String MOD_ID = "cmdkeybind";
    public static ConfigManager config = new ConfigManager();

    @Override
    public void onInitializeClient() {
        config.loadConfig();
        List<ConfigOptions.ConfigKeyBind> macros = config.getConfig().macros;
        ClientTickCallback.EVENT.register(e ->
        {
            if (e.currentScreen == null) {
                long hndl = MinecraftClient.getInstance().window.getHandle();
                for (ConfigOptions.ConfigKeyBind macro : macros)
                    if (macro.isTriggered(hndl))
                        macro.trigger(e.player);
            }
        });
    }
}
