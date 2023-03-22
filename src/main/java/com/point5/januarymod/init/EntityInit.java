package com.point5.januarymod.init;

import com.point5.januarymod.JanuaryMod;
import com.point5.januarymod.config.ModConfig;
import com.point5.januarymod.entity.fox.EntityFox;
import com.point5.januarymod.entity.reindeer.EntityReindeer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {
	
	 public static void registerEntities()
	    {
	        registerEntity("reindeer", EntityReindeer.class, JanuaryMod.REINDEER_ID, 50, 6281112, 100115140);
	        registerEntity("fox", EntityFox.class, JanuaryMod.FOX_ID, 51, 6281200, 100115200);
	     

	        if(ModConfig.spawnCreatureInOverworld)
	        {
	           
	            EntityRegistry.addSpawn(EntityReindeer.class, ModConfig.spawnRates.get("reindeer"), 0, 10, EnumCreatureType.CREATURE, Biomes.RIVER, Biomes.FOREST, Biomes.COLD_TAIGA, Biomes.COLD_TAIGA_HILLS, Biomes.FOREST_HILLS, Biomes.FROZEN_RIVER, Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.ICE_PLAINS);
	            EntityRegistry.addSpawn(EntityFox.class, ModConfig.spawnRates.get("fox"), 0, 10, EnumCreatureType.CREATURE, Biomes.RIVER, Biomes.FOREST, Biomes.COLD_TAIGA, Biomes.COLD_TAIGA_HILLS, Biomes.FOREST_HILLS, Biomes.FROZEN_RIVER, Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.ICE_PLAINS);
	        }
	    }

	    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
	    {
	        EntityRegistry.registerModEntity(new ResourceLocation(JanuaryMod.MODID + ":" + name), entity, name, id, JanuaryMod.instance, range, 1, true, color1, color2);
	    }
}
