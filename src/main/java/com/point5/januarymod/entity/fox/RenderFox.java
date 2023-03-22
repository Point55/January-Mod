package com.point5.januarymod.entity.fox;

import com.point5.januarymod.JanuaryMod;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

	@SideOnly(Side.CLIENT)
	public class RenderFox extends RenderLiving<EntityFox>
	{
	    private static final ResourceLocation FOX_TEXTURE1 = new ResourceLocation(JanuaryMod.MODID, "textures/model/entity/fox.png");
	    private static final ResourceLocation FOX_TEXTURE2 = new ResourceLocation(JanuaryMod.MODID, "textures/model/entity/fox2.png");
	    private static final ResourceLocation FOX_SLEEPING1 = new ResourceLocation(JanuaryMod.MODID, "textures/model/entity/fox_sleeping.png");
	    private static final ResourceLocation FOX_SLEEPING2 = new ResourceLocation(JanuaryMod.MODID, "textures/model/entity/fox_sleeping2.png");

	    
	    public RenderFox(RenderManager p_i47187_1_)
	    {
	        super(p_i47187_1_, new ModelFox(), 0.5F);
	    }

	    public void doRender(EntityFox entity, double x, double y, double z, float entityYaw, float partialTicks)
	    {
	        super.doRender(entity, x, y, z, entityYaw, partialTicks);
	    }

	    protected ResourceLocation getEntityTexture(EntityFox entity)
	    {
			boolean isSleeping = false;
			if (entity.isSleeping())
			{
				isSleeping = true;
			}
			if (isSleeping)
			{
	    		switch (entity.getVariant())
		    	{
		    		case 0:
		    		default:
		    			return FOX_SLEEPING1;
		    		case 1:
		    			return FOX_SLEEPING2;
		    	}
			}
			else
			{
	    	switch (entity.getVariant())
	    	{
	    		case 0:
	    		default:
	    			return FOX_TEXTURE1;
	    		case 1:
	    			return FOX_TEXTURE2;
	    	}
			}
	    }
	}
	

	
