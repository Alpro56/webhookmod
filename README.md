# WebhookMod

Forge 1.20.1 mod that adds a `/webhook post <url> <message>` command to send HTTP POST requests to webhooks. Placeholders like `@a` (all players) and `@p` (nearest player) are expanded before sending the payload.

## Usage

In-game command example:

```
/webhook post https://example.com/webhook Hello @a!
```

The mod asynchronously posts a JSON payload with a `content` field so webhook targets like Discord can receive player-driven events.

## Building

This repository includes Gradle configuration for Forge 1.20.1. If the Gradle wrapper JAR is missing in your environment, run `gradle wrapper` locally to regenerate it before invoking `./gradlew`.
