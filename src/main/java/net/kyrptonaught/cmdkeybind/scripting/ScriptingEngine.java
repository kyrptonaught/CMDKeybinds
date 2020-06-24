package net.kyrptonaught.cmdkeybind.scripting;


import net.kyrptonaught.cmdkeybind.scripting.modules.Command;
import net.kyrptonaught.cmdkeybind.scripting.modules.PlayerData;
import net.kyrptonaught.cmdkeybind.scripting.modules.RawInput;

import javax.script.*;
import java.util.concurrent.*;

public class ScriptingEngine {
    static ScriptEngine engine;

    public static void init() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        ScriptContext ctx = engine.getContext();
        ctx.setAttribute("rawInput", new RawInput(), ScriptContext.ENGINE_SCOPE);
        ctx.setAttribute("player", new PlayerData(), ScriptContext.ENGINE_SCOPE);
        ctx.setAttribute("command", new Command(), ScriptContext.ENGINE_SCOPE);
    }


    public static void run(String key) {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            try {
                engine.eval(key);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        });
        executor.shutdownNow();
    }
}
