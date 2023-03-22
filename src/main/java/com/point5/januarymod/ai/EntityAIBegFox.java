package com.point5.januarymod.ai;

import com.point5.januarymod.entity.fox.EntityFox;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIBegFox extends EntityAIBase
{
    private final EntityFox custom;
    private EntityPlayer player;
    private final World world;
    private final float minPlayerDistance;
    private int timeoutCounter;

    public EntityAIBegFox(EntityFox custom, float minDistance)
    {
        this.custom = custom;
        this.world = custom.world;
        this.minPlayerDistance = minDistance;
        this.setMutexBits(2);
    }

    public boolean shouldExecute()
    {
        this.player = this.world.getClosestPlayerToEntity(this.custom, (double)this.minPlayerDistance);
        return this.player == null ? false : this.hasTemptationItemInHand(this.player);
    }

    public boolean shouldContinueExecuting()
    {
        if (!this.player.isEntityAlive())
        {
            return false;
        }
        else if (this.custom.getDistanceSq(this.player) > (double)(this.minPlayerDistance * this.minPlayerDistance))
        {
            return false;
        }
        else
        {
            return this.timeoutCounter > 0 && this.hasTemptationItemInHand(this.player);
        }
    }

    public void startExecuting()
    {
        this.custom.setBegging(true);
        this.timeoutCounter = 40 + this.custom.getRNG().nextInt(40);
    }

    public void resetTask()
    {
        this.custom.setBegging(false);
        this.player = null;
    }

    public void updateTask()
    {
        this.custom.getLookHelper().setLookPosition(this.player.posX, this.player.posY + (double)this.player.getEyeHeight(), this.player.posZ, 10.0F, (float)this.custom.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }

    private boolean hasTemptationItemInHand(EntityPlayer player)
    {
        for (EnumHand enumhand : EnumHand.values())
        {
            ItemStack itemstack = player.getHeldItem(enumhand);

            if (this.custom.isTamed() && itemstack.getItem() == Items.BONE)
            {
                return true;
            }

            if (this.custom.isBreedingItem(itemstack))
            {
                return true;
            }
        }

        return false;
    }
}
