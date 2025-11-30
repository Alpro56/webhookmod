package com.example.webhookmod.commands;

import com.example.webhookmod.utils.WebhookUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

public class WebhookCommand {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("webhook")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("post")
                .then(Commands.argument("url", StringArgumentType.string())
                    .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(WebhookCommand::executePost)))));
    }

    private static int executePost(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String url = StringArgumentType.getString(context, "url");
        String message = StringArgumentType.getString(context, "message");

        CommandSourceStack source = context.getSource();
        String processedMessage = WebhookUtils.resolvePlaceholders(message, source);

        CompletableFuture<Void> future = WebhookUtils.sendWebhookAsync(url, processedMessage);
        future.thenRun(() -> source.sendSuccess(() -> Component.literal("Webhook enviado exitosamente!"), false))
            .exceptionally(throwable -> {
                LOGGER.error("Failed to send webhook", throwable);
                source.sendFailure(Component.literal("No se pudo enviar el webhook. Verifica la consola."));
                return null;
            });

        return 1;
    }
}
