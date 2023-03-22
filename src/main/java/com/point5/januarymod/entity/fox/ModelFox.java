package com.point5.januarymod.entity.fox;

import org.jline.utils.Log;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.14 with MCP mappings
// Paste this class into your mod and generate all required imports

@SideOnly(Side.CLIENT)
public class ModelFox extends ModelBase {
	private ModelRenderer body;
	private ModelRenderer head;
	private ModelRenderer leg0;
	private ModelRenderer leg1;
	private ModelRenderer leg2;
	private ModelRenderer leg3;
	private ModelRenderer tail;

	public ModelFox() {
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 16.0F, 0.0F);
		setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 24, 15, -3.0F, -3.0F, -3.0F, 6, 11, 6, 0.0F, false));

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 15.3F, -3.0F);
		head.cubeList.add(new ModelBox(head, 1, 5, -4.0F, -2.0F, -6.0F, 8, 6, 6, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 8, 1, -4.0F, -4.0F, -5.0F, 2, 2, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 15, 1, 2.0F, -4.0F, -5.0F, 2, 2, 1, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 6, 18, -2.0F, 2.0F, -9.0F, 4, 2, 3, 0.0F, false));

		leg0 = new ModelRenderer(this);
		leg0.setRotationPoint(-3.0F, 18.0F, 6.0F);
		leg0.cubeList.add(new ModelBox(leg0, 13, 24, -0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

		leg1 = new ModelRenderer(this);
		leg1.setRotationPoint(1.0F, 18.0F, 6.0F);
		leg1.cubeList.add(new ModelBox(leg1, 4, 24, 0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

		leg2 = new ModelRenderer(this);
		leg2.setRotationPoint(-3.0F, 18.0F, -1.0F);
		leg2.cubeList.add(new ModelBox(leg2, 13, 24, -0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

		leg3 = new ModelRenderer(this);
		leg3.setRotationPoint(1.0F, 18.0F, -1.0F);
		leg3.cubeList.add(new ModelBox(leg3, 4, 24, 0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

		tail = new ModelRenderer(this);
		tail.setRotationPoint(0.0F, 16.0F, 7.0F);
		setRotationAngle(tail, 1.5708F, 0.0F, 0.0F);
		tail.cubeList.add(new ModelBox(tail, 30, 0, -2.0F, 1.0F, -2.25F, 4, 9, 5, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		
		EntityFox fox = (EntityFox) entity;
		
		if (fox.isSleeping())
		{
			Log.info(fox.isSleeping());
			if (this.isChild)
		    {
					body = new ModelRenderer(this);
					body.setRotationPoint(0.0F, 21.0F, 0.0F);
					setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
					body.cubeList.add(new ModelBox(body, 24, 15, -3.0F, -3.0F, -3.0F, 6, 11, 6, 0.0F, false));

					head = new ModelRenderer(this);
					head.setRotationPoint(-1.0F, 19.8F, -3.0F);
					setRotationAngle(head, 0.0F, -1.9635F, 0.0F);
					head.cubeList.add(new ModelBox(head, 1, 5, -4.0F, -2.0F, -6.0F, 8, 6, 6, 0.0F, false));
					head.cubeList.add(new ModelBox(head, 8, 1, -4.0F, -4.0F, -5.0F, 2, 2, 1, 0.0F, false));
					head.cubeList.add(new ModelBox(head, 15, 1, 2.0F, -4.0F, -5.0F, 2, 2, 1, 0.0F, false));
					head.cubeList.add(new ModelBox(head, 6, 18, -2.0F, 2.0F, -9.0F, 4, 2, 3, 0.0F, false));

					leg0 = new ModelRenderer(this);
					leg0.setRotationPoint(-2.8F, 24.0F, 6.0F);
					setRotationAngle(leg0, 0.0F, 0.0F, -1.5708F);
					leg0.cubeList.add(new ModelBox(leg0, 13, 24, -0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

					leg1 = new ModelRenderer(this);
					leg1.setRotationPoint(-2.9F, 20.9F, 6.0F);
					setRotationAngle(leg1, 0.0F, 0.0F, -1.5708F);
					leg1.cubeList.add(new ModelBox(leg1, 4, 24, 0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

					leg2 = new ModelRenderer(this);
					leg2.setRotationPoint(-2.9F, 23.7F, -1.0F);
					setRotationAngle(leg2, 0.0F, 0.0F, -1.5708F);
					leg2.cubeList.add(new ModelBox(leg2, 13, 24, -0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

					leg3 = new ModelRenderer(this);
					leg3.setRotationPoint(-2.9F, 20.5F, -1.0F);
					setRotationAngle(leg3, 0.0F, 0.0F, -1.5708F);
					leg3.cubeList.add(new ModelBox(leg3, 4, 24, 0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

					tail = new ModelRenderer(this);
					tail.setRotationPoint(0.0F, 21.3F, 8.6F);
					setRotationAngle(tail, -1.5708F, 0.8029F, 3.1416F);
					tail.cubeList.add(new ModelBox(tail, 30, 0, -2.0F, 1.0F, -2.25F, 4, 9, 5, 0.0F, false));
		            float f = 2.0F;
		            GlStateManager.pushMatrix();
		            GlStateManager.translate(0.0F, 10.0F * scale, 1.0F * scale);
		            GlStateManager.scale(0.6F, 0.6F, 0.6F);
		            this.head.render(scale);
		            GlStateManager.popMatrix();
		            GlStateManager.pushMatrix();
		            GlStateManager.scale(0.5F, 0.5F, 0.5F);
		            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
		            this.body.render(scale);
		            this.leg0.render(scale);
		            this.leg1.render(scale);
		            this.leg2.render(scale);
		            this.leg3.render(scale);
		            this.tail.render(scale);
		            GlStateManager.popMatrix();
		    	}
				else
				{
					Log.info("Fox is Sleeping");
					body = new ModelRenderer(this);
					body.setRotationPoint(0.0F, 21.0F, 0.0F);
					setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
					body.cubeList.add(new ModelBox(body, 24, 15, -3.0F, -3.0F, -3.0F, 6, 11, 6, 0.0F, false));

					head = new ModelRenderer(this);
					head.setRotationPoint(-1.0F, 19.8F, -3.0F);
					setRotationAngle(head, 0.0F, -1.9635F, 0.0F);
					head.cubeList.add(new ModelBox(head, 1, 5, -4.0F, -2.0F, -6.0F, 8, 6, 6, 0.0F, false));
					head.cubeList.add(new ModelBox(head, 8, 1, -4.0F, -4.0F, -5.0F, 2, 2, 1, 0.0F, false));
					head.cubeList.add(new ModelBox(head, 15, 1, 2.0F, -4.0F, -5.0F, 2, 2, 1, 0.0F, false));
					head.cubeList.add(new ModelBox(head, 6, 18, -2.0F, 2.0F, -9.0F, 4, 2, 3, 0.0F, false));

					leg0 = new ModelRenderer(this);
					leg0.setRotationPoint(-2.8F, 24.0F, 6.0F);
					setRotationAngle(leg0, 0.0F, 0.0F, -1.5708F);
					leg0.cubeList.add(new ModelBox(leg0, 13, 24, -0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

					leg1 = new ModelRenderer(this);
					leg1.setRotationPoint(-2.9F, 20.9F, 6.0F);
					setRotationAngle(leg1, 0.0F, 0.0F, -1.5708F);
					leg1.cubeList.add(new ModelBox(leg1, 4, 24, 0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

					leg2 = new ModelRenderer(this);
					leg2.setRotationPoint(-2.9F, 23.7F, -1.0F);
					setRotationAngle(leg2, 0.0F, 0.0F, -1.5708F);
					leg2.cubeList.add(new ModelBox(leg2, 13, 24, -0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

					leg3 = new ModelRenderer(this);
					leg3.setRotationPoint(-2.9F, 20.5F, -1.0F);
					setRotationAngle(leg3, 0.0F, 0.0F, -1.5708F);
					leg3.cubeList.add(new ModelBox(leg3, 4, 24, 0.005F, 0.0F, -1.0F, 2, 6, 2, 0.0F, false));

					tail = new ModelRenderer(this);
					tail.setRotationPoint(0.0F, 21.3F, 8.6F);
					setRotationAngle(tail, -1.5708F, 0.8029F, 3.1416F);
					tail.cubeList.add(new ModelBox(tail, 30, 0, -2.0F, 1.0F, -2.25F, 4, 9, 5, 0.0F, false));
					this.head.render(scale);
					this.body.render(scale);
					this.leg0.render(scale);
					this.leg1.render(scale);
					this.leg2.render(scale);
					this.leg3.render(scale);
					this.tail.render(scale);
			}
		}
		else if (this.isChild)
		{
            float f = 2.0F;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 10.0F * scale, 1.0F * scale);
            GlStateManager.scale(0.6F, 0.6F, 0.6F);
            this.head.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.body.render(scale);
            this.leg0.render(scale);
            this.leg1.render(scale);
            this.leg2.render(scale);
            this.leg3.render(scale);
            this.tail.render(scale);
            GlStateManager.popMatrix();
		}
		else
	    {
	    	 body.render(scale);
	    	 head.render(scale);
	    	 leg0.render(scale);
	    	 leg1.render(scale);
	    	 leg2.render(scale);
	    	 leg3.render(scale);
	    	 tail.render(scale);
	    }

	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity e) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, e);
		this.head.rotateAngleY = netHeadYaw / 57.295776F;
		this.head.rotateAngleX = headPitch / 57.295776F;
	}
	
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
	{
	    EntityFox entityfox = (EntityFox)entitylivingbaseIn;

	 if (entityfox.isSitting())
	    {
         this.body.setRotationPoint(0.0F, 16.0F, 0.0F);
         this.body.rotateAngleX = 0.7418F;
         
         this.head.setRotationPoint(0.0F, 12.3F, -1.3F);
         
         this.leg0.setRotationPoint(-3.0F, 22.5F, 3.6F);
         this.leg0.rotateAngleX = -1.4764F;
        		 this.leg0.rotateAngleY =  0.3911F;
        		 this.leg0.rotateAngleZ = 0.0361F;
         
         this.leg1.setRotationPoint(1.0F, 22.5F, 2.6F);
         this.leg1.rotateAngleX = -1.4764F;
        		 this.leg1.rotateAngleY = -0.3911F;
        		 this.leg1.rotateAngleZ = -0.0361F;
         
         this.leg2.setRotationPoint(-3.0F, 18.28F, -1.48F);
         this.leg2.rotateAngleX = -0.2952F;
        		 this.leg2.rotateAngleY = -0.0306F;
        		 this.leg2.rotateAngleZ = -0.1002F;
         
         this.leg3.setRotationPoint(1.0F, 18.0F, -1.4F);
         this.leg3.rotateAngleX = -0.2752F;
        		 this.leg3.rotateAngleY = 0.0479F;
        		 this.leg3.rotateAngleZ = 0.1504F;
         
         this.tail.setRotationPoint(0.0F, 21.6F, 4.6F);
         this.tail.rotateAngleX = 1.5708F;
	    }
	    else
	    {
	    	this.body.setRotationPoint(0.0F, 16.0F, 0.0F);
			setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
			
			this.head.setRotationPoint(0.0F, 15.3F, -3.0F);
			setRotationAngle(head, 0.0F, 0.0F, 0.0F);
			
			this.leg0.setRotationPoint(-3.0F, 18.0F, 6.0F);
			setRotationAngle(leg0, 0.0F, 0.0F, 0.0F);
			
			this.leg1.setRotationPoint(1.0F, 18.0F, 6.0F);
			setRotationAngle(leg1, 0.0F, 0.0F, 0.0F);
			
			this.leg2.setRotationPoint(-3.0F, 18.0F, -1.0F);
			setRotationAngle(leg2, 0.0F, 0.0F, 0.0F);
			
			this.leg3.setRotationPoint(1.0F, 18.0F, -1.0F);
			setRotationAngle(leg3, 0.0F, 0.0F, 0.0F);
			
			this.tail.setRotationPoint(0.0F, 16.0F, 7.0F);
			setRotationAngle(tail, 1.5708F, 0.0F, 0.0F);
		
			this.leg0.rotateAngleX = MathHelper.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
			this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
			this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
			this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
	    }
	 
	 	this.head.rotateAngleZ = entityfox.getShakeAngle(partialTickTime, 0.0F);
	 	this.body.rotateAngleZ = entityfox.getShakeAngle(partialTickTime, -0.16F);
	 	this.tail.rotateAngleZ = entityfox.getShakeAngle(partialTickTime, -0.2F);
	}
	
}