package com.point5.januarymod.entity.reindeer;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.jline.utils.Log;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import com.point5.januarymod.ai.EntityAIBegReindeer;
import com.point5.januarymod.ai.EntityAIFollow;
import com.point5.januarymod.ai.EntityAIReindeerFollowCaravan;
import com.point5.januarymod.ai.EntityAISit;
import com.point5.januarymod.ai.EntityAIWander;
import com.point5.januarymod.entity.EntityCustom;
import com.point5.januarymod.util.handlers.SoundHandler;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityReindeer extends EntityCustom implements IAnimatable, IJumpingMount
{
	private AnimationFactory factory = new AnimationFactory(this);

	private int deerTimer;
	public EntityAIEatGrass entityAIEatGrass;
	
	private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.<Float>createKey(EntityReindeer.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> BEGGING = EntityDataManager.<Boolean>createKey(EntityReindeer.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityReindeer.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> GENDER = EntityDataManager.<Integer>createKey(EntityReindeer.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IS_STANDING = EntityDataManager.<Boolean>createKey(EntityReindeer.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> WANDERING = EntityDataManager.createKey(EntityReindeer.class, DataSerializers.BOOLEAN);
    
    protected static final IAttribute JUMP_STRENGTH = (new RangedAttribute((IAttribute)null, "reindeer.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
    
    private static final Set<Item> TAME_ITEMS = Sets.newHashSet(Items.CARROT, Items.APPLE, Items.BREAD, Items.WHEAT);
    
    protected boolean reindeerJumping;
    protected float jumpPower;
    private EntityAISit aiSit;
    
    @Nullable
    private EntityReindeer caravanHead;
    @Nullable
    private EntityReindeer caravanTail;
    
//Animations	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		if (event.isMoving() && !this.isBeingRidden()){
		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reindeer.walk", true));
		return PlayState.CONTINUE;
		} 
		if (this.isBeingRidden() && event.isMoving() && !this.isReindeerJumping()){
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reindeer.trot", true));
			return PlayState.CONTINUE;
		}
		if (this.deerTimer > 4 && this.deerTimer <= 40 && !event.isMoving()){
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reindeer.grass", true));
			return PlayState.CONTINUE;
		}
		if (!event.isMoving()  && !this.isBegging()){
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reindeer.idle", true));
			return PlayState.CONTINUE;
		}
		if (this.isReindeerJumping() && this.isBeingRidden()){
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reindeer.jump", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private <E extends IAnimatable> PlayState attack(AnimationEvent<E> event)
	{
        if (this.isStanding() == true)
        {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reindeer.attack", false));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	
	private <E extends IAnimatable> PlayState begging(AnimationEvent<E> event)
	{
		if (this.isBegging() && this.isTamed()){
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reindeer.beg", true));
			return PlayState.CONTINUE;
    	}
		return PlayState.STOP;
	}
	
	private <E extends IAnimatable> PlayState child(AnimationEvent<E> event)
	{
        if (this.isChild() && this.deerTimer > 4 && this.deerTimer <= 40 && !event.isMoving()){
    			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reindeer.babyeat", true));
    			return PlayState.CONTINUE;
    		}
        
        if (this.isChild() && event.isMoving()){
    			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.reindeer.babywalk", true));
    			return PlayState.CONTINUE;
    		}
		return PlayState.STOP;
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		AnimationController controller = new AnimationController(this, "begging", 200, this::begging);
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
		data.addAnimationController(new AnimationController(this, "child", 0, this::child));
		data.addAnimationController(new AnimationController(this, "attack", 500, this::attack));
		data.addAnimationController(new AnimationController(this, "begging", 500, this::begging));
		controller.registerSoundListener(this::soundListener);
		data.addAnimationController(controller);
	}
	
	private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (player != null) {
		player.playSound(SoundHandler.REINDEER_BEGGING, 1, 1);
		}
	}
		
	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}

	
//Spawn and Basics
	public EntityReindeer(World worldIn)
	{
		super(worldIn);
		this.setSize(1.4F, 0.9F);
	}
	
	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(WANDERING, Boolean.valueOf(true));
        this.dataManager.register(GENDER, Integer.valueOf(0));
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
        this.dataManager.register(BEGGING, Boolean.valueOf(false));
        this.dataManager.register(IS_STANDING, Boolean.valueOf(false));
    }  
	
	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
	    this.setGender(this.rand.nextInt(2));
		this.setVariant(getRNG().nextInt(setReindeerVariants()));
		this.setTamed(false);
	    return super.onInitialSpawn(difficulty, livingdata);
	}	
	
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
		 this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
		this.getAttributeMap().registerAttribute(JUMP_STRENGTH);
	    this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
	}

	public int getGender()
	{
	        return MathHelper.clamp(((Integer)this.dataManager.get(GENDER)).intValue(), 1, 2);
	}

	public void setGender(int variantIn)
	{
	        this.dataManager.set(GENDER, Integer.valueOf(variantIn));
	}
	
	public static String getNameForGender(int gender)
	{
		if (gender == 1) {
			return "male";
		}
		else {
			return "female";
		}
	}
	
	public int getVariant()
	{
	        return this.dataManager.get(VARIANT);
	}

	public void setVariant(int variantIn)
	{
	        this.dataManager.set(VARIANT, Integer.valueOf(variantIn));
	}
	 
	protected PathNavigate createNavigator(World worldIn)
	{
	        PathNavigateGround pathnavigateground = new PathNavigateGround(this, worldIn);
	        return pathnavigateground;
	}
	
	public int setReindeerVariants() {
		return 15;
	}
	
	public void onUpdate()
    {
        super.onUpdate();
    }
	 
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
	        
		if (this.world.isRemote){
	            this.deerTimer = Math.max(0, this.deerTimer - 1);
		}
	}  
 
	public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Wandering", this.isWandering());
        compound.setInteger("Gender", this.getGender());
        compound.setInteger("Variant", this.getVariant());
        compound.setBoolean("TamedReindeer", this.isTamed());
        if (this.getCustomNameTag() != null && !this.getCustomNameTag().isEmpty()) {
            compound.setString("CustomName", this.getCustomNameTag());
        }
    }

	public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setWandering(compound.getBoolean("Wandering"));
        this.setGender(compound.getInteger("Gender"));
        this.setVariant(compound.getInteger("Variant"));
        this.setTamed(compound.getBoolean("TamedReindeer"));
        if (!compound.getString("CustomName").isEmpty()) {
            this.setCustomNameTag(compound.getString("CustomName"));
        }
    }
	
	public void setWandering(boolean value) {
	        this.dataManager.set(WANDERING, Boolean.valueOf(value));
	}

	public boolean isWandering() {
	        return this.dataManager.get(WANDERING);
	}
    

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 10){
		this.deerTimer = 40;
		}
		else {
			super.handleStatusUpdate(id);
		}
	}
	
    public int getMaxSpawnedInChunk()
    {
        return 5;
    }
    
	
//AI	
	@Override
	protected void initEntityAI()
    {
		clearAITasks();
		this.aiSit = new EntityAISit((EntityCustom)this, 1);
        this.entityAIEatGrass = new EntityAIEatGrass(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, (EntityAIBase)new EntityAIFollow((EntityCustom)this, 1.5, 2.0f, 15.0f, 2));
        this.tasks.addTask(4, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(5, new EntityAITempt(this, 1.1D, Items.APPLE, false));
        this.tasks.addTask(6, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(7, this.entityAIEatGrass);
        this.tasks.addTask(8, (EntityAIBase)new EntityAIWander((EntityCustom)this, 1.0));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(11, new EntityAIBegReindeer(this, 8.0F));
        this.tasks.addTask(12, new EntityAIReindeerFollowCaravan(this, 1.5D));
        this.tasks.addTask(13, new AIMeleeAttack());
        this.tasks.addTask(14, new AIPanic());
        this.targetTasks.addTask(1, new AIHurtByTarget());
        this.targetTasks.addTask(2, new AIAttackPlayer());
    }
	
	@Override
	protected void updateAITasks()
    	{
        this.deerTimer = this.entityAIEatGrass.getEatingGrassTimer();
        this.dataManager.set(DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
        super.updateAITasks();
    	}
	
	protected void clearAITasks()
		{
		tasks.taskEntries.clear();
		targetTasks.taskEntries.clear();
		}

	 
//Attack

@Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

        if (flag)
        {
            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

public boolean isStanding()
{
	return ((Boolean)this.dataManager.get(IS_STANDING)).booleanValue();
}

public void setStanding(boolean standing)
{
	if (!this.isTamed()) {
    this.dataManager.set(IS_STANDING, Boolean.valueOf(standing));
	}
}

class AIAttackPlayer extends EntityAINearestAttackableTarget<EntityPlayer>
{
    public AIAttackPlayer()
    {
        super(EntityReindeer.this, EntityPlayer.class, 20, true, true, (Predicate)null);
    }

    public boolean shouldExecute()
    {
        if (EntityReindeer.this.isChild() && EntityReindeer.this.getGender() != 1 && EntityReindeer.this.isTamed())
        {
            return false;
        }
        else
        {
            if (super.shouldExecute())
            {
                for (EntityReindeer entityreindeer : EntityReindeer.this.world.getEntitiesWithinAABB(EntityReindeer.class, EntityReindeer.this.getEntityBoundingBox().grow(5.0D, 2.0D, 5.0D)))
                {
                    if (entityreindeer.isChild())
                    {
                        return true;
                    }
                }
            }

            EntityReindeer.this.setAttackTarget((EntityLivingBase)null);
            return false;
        }
    }
    

	protected double getTargetDistance()
    {
        return super.getTargetDistance() * 0.5D;
    }
}

class AIHurtByTarget extends EntityAIHurtByTarget
{
    public AIHurtByTarget()
    {
        super(EntityReindeer.this, false);
    }

    public void startExecuting()
    {
        super.startExecuting();
        if (EntityReindeer.this.isChild())
        {
            this.alertOthers();
            this.resetTask();
        }
    }

    protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
    {
        if (creatureIn instanceof EntityReindeer && !creatureIn.isChild() && EntityReindeer.this.getGender() == 1 && !EntityReindeer.this.isTamed())
        {
            super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
        }
    }
}

class AIMeleeAttack extends EntityAIAttackMelee
{
    public AIMeleeAttack()
    {
        super(EntityReindeer.this, 1.25D, false);
    }

    protected void checkAndPerformAttack(EntityLivingBase enemy, double distToEnemySqr)
    {
        double d0 = this.getAttackReachSqr(enemy);

        if (distToEnemySqr <= d0 && this.attackTick <= 0)
        {
            this.attackTick = 20; //default 20
            this.attacker.attackEntityAsMob(enemy);
            EntityReindeer.this.setStanding(false);
        }
        else if (distToEnemySqr <= d0 * 2.0D)
        {
            if (this.attackTick <= 0)
            {
                EntityReindeer.this.setStanding(false);
                this.attackTick = 20; //default 20
            }

            if (this.attackTick <= 10) //default 10
            {
                EntityReindeer.this.setStanding(true);
            }
        }
        else
        {
            this.attackTick = 20; //default 20
            EntityReindeer.this.setStanding(false);
        }
    }

    public void resetTask()
    {
        EntityReindeer.this.setStanding(false);
        super.resetTask();
    }

    protected double getAttackReachSqr(EntityLivingBase attackTarget)
    {
        return (double)(4.0F + attackTarget.width);
    }
}

class AIPanic extends EntityAIPanic
{
    public AIPanic()
    {
        super(EntityReindeer.this, 2.0D);
    }

    public boolean shouldExecute()
    {
        return !EntityReindeer.this.isChild() && !EntityReindeer.this.isBurning() ? false : super.shouldExecute();
    }
}


//Interaction with Player	 
	 public boolean processInteract(EntityPlayer player, EnumHand hand) 
		{
			ItemStack itemstack = player.getHeldItem(hand);
			
			if (this.isTamed() && this.isOwner(player))
			{
				if (!itemstack.isEmpty())
				{
					if (itemstack.getItem() instanceof ItemFood)
			         {
						ItemFood itemfood = (ItemFood)itemstack.getItem();
			
			            if (TAME_ITEMS.contains(itemstack.getItem()) && ((Float)this.dataManager.get(DATA_HEALTH_ID)).floatValue() < 20.0F)
			            {
			            	if (!player.capabilities.isCreativeMode)
		                    {
		                    	itemstack.shrink(1);
		                    }
			            	this.playSound(SoundEvents.ENTITY_HORSE_EAT, 0.4F, 1.0F);
		                    this.heal((float)itemfood.getHealAmount(itemstack));
		                    return true;
		                } 
			         }
				     else if (itemstack.getItem() == Items.BOOK)
				     {
				            if (this.getGender() == 1) 
				            {
				                Minecraft mc = Minecraft.getMinecraft();
				                if (this.world.isRemote) 
				                {
				                    mc.player.sendMessage(new TextComponentTranslation("male"));
				                }
				            }
				            else 
				            {
				                Minecraft mc = Minecraft.getMinecraft();
				                if (this.world.isRemote) 
				                {
				                    mc.player.sendMessage(new TextComponentTranslation("female"));
				                }
				            }
				            return true;
				     }
				     else if (this.isOwner(player) && player.isSneaking() && !this.world.isRemote && itemstack.getItem() == Items.STICK) {
			               
				    	 if (hand == EnumHand.MAIN_HAND) {
			                    if (this.getMovement() < 2) {
			                        this.setMovement(this.getMovement() + 1);
			                    }
			                    else {
			                        this.setMovement(0);
			                    }
				    	 	}
                         	String commandText = "wander";
                         	if (this.getMovement() == 1) {
                         		commandText = "sit";
                         	}
                         	if (this.getMovement() == 2) {
                         		commandText = "follow";
                         	}
                         	player.sendStatusMessage(new TextComponentTranslation(commandText), true);
                         	return true;
			          }
				} 
				else if (!player.isSneaking() && (!this.isChild()))
				{	
					player.startRiding(this);
					int x = (int) this.posX;
					int y = (int) this.posY;
					int z = (int) this.posZ;
					return true;
				}
			}
			else if (TAME_ITEMS.contains(itemstack.getItem()))
		    {
		            if (!player.capabilities.isCreativeMode)
		            {
		                itemstack.shrink(1);
		            }
		            this.playSound(SoundEvents.ENTITY_HORSE_EAT, 0.4F, 1.0F);
		            if (!this.world.isRemote)
		            {
		                if (this.rand.nextInt(10) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player))
		                {
		                	 this.setTamedBy(player);
		                     this.navigator.clearPath();
		                     this.setAttackTarget((EntityLivingBase)null);
		                     this.setHealth(20.0F);
		                     this.playTameEffect(true);
		                     this.world.setEntityState(this, (byte)7);
		                }
		                else
		                {
		                    this.playTameEffect(false);
		                    this.world.setEntityState(this, (byte)6);
		                }
		            }
		            return true;
		     } 
		     return super.processInteract(player, hand);
		}
	 
	 
//Breeding	 
	  
	 public boolean isBreedingItem(ItemStack stack)
	 {
		        return TAME_ITEMS.contains(stack.getItem());
	 }
	  
	 public boolean canMateWith(EntityAnimal otherAnimal)
	 {
	        if (otherAnimal == this)
	        {
	            return false;
	        }
	        else if (!this.isTamed())
	        {
	            return false;
	        }
	        else if (!(otherAnimal instanceof EntityReindeer))
	        {
	            return false;
	        }
	        else
	        {
	            EntityReindeer entityreindeer = (EntityReindeer)otherAnimal;
	            if (this.getGender() == entityreindeer.getGender()) {
	                return false;
	            }
	            else if (!entityreindeer.isTamed())
	            {
	                return false;
	            }
		        else if (entityreindeer.isSitting())
	            {
	                return false;
	            }
	            else {
	            return this.isInLove() && entityreindeer.isInLove();
	            }
	        }
	 }
	 
	 @Override
		public EntityAgeable createChild(EntityAgeable ageable) 
	 {
		 EntityReindeer reindeer = new EntityReindeer(this.world);
			UUID uuid = this.getOwnerId();
			
			if (uuid != null)
	        {
	            reindeer.setOwnerId(uuid);
	            reindeer.setTamed(true);
	        }
			reindeer.setVariant(this.getVariant());
	        int j = this.rand.nextInt(2);
	        
	        if (j == 0) 
	        {
	            reindeer.setGender(1);
	        } 
	        else {
	            reindeer.setGender(2);
	        }
	        return reindeer;
	        
	 }
	 
	    public float getEyeHeight()
	    {
	        return this.isChild() ? this.height : 1.3F;
	    }
	    
	    protected void setOffspringAttributes(EntityAgeable p_190681_1_, EntityReindeer p_190681_2_) 
	    {
	    	double d0 = getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + p_190681_1_.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + getModifiedMaxHealth();
	    	p_190681_2_.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(d0 / 3.0D);
	    	double d1 = getEntityAttribute(JUMP_STRENGTH).getBaseValue() + p_190681_1_.getEntityAttribute(JUMP_STRENGTH).getBaseValue() + getModifiedJumpStrength();
	    	p_190681_2_.getEntityAttribute(JUMP_STRENGTH).setBaseValue(d1 / 3.0D);
	    	double d2 = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + p_190681_1_.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + getModifiedMovementSpeed();
	    	p_190681_2_.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(d2 / 3.0D);
	    }
	    
	    protected float getModifiedMaxHealth() 
	    {
	    	return 15.0F + this.rand.nextInt(8) + this.rand.nextInt(9);
	    }

	 
	 
//Begging	 
		public void setBegging(boolean beg)
		{
			this.dataManager.set(BEGGING, Boolean.valueOf(beg));
		}

		public boolean isBegging()
		{
			return ((Boolean)this.dataManager.get(BEGGING)).booleanValue();
		}


//Sounds
	 
		@Override
	    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
			{
	        return SoundHandler.REINDEER_HURT;
			}
		
		@Override
	    protected SoundEvent getDeathSound()
			{
	        return SoundHandler.REINDEER_DEATH;
			}
		
		@Override
	    protected SoundEvent getAmbientSound()
			{
	        return SoundHandler.REINDEER_LIVING;
			}
		
		@Override
		protected void playStepSound(BlockPos pos, Block blockIn)
			{
	        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
			}
		
	    protected float getSoundVolume()
	    {
	        return 0.4F;
	    }
	    
	    public int getTalkInterval()
	    {
	        return 400;
	    }
	
		
//Caravan
		   public void leaveCaravan()
		{
	        if (this.caravanHead != null)
	        {
	            this.caravanHead.caravanTail = null;
	        }

	        this.caravanHead = null;
	    }

	    public void joinCaravan(EntityReindeer entityreindeer)
	    {
	        this.caravanHead = entityreindeer;
	        this.caravanHead.caravanTail = this;
	    }

	    public boolean hasCaravanTrail()
	    {
	        return this.caravanTail != null;
	    }

	    public boolean inCaravan()
	    {
	        return this.caravanHead != null;
	    }

	    @Nullable
	    public EntityReindeer getCaravanHead()
	    {
	        return this.caravanHead;
	    }
		
	    protected ResourceLocation getLootTable()
	    {
	        return LootTableList.ENTITIES_COW;
	    }

//Riding
	    protected void mountTo(EntityPlayer player)
	    {
	        player.rotationYaw = this.rotationYaw;
	        player.rotationPitch = this.rotationPitch;

	        if (!this.world.isRemote)
	        {
	            player.startRiding(this);
	        }
	    }
	    
		public void setAIMoveSpeed(float speed) 
		{
			if (isBeingRidden()) 
			{
				super.setAIMoveSpeed(speed * 1.0F);
			} 
			else 
			{
				super.setAIMoveSpeed(speed);
			} 
		}
	    
	    protected boolean isMovementBlocked()
	    {
	        return super.isMovementBlocked() && this.isBeingRidden();
	    }
	    
	    public void travel(float strafe, float vertical, float forward)
	    {
	        if (this.isBeingRidden() && this.canBeSteered())
	        {
	            EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
	            this.rotationYaw = entitylivingbase.rotationYaw;
	            this.prevRotationYaw = this.rotationYaw;
	            this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
	            this.setRotation(this.rotationYaw, this.rotationPitch);
	            this.renderYawOffset = this.rotationYaw;
	            this.rotationYawHead = this.renderYawOffset;
	            strafe = entitylivingbase.moveStrafing * 0.5F;
	            forward = entitylivingbase.moveForward;

	            if (forward <= 0.0F)
	            {
	                forward *= 0.25F;
	            }

	            if (this.jumpPower > 0.0F && !this.isReindeerJumping() && this.onGround)
	            {
	                this.motionY = this.getReindeerJumpStrength() * (double)this.jumpPower;

	                if (this.isPotionActive(MobEffects.JUMP_BOOST))
	                {
	                    this.motionY += (double)((float)(this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
	                }

	                this.setReindeerJumping(true);
	                this.isAirBorne = true;

	                if (forward > 0.0F)
	                {
	                    float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
	                    float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
	                    this.motionX += (double)(-0.4F * f * this.jumpPower);
	                    this.motionZ += (double)(0.4F * f1 * this.jumpPower);
	                    this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4F, 1.0F);
	                }

	                this.jumpPower = 0.0F;
	            }
	            else if (this.jumpPower > 0.0F && this.isInWater() && !this.isReindeerJumping()) {
	                this.motionY = this.getReindeerJumpStrength() * 0.4F;
	                this.setReindeerJumping(true);
	                this.jumpPower = 0.0F;
	            }

	            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

	            if (this.canPassengerSteer())
	            {
	                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
	                super.travel(strafe, vertical, forward);
	            }
	            else if (entitylivingbase instanceof EntityPlayer)
	            {
	                this.motionX = 0.0D;
	                this.motionY = 0.0D;
	                this.motionZ = 0.0D;
	            }

	            if (this.onGround || this.isInWater())
	            {
	                this.jumpPower = 0.0F;
	                this.setReindeerJumping(false);
	            }

	        }
	        else
	        {
	            this.jumpMovementFactor = 0.02F;
	            super.travel(strafe, vertical, forward);
	        }
	    }
	    
	    protected double getModifiedJumpStrength()
	    {
	        return 0.4000000059604645D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D;
	    }

	    @Override
	    public void updatePassenger(Entity passenger)
	    {
	        super.updatePassenger(passenger);

	        if (passenger instanceof EntityLiving)
	        {
	            EntityLiving entityliving = (EntityLiving)passenger;
	            this.renderYawOffset = entityliving.renderYawOffset;
	        }
	        if (this.isReindeerJumping())
	        {
	            float f3 = MathHelper.sin(this.renderYawOffset * 0.017453292F);
	            float f = MathHelper.cos(this.renderYawOffset * 0.017453292F);
	            float f1 = 0.5F;
	            float f2 = 0.05F;
	            passenger.setPosition(this.posX + (double)(f1 * f3), this.posY + this.getMountedYOffset() + passenger.getYOffset() + (double)f2, this.posZ - (double)(f1 * f));

	            if (passenger instanceof EntityLivingBase)
	            {
	                ((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset;
	            }
	        }
	    }
		
		 public double getMountedYOffset() 
		 	{
			return this.height * 1.1D;
		 	}
		 
		    @Override
		    public boolean canRiderInteract() {
		        return false;
		    }
		 
		@Override
		public boolean canBeSteered() 
			{
			 return this.getControllingPassenger() instanceof EntityLivingBase;
			}
		
		 @Override
		    public boolean canBePushed() {
		        return !this.isBeingRidden();
		    }
		
	    public boolean isReindeerJumping()
	    {
	    	Log.info(this.reindeerJumping);
	        return this.reindeerJumping;
	    }
	    
	    public void setReindeerJumping(boolean jumping)
	    {
	        this.reindeerJumping = jumping;
	    }
	    
	    public double getReindeerJumpStrength()
	    {
	        return this.getEntityAttribute(JUMP_STRENGTH).getAttributeValue();
	    }
	    
	    protected double getModifiedMovementSpeed()
	    {
	        return (0.44999998807907104D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D) * 0.25D;
	    }

	    
	    @SideOnly(Side.CLIENT)
	    public void setJumpPower(int jumpPowerIn)
	    {
	            if (jumpPowerIn < 0)
	            {
	                jumpPowerIn = 0;
	            }

	            if (jumpPowerIn >= 90)
	            {
	                this.jumpPower = 1.0F;
	            }
	            else
	            {
	                this.jumpPower = 0.4F + 0.4F * (float)jumpPowerIn / 90.0F;
	            }
	    }
	    
	    public boolean canJump()
	    {
	        return true;
	    }
	    
	    public void handleStartJump(int p_184775_1_)
	    {
	    }

	    public void handleStopJump()
	    {
	    }
	    
	    @Nullable
	    public Entity getControllingPassenger()
	    {
	        return this.getPassengers().isEmpty() ? null : (Entity)this.getPassengers().get(0);
	    }

	    public void fall(float distance, float damageMultiplier)
	    {
	        if (distance > 1.0F)
	        {
	            this.playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4F, 1.0F);
	        }

	        int i = MathHelper.ceil((distance * 0.5F - 3.0F) * damageMultiplier);

	        if (i > 0)
	        {
	            this.attackEntityFrom(DamageSource.FALL, (float)i);

	            if (this.isBeingRidden())
	            {
	                for (Entity entity : this.getRecursivePassengers())
	                {
	                    entity.attackEntityFrom(DamageSource.FALL, (float)i);
	                }
	            }

	            IBlockState iblockstate = this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.2D - (double)this.prevRotationYaw, this.posZ));
	            Block block = iblockstate.getBlock();

	            if (iblockstate.getMaterial() != Material.AIR && !this.isSilent())
	            {
	                SoundType soundtype = block.getSoundType();
	                this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, soundtype.getStepSound(), this.getSoundCategory(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
	            }
	        }
	    }

}
