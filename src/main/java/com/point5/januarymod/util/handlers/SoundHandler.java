package com.point5.januarymod.util.handlers;

import com.point5.januarymod.JanuaryMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber(modid = JanuaryMod.MODID)
public class SoundHandler {
	
	public static SoundEvent REINDEER_HURT;
	public static SoundEvent REINDEER_DEATH;
	public static SoundEvent REINDEER_LIVING;
	public static SoundEvent REINDEER_BEGGING;
	public static SoundEvent FOX_HURT;
	public static SoundEvent FOX_DEATH;
	public static SoundEvent FOX_LIVING;
	public static SoundEvent FOX_SNIFFING;
	public static SoundEvent FOX_EATING;
	public static SoundEvent FOX_SLEEPING;
	
	
	public static void registerSounds()
	{
		REINDEER_HURT = registerSound("reindeer.hurt");
		REINDEER_DEATH = registerSound("reindeer.death");
		REINDEER_LIVING = registerSound("reindeer.living");
		REINDEER_BEGGING = registerSound("reindeer.begging");
		FOX_HURT = registerSound("fox_hurt");
		FOX_DEATH = registerSound("fox_death");
		FOX_LIVING = registerSound("fox_idle");
		FOX_SNIFFING = registerSound("fox_sniffing");
		FOX_EATING = registerSound("fox_eat");
		FOX_SLEEPING = registerSound("fox_sleep");
	}
	
	private static SoundEvent registerSound(String name)
	{
		ResourceLocation location = new ResourceLocation(JanuaryMod.MODID, name);
		SoundEvent event = new SoundEvent(location);
		
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		
		return event;
	}

}
