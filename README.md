# WebhookMod

Forge 1.20.1 mod that adds a `/webhook post <url> <message>` command to send HTTP POST requests to webhooks. Placeholders like `@a` (all players) and `@p` (nearest player) are expanded before sending the payload.

## Usage

In-game command example:

```
/webhook post https://example.com/webhook Hello @a!
```

The mod asynchronously posts a JSON payload with a `content` field so webhook targets like Discord can receive player-driven events.

## Building

1. Install JDK 17 (Forge 1.20.1 requires Java 17 toolchains). Set `JAVA_HOME` accordingly or ensure `java -version` reports Java 17.
2. If `gradle/wrapper/gradle-wrapper.jar` is missing, regenerate it once with a local Gradle install:
   ```
   gradle wrapper
   ```
   (Any Gradle 8.x install works; this only creates the wrapper files.)
3. Build the mod with the wrapper:
   ```
   ./gradlew build
   ```
4. The compiled mod JAR will appear at `build/libs/webhookmod-1.0.0.jar` (alongside `-sources` and `-dev` variants). Drop the main JAR into your Forge 1.20.1 `mods/` folder.
