package net.kyrptonaught.cmdkeybind;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.kyrptonaught.cmdkeybind.config.ConfigManager;
import net.kyrptonaught.cmdkeybind.config.ConfigOptions;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class CmdKeybindMod implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "cmdkeybind";
	public static ConfigManager config = new ConfigManager();

	@Override
	public void onInitialize() {
	}

	@Override
	public void onInitializeClient() {
		config.loadConfig();
		ConfigOptions options = config.getConfig();
		ClientTickCallback.EVENT.register(e ->
		{
			if (e.currentScreen == null) {
				long hndl = MinecraftClient.getInstance().window.getHandle();
				for (int i = 0; i < options.macros.size(); i++)
					if (options.macros.get(i).isTriggered(hndl))
						options.macros.get(i).trigger(e.player);
			}
		});
	}


}
