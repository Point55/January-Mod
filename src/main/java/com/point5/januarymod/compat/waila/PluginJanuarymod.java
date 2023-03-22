package com.point5.januarymod.compat.waila;

import com.point5.januarymod.entity.fox.EntityFox;
import com.point5.januarymod.entity.reindeer.EntityReindeer;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class PluginJanuarymod implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(HUDHandlerFox.INSTANCE, EntityFox.class);
        registrar.registerNBTProvider(HUDHandlerFox.INSTANCE, EntityFox.class);
        registrar.registerBodyProvider(HUDHandlerReindeer.INSTANCE, EntityReindeer.class);
        registrar.registerNBTProvider(HUDHandlerReindeer.INSTANCE, EntityReindeer.class);
    }
}
