//package com.point5.januarymod.ai;
//
//import java.util.Random;
//
//import javax.annotation.Nullable;
//
//import com.point5.januarymod.entity.fox.EntityFox;
//
//import net.minecraft.entity.ai.EntityAIBase;
//import net.minecraft.pathfinding.PathNavigate;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//
//	public class EntityAISleep extends EntityAIBase {
//
//		private final EntityFox entity;
//		private int delay;
//		protected World world;
//		
//	    private double shelterX;
//	    private double shelterY;
//	    private double shelterZ;
//	    private final PathNavigate navigation;
//	    private final double movementSpeed;
//	    public EntityAISleep(EntityFox entity, double movementSpeedIn)
//		{
//			this.entity = entity;
//			this.world = entity.world;
//			this.movementSpeed = movementSpeedIn;
//			this.setMutexBits(3);
//			this.delay = 0;
//			this.navigation = entity.getNavigator();
//		}
//
//		@Override
//		public boolean shouldExecute()
//		{
//			if (this.entity.isSitting())
//			{
//				return false;
//			}
//			else if (this.entity.getLeashed())
//			{
//				return false;
//			}
//			else if (this.world.getWorldTime() >= 13000)
//			{
//				return false;
//			}
//			else if (++this.delay <= 100 + entity.getRNG().nextInt(100))
//			{
//				return false;
//			}
//		    else if (!this.entity.onGround)
//		    {
//		        return false;
//		    }
//			else if (this.entity.isInWater())
//			{
//				return false;
//			}
//			else
//			{
//		       Vec3d vec3d = this.findPossibleShelter();
//
//	            if (vec3d == null)
//	            {
//	                return false;
//	            }
//	            else
//	            {
//	                this.shelterX = vec3d.x;
//	                this.shelterY = vec3d.y;
//	                this.shelterZ = vec3d.z;
//	                return true;
//	            }
//			}
//		}
//		
//		@Override
//	    public void startExecuting()
//	    {
//			if (this.world.canSeeSky(new BlockPos(this.entity.posX, this.entity.getEntityBoundingBox().minY, this.entity.posZ)))
//			{
//				this.entity.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
//			}
//			else
//			{
//		        this.entity.setSleeping(true);
//			}
//	    }
//
//		@Override
//		public void updateTask()
//		{
//			super.updateTask();
//		}
//
//		@Override
//		public boolean shouldContinueExecuting()
//		{
//			return !this.navigation.noPath();
//		}
//
//		@Override
//		public void resetTask()
//		{
//			if (this.world.getWorldTime() >= 13000)
//			{
//				this.entity.setSleeping(false);
//				this.delay = 0;
//			}
//			else if (this.entity.getLeashed())
//			{
//				this.entity.setSleeping(false);
//				this.delay = 0;
//			}
//			else if (this.entity.isSitting())
//			{
//				this.entity.setSleeping(false);
//				this.delay = 0;
//			}
//		    else if (!this.entity.onGround)
//		    {
//				this.entity.setSleeping(false);
//				this.delay = 0;
//		    }
//			else if (this.entity.isInWater())
//			{
//				this.entity.setSleeping(false);
//				this.delay = 0;
//			}
//			
//		}
//		
//		  @Nullable
//		    private Vec3d findPossibleShelter()
//		    {
//		        Random random = this.entity.getRNG();
//		        BlockPos blockpos = new BlockPos(this.entity.posX, this.entity.getEntityBoundingBox().minY, this.entity.posZ);
//
//		        for (int i = 0; i < 10; ++i)
//		        {
//		            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
//
//		            if (!this.world.canSeeSky(blockpos1) && this.entity.getBlockPathWeight(blockpos1) < 0.0F)
//		            {
//		                return new Vec3d((double)blockpos1.getX(), (double)blockpos1.getY(), (double)blockpos1.getZ());
//		            }
//		        }
//
//		        return null;
//		    }
//
//	}
