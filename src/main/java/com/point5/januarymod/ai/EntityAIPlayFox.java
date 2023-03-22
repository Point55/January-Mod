package com.point5.januarymod.ai;

import java.util.List;

import com.point5.januarymod.entity.fox.EntityFox;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIPlayFox extends EntityAIBase
{
    private final EntityFox fox;
    private EntityLivingBase targetFox;
    private final double speed;
    private int playTime;

    public EntityAIPlayFox(EntityFox entityIn, double speedIn)
    {
        this.fox = entityIn;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        if (this.fox.getRNG().nextInt(400) != 0)
        {
            return false;
        }
        else
        {
            List<EntityFox> list = this.fox.world.<EntityFox>getEntitiesWithinAABB(EntityFox.class, this.fox.getEntityBoundingBox().grow(6.0D, 3.0D, 6.0D));
            double d0 = Double.MAX_VALUE;

            for (EntityFox entityfox : list)
            {
                if (entityfox != this.fox && !entityfox.isPlaying())
                {
                    double d1 = entityfox.getDistanceSq(this.fox);

                    if (d1 <= d0)
                    {
                        d0 = d1;
                        this.targetFox = entityfox;
                    }
                }
            }

            if (this.targetFox == null)
            {
                Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.fox, 16, 3);

                if (vec3d == null)
                {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean shouldContinueExecuting()
    {
        return this.playTime > 0;
    }

    public void startExecuting()
    {
        if (this.targetFox != null)
        {
            this.fox.setPlaying(true);
        }

        this.playTime = 1000;
    }

    public void resetTask()
    {
        this.fox.setPlaying(false);
        this.targetFox = null;
    }

    public void updateTask()
    {
        --this.playTime;

        if (this.targetFox != null)
        {
            if (this.fox.getDistanceSq(this.targetFox) > 4.0D)
            {
                this.fox.getNavigator().tryMoveToEntityLiving(this.targetFox, this.speed);
            }
        }
        else if (this.fox.getNavigator().noPath())
        {
            Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.fox, 16, 3);

            if (vec3d == null)
            {
                return;
            }

            this.fox.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, this.speed);
        }
    }
}
