package com.point5.januarymod;

import com.point5.januarymod.entity.fox.EntityFox;
import com.point5.januarymod.entity.reindeer.EntityReindeer;
import com.point5.januarymod.util.handlers.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public class CommonListener {
	


	@SubscribeEvent
	public void onRegisterEntities(RegistryEvent.Register<EntityEntry> event)
	{
		int id = 0;

		event.getRegistry().register(EntityEntryBuilder.create().entity(EntityReindeer.class).name("Reindeer").id(new ResourceLocation(JanuaryMod.MODID, "reindeer"), id++).tracker(160, 2, false).build());
		event.getRegistry().register(EntityEntryBuilder.create().entity(EntityFox.class).name("Reindeer").id(new ResourceLocation(JanuaryMod.MODID, "fox"), id++).tracker(161, 2, false).build());

	}

	@SubscribeEvent
	public void onRegisterSoundEvents(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().register(SoundHandler.REINDEER_HURT);
		event.getRegistry().register(SoundHandler.REINDEER_DEATH);
		event.getRegistry().register(SoundHandler.REINDEER_LIVING);
		event.getRegistry().register(SoundHandler.REINDEER_BEGGING);
	}


}
