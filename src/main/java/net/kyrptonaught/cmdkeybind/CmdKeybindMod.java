package net.kyrptonaught.cmdkeybind;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CmdKeybindMod implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "cmdkeybind";
	public static ConfigManager config = new ConfigManager();

	@Override
	public void onInitialize() {
	}

	@Override
	public void onInitializeClient() {
		config.loadConfig();
		ConfigOptions options = CmdKeybindMod.config.getConfig();
		ClientTickCallback.EVENT.register(e ->
		{
			if (e.currentScreen == null) {
				long hndl = MinecraftClient.getInstance().window.getHandle();
				for (int i = 0; i < options.macros.size(); i++) {
					if (options.macros.get(i).inputType == InputUtil.Type.MOUSE) {
						if (GLFW.glfwGetMouseButton(hndl, options.macros.get(i).getKeyCode()) == 1)
							e.player.sendChatMessage(options.macros.get(i).command);
					} else if (GLFW.glfwGetKey(hndl, options.macros.get(i).getKeyCode()) == 1)
						e.player.sendChatMessage(options.macros.get(i).command);
					e.player.inventory.getMainHandStack().getEnchantments()
				}
			}
		});
	}
}
