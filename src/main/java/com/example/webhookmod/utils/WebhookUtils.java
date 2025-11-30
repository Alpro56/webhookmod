package com.example.webhookmod.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class WebhookUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    public static String resolvePlaceholders(String message, CommandSourceStack source) throws CommandSyntaxException {
        String result = message;

        if (result.contains("@a")) {
            List<ServerPlayer> players = source.getServer().getPlayerList().getPlayers();
            String playerNames = players.stream()
                .map(player -> player.getGameProfile().getName())
                .collect(Collectors.joining(", "));
            result = result.replace("@a", playerNames);
        }

        if (result.contains("@p")) {
            ServerPlayer closest = getClosestPlayer(source);
            if (closest != null) {
                result = result.replace("@p", closest.getGameProfile().getName());
            }
        }

        return result;
    }

    private static ServerPlayer getClosestPlayer(CommandSourceStack source) throws CommandSyntaxException {
        ServerLevel level = source.getLevel();
        Vec3 position = source.getPosition();
        return level.players().stream()
            .min(Comparator.comparingDouble(player -> player.position().distanceToSqr(position)))
            .orElse(null);
    }

    public static CompletableFuture<Void> sendWebhookAsync(String webhookUrl, String content) {
        return CompletableFuture.runAsync(() -> sendWebhook(webhookUrl, content));
    }

    private static void sendWebhook(String webhookUrl, String content) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String payload = String.format("{\"content\": \"%s\"}", escapeJson(content));
            byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(payloadBytes);
            }

            int responseCode = connection.getResponseCode();
            LOGGER.info("Webhook response code: {}", responseCode);
        } catch (Exception exception) {
            LOGGER.error("Error sending webhook", exception);
        }
    }

    private static String escapeJson(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }
}
