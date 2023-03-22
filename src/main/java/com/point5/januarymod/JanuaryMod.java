package com.point5.januarymod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import com.point5.januarymod.proxy.CommonProxy;
import com.point5.januarymod.tabs.JanuaryTab;
import com.point5.januarymod.util.handlers.RegistryHandler;

@Mod(modid = JanuaryMod.MODID, name = JanuaryMod.NAME, version = JanuaryMod.VERSION)
public class JanuaryMod
{
    public static final String MODID = "januarymod";
    public static final String NAME = "January Mod";
    public static final String VERSION = "1.0";
    
    public static final CreativeTabs itemsblockstab = new JanuaryTab("januarymod");
	public static final int REINDEER_ID = 120;
	public static final int FOX_ID = 121;

    private static Logger logger;
    
    @SidedProxy(clientSide = "com.point5.januarymod.proxy.ClientProxy", serverSide = "com.point5.januarymod.proxy.ServerProxy")
	public static CommonProxy proxy;
    
    @Mod.Instance
	public static JanuaryMod instance;

    @Mod.EventHandler
    public void preInitializationEvent (FMLPreInitializationEvent event)
    {
    	proxy.preInitializationEvent(event);
        logger = event.getModLog();
    }
    
    @Mod.EventHandler
    public void initializationEvent (FMLInitializationEvent event)
    {
        proxy.initializationEvent(event);
        // some example code
        // logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
    
    @Mod.EventHandler
    public void postInitializationEvent (FMLPostInitializationEvent event) {
    	proxy.postInitializationEvent(event);
    }
    
    public static String appendModID(String value)
    {
    	return MODID + ":" + value;
    }

    
}
