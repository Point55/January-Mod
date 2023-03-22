package com.point5.januarymod.events;

import com.point5.januarymod.JanuaryMod;
import com.point5.januarymod.entity.fox.EntityFox;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = JanuaryMod.MODID)
public class EntityEventHandler
{

	@SubscribeEvent
	public static void onEntityTakeDamage(LivingHurtEvent event)
	{
		float amount = event.getAmount();
		EntityLivingBase entity = event.getEntityLiving();
		DamageSource source = event.getSource();

		if (entity instanceof EntityAnimal)
		{
			EntityAnimal animal = (EntityAnimal) entity;

			if (source == DamageSource.FALL)
				if (animal.getLeashed())
				{
					event.setAmount(amount * 0.2f);
					animal.fallDistance = 0;
				}

		}

	}

	@SubscribeEvent
	public static void onEntityHit(LivingAttackEvent event)
	{
		float amount = event.getAmount();
		EntityLivingBase entity = event.getEntityLiving();
		DamageSource source = event.getSource();

		if (entity instanceof EntityLivingBase)
		{
			EntityLivingBase animal = entity;

			if (animal.isRiding())
				event.setCanceled(true);
		}

		if (entity instanceof EntityFox)
		{
			EntityFox isleeping = (EntityFox) entity;
			if (isleeping.isSleeping())
			{
				if (source == DamageSource.STARVE)
				{
					event.setCanceled(true);
				}

				((EntityFox) entity).setSleeping(false);
			}
		}

		if (entity instanceof EntityTameable)
		{
			if (((EntityTameable) entity).isSitting())
				((EntityTameable) entity).setSitting(false);
		}

	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();

		if (entity instanceof EntityFox && entity instanceof EntityAnimal)
		{
			EntityFox isleeping = (EntityFox) entity;
			if (isleeping.isSleeping() && ((EntityAnimal) entity).getLeashed())
				isleeping.setSleeping(false);
		}
	}

}