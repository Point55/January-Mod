package com.point5.januarymod.entity.reindeer;

import com.point5.januarymod.JanuaryMod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@SideOnly(Side.CLIENT)
public class ModelReindeer extends AnimatedGeoModel<EntityReindeer>
{
	
	@Override
	public ResourceLocation getModelLocation(EntityReindeer entity)
	{
		if (((EntityLivingBase) entity).isChild()) {
            return new ResourceLocation(JanuaryMod.MODID, "geo/babyreindeer.geo.json");
        }
		return new ResourceLocation(JanuaryMod.MODID, "geo/modelreindeer.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureLocation(EntityReindeer entity)
	{
		if (((EntityLivingBase) entity).isChild()) {
            return new ResourceLocation(JanuaryMod.MODID, "textures/model/entity/babyreindeer" + entity.getVariant() + ".png");
        }
		return new ResourceLocation(JanuaryMod.MODID, "textures/model/entity/reindeer" + entity.getVariant() + ".png");
	}
	
	
	@Override
	public ResourceLocation getAnimationFileLocation(EntityReindeer animatable)
	{
		return new ResourceLocation(JanuaryMod.MODID, "animations/modelreindeer.json");
	}
	
	public void setLivingAnimations(EntityReindeer entity, Integer uniqueID, AnimationEvent customPredicate)
	{
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("head");

		EntityLivingBase entityIn = (EntityLivingBase) entity;
		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		head.setRotationX(extraData.headPitch * ((float)Math.PI / 180F));
		head.setRotationY(extraData.netHeadYaw * ((float)Math.PI / 180F));
	}

}
