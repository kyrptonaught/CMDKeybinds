package net.kyrptonaught.cmdkeybind.scripting.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class RawInput {

    public void pressKeyRaw(String keyName) {
        KeyBinding.onKeyPressed(InputUtil.fromName(keyName));
    }

    public void holdKeyRaw(String keyName, Boolean value) {
        KeyBinding.setKeyPressed(InputUtil.fromName(keyName), value);
    }

    public void pressKey(String keyName) {
        KeyBinding[] keys = MinecraftClient.getInstance().options.keysAll;
        for (KeyBinding key : keys) {
            if (key.getId().equals(keyName)) {
                KeyBinding.onKeyPressed(InputUtil.fromName(key.getName()));
                break;
            }
        }
    }

    public void holdKey(String keyName, Boolean value) {
        KeyBinding[] keys = MinecraftClient.getInstance().options.keysAll;
        for (KeyBinding key : keys) {
            if (key.getId().equals(keyName)) {
                key.setPressed(value);
                break;
            }
        }
    }
}
