package com.point5.januarymod.ai;

import com.point5.januarymod.entity.EntityCustom;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;	

public class EntityAIFollow extends EntityAIBase
{
    private final EntityCustom thePet;
    private EntityLivingBase theOwner;
    World world;
    private final double followSpeed;
    private final PathNavigate petPathfinder;
    private int timeToRecalcPath;
    float maxDist;
    float minDist;
    private float oldWaterCost;
    private int state;
    
    public EntityAIFollow(final EntityCustom thePetIn, final double followSpeedIn, final float minDistIn, final float maxDistIn, final int state) {
        this.thePet = thePetIn;
        this.world = thePetIn.world;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = thePetIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.state = state;
        this.setMutexBits(3);
        if (!(thePetIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }
    
    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.thePet.getOwner();
        if (entitylivingbase == null) {
            return false;
        }
        if (this.thePet.getMovement() != this.state) {
            return false;
        }
        if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).isSpectator()) {
            return false;
        }
        if (this.thePet.getDistanceSq((Entity)entitylivingbase) < this.minDist * this.minDist) {
            return false;
        }
        this.theOwner = entitylivingbase;
        return true;
    }
    
    public boolean continueExecuting() {
        return !this.petPathfinder.noPath() && this.thePet.getDistanceSq((Entity)this.theOwner) > this.maxDist * this.maxDist && !this.thePet.isSitting();
    }
    
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.thePet.getPathPriority(PathNodeType.WATER);
        this.thePet.setPathPriority(PathNodeType.WATER, 0.0f);
    }
    
    public void resetTask() {
        this.theOwner = null;
        this.petPathfinder.clearPath();
        this.thePet.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }
    
    private boolean isEmptyBlock(final BlockPos pos) {
        final IBlockState iblockstate = this.world.getBlockState(pos);
        return iblockstate.getMaterial() == Material.AIR || !iblockstate.isFullCube();
    }
    
    public void updateTask() {
        this.thePet.getLookHelper().setLookPositionWithEntity((Entity)this.theOwner, 10.0f, (float)this.thePet.getVerticalFaceSpeed());
        final int timeToRecalcPath = this.timeToRecalcPath - 1;
        this.timeToRecalcPath = timeToRecalcPath;
        if (timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.petPathfinder.tryMoveToEntityLiving((Entity)this.theOwner, this.followSpeed) && !this.thePet.getLeashed() && this.thePet.getDistanceSq((Entity)this.theOwner) >= 144.0) {
                final int i = MathHelper.floor(this.theOwner.posX) - 2;
                final int j = MathHelper.floor(this.theOwner.posZ) - 2;
                final int k = MathHelper.floor(this.theOwner.getEntityBoundingBox().minY);
                for (int l = 0; l <= 4; ++l) {
                    for (int i2 = 0; i2 <= 4; ++i2) {
                        if ((l < 1 || i2 < 1 || l > 3 || i2 > 3) && this.world.getBlockState(new BlockPos(i + l, k - 1, j + i2)).isOpaqueCube() && this.isEmptyBlock(new BlockPos(i + l, k, j + i2)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i2))) {
                            this.thePet.setLocationAndAngles((double)(i + l + 0.5f), (double)k, (double)(j + i2 + 0.5f), this.thePet.rotationYaw, this.thePet.rotationPitch);
                            this.petPathfinder.clearPath();
                            return;
                        }
                    }
                }
            }
        }
    }
}
