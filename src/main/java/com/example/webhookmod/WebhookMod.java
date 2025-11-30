package com.example.webhookmod;

import com.example.webhookmod.commands.WebhookCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(WebhookMod.MODID)
public class WebhookMod {
    public static final String MODID = "webhookmod";
    private static final Logger LOGGER = LogManager.getLogger();

    public WebhookMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("WebhookMod common setup initialized.");
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        WebhookCommand.register(event.getDispatcher());
    }
}
