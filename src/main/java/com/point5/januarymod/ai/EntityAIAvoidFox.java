package com.point5.januarymod.ai;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.point5.januarymod.entity.fox.EntityFox;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

public class EntityAIAvoidFox <T extends Entity> extends EntityAIBase
{
    private final Predicate<Entity> canBeSeenSelector;
    protected EntityFox entity;
    private final double farSpeed;
    private final double nearSpeed;
    protected T closestLivingEntity;
    private final float avoidDistance;
    protected EntityPlayer player;
    private Path path;
    private final PathNavigate navigation;
    private final Class<T> classToAvoid;
    private final Predicate <? super T > avoidTargetSelector;

    public EntityAIAvoidFox(EntityFox entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn)
    {
        this(entityIn, classToAvoidIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public EntityAIAvoidFox(EntityFox entityIn, Class<T> classToAvoidIn, Predicate <? super T > avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn)
    {
        this.canBeSeenSelector = new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_.isEntityAlive() && EntityAIAvoidFox.this.entity.getEntitySenses().canSee(p_apply_1_) && !EntityAIAvoidFox.this.entity.isOnSameTeam(p_apply_1_);
            }
        };
        this.entity = entityIn;
        this.classToAvoid = classToAvoidIn;
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.navigation = entityIn.getNavigator();
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
        List<T> list = this.entity.world.<T>getEntitiesWithinAABB(this.classToAvoid, this.entity.getEntityBoundingBox().grow((double)this.avoidDistance, 2.0D, (double)this.avoidDistance), Predicates.and(EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector));

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            this.closestLivingEntity = list.get(0);
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

            if (vec3d == null)
            {
                return false;
            }
            else if (this.closestLivingEntity.isSneaking())
            {
                return false;
            }
            else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.entity))
            {
                return false;
            }
            else
            {
                this.path = this.navigation.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                return this.path != null;
            }
        }
    }

    public boolean shouldContinueExecuting()
    {
        return !this.navigation.noPath();
    }

    public void startExecuting()
    {
        this.navigation.setPath(this.path, this.farSpeed);
//        this.entity.setSleeping(false);
    }

    public void resetTask()
    {
        this.closestLivingEntity = null;
    }

    public void updateTask()
    {
        if (this.entity.getDistanceSq(this.closestLivingEntity) < 49.0D)
        {
            this.entity.getNavigator().setSpeed(this.nearSpeed);
        }
        else
        {
            this.entity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}
