package com.point5.januarymod.proxy;

import com.point5.januarymod.JanuaryMod;
import com.point5.januarymod.entity.fox.EntityFox;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber (modid = JanuaryMod.MODID, value = Side.SERVER)
public class ServerProxy extends CommonProxy
{
    public void doParticlesSimple(String type, EntityFox fox) { }
    public void doParticleEffect(String type, EntityFox fox, double xpos, double ypos, double zpos, double xvel, double yvel, double zvel) { }
}
