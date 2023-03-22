package com.point5.januarymod.config;

import java.util.HashMap;
import java.util.Map;

import com.point5.januarymod.JanuaryMod;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = JanuaryMod.MODID)
public class ModConfig {

	    @Config.LangKey("config.januarymod.overworld_creature_spawn_chance")
	    @Config.Comment({"A value from 1 to 0, the lower the number the less"})
	    public static float overworldCreatureSpawnChance = 0.1f;


	    @Config.RequiresMcRestart
	    @Config.LangKey("config.januarymod.spawn_rates")
	    @Config.Comment({"They can go from 100 to 0",
	            "..."})
	    public static Map<String, Integer> spawnRates = new HashMap<String, Integer>(){{
	        put("reindeer", 10);
	        put("fox", 10);
	    }};

	    @Config.RequiresMcRestart
	    @Config.LangKey("config.januarymod.enable_overworld_spawn")
	    @Config.Comment({"enable or disable spawning"})
		public static boolean spawnCreatureInOverworld = true;


	    @Mod.EventBusSubscriber(modid = JanuaryMod.MODID)
	    private static class EventHandler {
	        @SubscribeEvent
	        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
	            if (event.getModID().equals(JanuaryMod.MODID)) {
	                ConfigManager.sync(JanuaryMod.MODID, Config.Type.INSTANCE);
	            }
	        }
	    }
}
