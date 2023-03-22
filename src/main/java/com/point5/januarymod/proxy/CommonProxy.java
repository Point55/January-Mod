package com.point5.januarymod.proxy;

import com.point5.januarymod.JanuaryMod;
import com.point5.januarymod.entity.fox.EntityFox;
import com.point5.januarymod.init.EntityInit;
import com.point5.januarymod.util.handlers.RegistryHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber (modid = JanuaryMod.MODID)
public class CommonProxy {


    public void preInitializationEvent(FMLPreInitializationEvent event)
    {
    	EntityInit.registerEntities();
    }


    public void initializationEvent(FMLInitializationEvent event)
    {
    	RegistryHandler.initRegistries();
    }
    

    public void postInitializationEvent(FMLPostInitializationEvent event) 
    {

    }
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event)
    {
    	
    }
    
    @SubscribeEvent
    public static void registerBlocksItems(final RegistryEvent.Register<Item> event)
    {
    	
    }
  
    @SideOnly(Side.CLIENT)
    public void doParticlesSimple(String type, EntityFox fox) {

        double px, py, pz, vx, vy, vz;

        int i = fox.getRNG().nextInt(4) + 3;
        for (int j = 0; j < i; ++j) {

            px = fox.posX + fox.getRNG().nextFloat() * fox.width - fox.width;
            py = fox.getEntityBoundingBox().minY + 0.3D + fox.getRNG().nextFloat() * fox.height;
            pz = fox.posZ + fox.getRNG().nextFloat() * fox.width - fox.width;

            vx = fox.getRNG().nextGaussian() * 0.02D;
            vy = fox.getRNG().nextGaussian() * 0.02D;
            vz = fox.getRNG().nextGaussian() * 0.02D;

            doParticleEffect(type, fox, px, py, pz, vx, vy, vz);
        }
    }

    @SideOnly(Side.CLIENT)
    public void doParticleEffect(String type, EntityFox fox, double xpos, double ypos, double zpos, double xvel, double yvel, double zvel) {

        EnumParticleTypes particle = null;

        switch (type) {

        case "shake":
            particle = EnumParticleTypes.WATER_SPLASH;
            break;
        }

        if (particle != null) { fox.world.spawnParticle(particle, true, xpos, ypos, zpos, xvel, yvel, zvel); }
    }
    
 // Sleep
    @SideOnly(Side.CLIENT)
 	public void Sleep(EntityPlayer entityplayer)
 	{
 		long currentTime = 0;
 		int factorTime = 0;

 		for (int j = 0; j < entityplayer.world.getMinecraftServer().getServer().worlds.length; ++j)
 		{
 			currentTime = entityplayer.world.getMinecraftServer().getServer().worlds[j].getWorldTime() % 24000;
 			factorTime = 24000 - (int) currentTime;
 			entityplayer.world.getMinecraftServer().getServer().worlds[j].setWorldTime(entityplayer.world.getMinecraftServer().getServer().worlds[j].getWorldTime() + factorTime);
 		}
 	}
    
}
