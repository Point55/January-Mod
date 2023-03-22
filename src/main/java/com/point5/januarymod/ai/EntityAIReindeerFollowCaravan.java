package com.point5.januarymod.ai;

import java.util.List;

import com.point5.januarymod.entity.reindeer.EntityReindeer;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIReindeerFollowCaravan extends EntityAIBase
{
    public EntityReindeer reindeer;
    private double speedModifier;
    private int distCheckCounter;

    public EntityAIReindeerFollowCaravan(EntityReindeer reindeerIn, double speedModifierIn)
    {
        this.reindeer = reindeerIn;
        this.speedModifier = speedModifierIn;
        this.setMutexBits(2);
    }

    public boolean shouldExecute()
    {
        if (!this.reindeer.getLeashed() && !this.reindeer.inCaravan())
        {
            List<EntityReindeer> list = this.reindeer.world.<EntityReindeer>getEntitiesWithinAABB(this.reindeer.getClass(), this.reindeer.getEntityBoundingBox().grow(9.0D, 4.0D, 9.0D));
            EntityReindeer entityreindeer = null;
            double d0 = Double.MAX_VALUE;

            for (EntityReindeer entityreindeer1 : list)
            {
                if (entityreindeer1.inCaravan() && !entityreindeer1.hasCaravanTrail())
                {
                    double d1 = this.reindeer.getDistanceSq(entityreindeer1);

                    if (d1 <= d0)
                    {
                        d0 = d1;
                        entityreindeer = entityreindeer1;
                    }
                }
            }

            if (entityreindeer == null)
            {
                for (EntityReindeer entityreindeer2 : list)
                {
                    if (entityreindeer2.getLeashed() && !entityreindeer2.hasCaravanTrail())
                    {
                        double d2 = this.reindeer.getDistanceSq(entityreindeer2);

                        if (d2 <= d0)
                        {
                            d0 = d2;
                            entityreindeer = entityreindeer2;
                        }
                    }
                }
            }

            if (entityreindeer == null)
            {
                return false;
            }
            else if (d0 < 4.0D)
            {
                return false;
            }
            else if (!entityreindeer.getLeashed() && !this.firstIsLeashed(entityreindeer, 1))
            {
                return false;
            }
            else
            {
                this.reindeer.joinCaravan(entityreindeer);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean shouldContinueExecuting()
    {
        if (this.reindeer.inCaravan() && this.reindeer.getCaravanHead().isEntityAlive() && this.firstIsLeashed(this.reindeer, 0))
        {
            double d0 = this.reindeer.getDistanceSq(this.reindeer.getCaravanHead());

            if (d0 > 676.0D)
            {
                if (this.speedModifier <= 3.0D)
                {
                    this.speedModifier *= 1.2D;
                    this.distCheckCounter = 40;
                    return true;
                }

                if (this.distCheckCounter == 0)
                {
                    return false;
                }
            }

            if (this.distCheckCounter > 0)
            {
                --this.distCheckCounter;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void resetTask()
    {
        this.reindeer.leaveCaravan();
        this.speedModifier = 2.1D;
    }

    public void updateTask()
    {
        if (this.reindeer.inCaravan())
        {
            EntityReindeer entityreindeer = this.reindeer.getCaravanHead();
            double d0 = (double)this.reindeer.getDistance(entityreindeer);
            float f = 2.0F;
            Vec3d vec3d = (new Vec3d(entityreindeer.posX - this.reindeer.posX, entityreindeer.posY - this.reindeer.posY, entityreindeer.posZ - this.reindeer.posZ)).normalize().scale(Math.max(d0 - 2.0D, 0.0D));
            this.reindeer.getNavigator().tryMoveToXYZ(this.reindeer.posX + vec3d.x, this.reindeer.posY + vec3d.y, this.reindeer.posZ + vec3d.z, this.speedModifier);
        }
    }

    private boolean firstIsLeashed(EntityReindeer p_190858_1_, int p_190858_2_)
    {
        if (p_190858_2_ > 8)
        {
            return false;
        }
        else if (p_190858_1_.inCaravan())
        {
            if (p_190858_1_.getCaravanHead().getLeashed())
            {
                return true;
            }
            else
            {
                EntityReindeer entityreindeer = p_190858_1_.getCaravanHead();
                ++p_190858_2_;
                return this.firstIsLeashed(entityreindeer, p_190858_2_);
            }
        }
        else
        {
            return false;
        }
    }
}
