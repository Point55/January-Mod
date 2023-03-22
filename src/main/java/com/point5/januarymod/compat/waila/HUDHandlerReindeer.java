package com.point5.januarymod.compat.waila;

import java.util.List;

import javax.annotation.Nonnull;

import com.point5.januarymod.entity.fox.EntityFox;
import com.point5.januarymod.entity.reindeer.EntityReindeer;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class HUDHandlerReindeer implements IWailaEntityProvider {
    public static int nhearts = 20;
    public static float maxhpfortext = 40.0f;
    public static IWailaEntityProvider INSTANCE = new HUDHandlerReindeer();

    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        currenttip.add(String.format(EntityFox.getNameForGender(0)));
        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
        if (ent instanceof EntityReindeer)
            ent.writeToNBT(tag);
        return tag;
    }
}
