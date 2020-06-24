package net.kyrptonaught.cmdkeybind;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.command.arguments.FunctionArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunctionManager;

public class ClientCommands implements ClientCommandPlugin {
/*
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("script").executes(
                source -> {
                    source.getSource().sendFeedback(new LiteralText("Hello, world!"));
                    return 1;
                }
        ));
    }


 */
public static final SuggestionProvider<CottonClientCommandSource> SUGGESTION_PROVIDER = (commandContext, suggestionsBuilder) -> {
    CommandFunctionManager commandFunctionManager = ((ServerCommandSource)commandContext.getSource()).getMinecraftServer().getCommandFunctionManager();
    CommandSource.suggestIdentifiers(commandFunctionManager.getTags().getKeys(), suggestionsBuilder, "#");
    return CommandSource.suggestIdentifiers((Iterable)commandFunctionManager.getFunctions().keySet(), suggestionsBuilder);
};
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register((ArgumentBuilders.literal("script").
                requires((serverCommandSource_1) -> serverCommandSource_1.hasPermissionLevel(0))
                .then(ArgumentBuilders.argument("name", FunctionArgumentType.function()).suggests(SUGGESTION_PROVIDER))
                .executes((commandContext_1) -> execute(commandContext_1.getSource(), commandContext_1))));
    }
    private static int execute(CottonClientCommandSource serverCommandSource, CommandContext<CottonClientCommandSource> commandContext_1) {
        System.out.println(commandContext_1.getInput());
        return 1;
    }
}