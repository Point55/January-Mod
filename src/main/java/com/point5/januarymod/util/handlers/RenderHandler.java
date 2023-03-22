package com.point5.januarymod.util.handlers;

import com.point5.januarymod.entity.fox.EntityFox;
import com.point5.januarymod.entity.fox.RenderFox;
import com.point5.januarymod.entity.reindeer.EntityReindeer;
import com.point5.januarymod.entity.reindeer.RenderReindeer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@SideOnly(Side.CLIENT)
public class RenderHandler {
	
	public static void registerEntityRenders()
    {
		RenderingRegistry.registerEntityRenderingHandler(EntityReindeer.class, new IRenderFactory<EntityReindeer>()
		{
				@Override
				public Render<? super EntityReindeer> createRenderFor(RenderManager manager)
				{
				return new RenderReindeer(manager);
				}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityFox.class, new IRenderFactory<EntityFox>()
		{
				@Override
				public Render<? super EntityFox> createRenderFor(RenderManager manager)
				{
				return new RenderFox(manager);
				}
		});

    }
}
