package com.point5.januarymod.ai;

import javax.annotation.Nullable;

import com.point5.januarymod.entity.EntityCustom;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAIWander extends EntityAIBase
{
    protected final EntityCustom entity;
    protected double xPosition;
    protected double yPosition;
    protected double zPosition;
    protected final double speed;
    protected int executionChance;
    protected int state;
    protected boolean mustUpdate;
    
    public EntityAIWander(final EntityCustom creatureIn, final double speedIn) {
        this(creatureIn, speedIn, 120, 0);
    }
    
    public EntityAIWander(final EntityCustom creatureIn, final double speedIn, final int chance, final int state) {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.state = state;
        this.setMutexBits(8);
    }
    
    public boolean shouldExecute() {
        if (!this.mustUpdate) {
            if (this.entity.getMovement() != this.state) {
                return false;
            }
            if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
                return false;
            }
        }
        final Vec3d vec3d = this.getPosition();
        if (vec3d == null) {
            return false;
        }
        this.xPosition = vec3d.x;
        this.yPosition = vec3d.y;
        this.zPosition = vec3d.z;
        this.mustUpdate = false;
        return true;
    }
    
    @Nullable
    protected Vec3d getPosition() {
        return RandomPositionGenerator.findRandomTarget((EntityCreature)this.entity, 10, 7);
    }
    
    public boolean continueExecuting() {
        return !this.entity.getNavigator().noPath() && this.entity.getMovement() == 0;
    }
    
    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
    
    public void makeUpdate() {
        this.mustUpdate = true;
    }
    
    public void setExecutionChance(final int newchance) {
        this.executionChance = newchance;
    }
}
