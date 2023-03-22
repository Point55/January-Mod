package com.point5.januarymod.util.handlers;

import com.point5.januarymod.entity.fox.EntityFox;

import net.minecraft.util.math.BlockPos;

public class FoxLogicHandler {
	
    private EntityFox fox;
    
    public FoxLogicHandler(EntityFox fox) {
        this.fox = fox;
    }

    /*
    logic done exclusively on server.
    */
    public void updateFoxServer() 
    {
    	if (!fox.isInWater() && !fox.isSleeping() && fox.onGround && fox.getAttackTarget() == null && fox.isDaytime() && fox.getRNG().nextInt(250) == 0) {
            fox.setSleeping(true);
        }
        if (fox.isSleeping() && (fox.isInWater() || (fox.world.canBlockSeeSky(new BlockPos(fox)) || !fox.isDaytime()) || fox.getAttackTarget() != null)) {
            fox.setSleeping(false);
        }
        if (!fox.canMove()) {
            if (fox.getAttackTarget() != null) {
                fox.setAttackTarget(null);
            }
            fox.getNavigator().clearPath();
        }
        if(!fox.isTamed()){
            fox.updateCheckPlayer();
        }
        if (fox.getLeashed() && fox.isSleeping()) {
            fox.setSleeping(false);
        }
        if (fox.isSitting()) {
            fox.getNavigator().clearPath();
        }
        if (fox.isSleeping()) {
            fox.getNavigator().clearPath();
        }
        if (fox.isSleeping() && fox.getAttackTarget() != null && fox.getEntityBoundingBox().expand(2.0F, 2.0F, 2.0F).intersects(fox.getAttackTarget().getEntityBoundingBox())) {
            fox.attackEntityAsMob(fox.getAttackTarget());
        }
    }
    
    /*
    logic done exclusively on client.
    */
    public void updateFoxClient() {
    	
    }

    /*
    logic done on server and client on parallel.
    */
    public void updateFoxCommon()
    {
    	  boolean sleeping = fox.isSleeping();
          if (sleeping && fox.sleepProgress < 20.0F) {
              fox.sleepProgress += 0.5F;
          } else if (!sleeping && fox.sleepProgress > 0.0F) {
              fox.sleepProgress -= 0.5F;
          }
    }
}
