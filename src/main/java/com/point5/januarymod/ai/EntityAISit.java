package com.point5.januarymod.ai;

import com.point5.januarymod.entity.EntityCustom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAISit extends EntityAIBase
{
    private final EntityCustom theEntity;
    private final int state;
    
    public EntityAISit(final EntityCustom entityIn, final int state) {
        this.theEntity = entityIn;
        this.state = state;
        this.setMutexBits(2);
    }
    
    public boolean shouldExecute() {
        if (!this.theEntity.isTamed()) {
            return false;
        }
        if (this.theEntity.isInWater()) {
            return false;
        }
        if (!this.theEntity.onGround) {
            return false;
        }
        final EntityLivingBase entitylivingbase = this.theEntity.getOwner();
        return entitylivingbase == null || ((this.theEntity.getDistanceSq((Entity)entitylivingbase) >= 144.0 || entitylivingbase.getRevengeTarget() == null) && this.theEntity.getMovement().equals(this.state));
    }
    
    public void startExecuting() {
        this.theEntity.getNavigator().clearPath();
        this.theEntity.setMovement(this.state);
    }
}
