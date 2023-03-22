package com.point5.januarymod.util.handlers;

import com.point5.januarymod.init.EntityInit;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RegistryHandler {
	
	    public static void preInitRegistries()
	    {
	        EntityInit.registerEntities();
	        RenderHandler.registerEntityRenders();
	    }

	    public static void initRegistries()
	    {
	        SoundHandler.registerSounds();
	    }

}
