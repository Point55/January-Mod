package com.point5.januarymod.entity.reindeer;

import com.point5.januarymod.entity.GeoModelRenderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderReindeer extends GeoModelRenderer<EntityReindeer> {
	

	public RenderReindeer(RenderManager renderManager)
	{
		super(renderManager, new ModelReindeer());
		this.shadowSize = 1.0F;
	}
	
	@Override
	public void renderEarly(EntityReindeer animatable, float ticks, float red, float green, float blue, float partialTicks)
	{
		if (animatable.isChild()) {
            GlStateManager.scale(1.0F, 1.0F, 1.0F);
        }
	        GlStateManager.scale(0.9F, 0.9F, 0.9F);
	}
	
}
