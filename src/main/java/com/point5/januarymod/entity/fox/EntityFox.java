package com.point5.januarymod.entity.fox;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import com.point5.januarymod.JanuaryMod;
import com.point5.januarymod.ai.EntityAIAvoidFox;
import com.point5.januarymod.ai.EntityAIBegFox;
import com.point5.januarymod.ai.EntityAIFoxAttack;
import com.point5.januarymod.ai.EntityAIPlayFox;
import com.point5.januarymod.entity.EntityCustom;
import com.point5.januarymod.util.handlers.FoxLogicHandler;
import com.point5.januarymod.util.handlers.SoundHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFox extends EntityCustom {
	private static final DataParameter<Float> DATA_HEALTH_ID = EntityDataManager.<Float>createKey(EntityFox.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityFox.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> GENDER = EntityDataManager.<Integer>createKey(EntityFox.class, DataSerializers.VARINT);
    protected static final DataParameter<Boolean> WANDERING = EntityDataManager.createKey(EntityFox.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> BEGGING = EntityDataManager.<Boolean>createKey(EntityFox.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> SLEEPING = EntityDataManager.<Boolean>createKey(EntityFox.class, DataSerializers.BOOLEAN);
	
    private static final Set<Item> TAME_ITEMS = Sets.newHashSet(Items.RABBIT, Items.CHICKEN, Items.FISH, Items.EGG);

    private EntityAISit aiSit;
    public EntityAIAvoidFox<EntityPlayer> aiAvoid;
    private EntityAITempt aiTempt;
    public FoxLogicHandler logic;
    
    private boolean isWet;
    private boolean isShaking;
    private boolean isPlaying;
    
    private float timeFoxIsShaking;
    private float prevTimeFoxIsShaking;
    
    public float sleepProgress;
    public boolean isDaytime;
    private boolean isSleeping;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
            
	public EntityFox(World worldIn)
	{
		super(worldIn);
		this.setSize(1.4F, 0.9F);
        this.logic = createFoxLogic();

	}
	
	  //AI	
  	@Override
  	protected void initEntityAI()
      {
  		clearAITasks();
  		this.aiSit = new EntityAISit((EntityFox)this);
        this.aiTempt = new EntityAITempt(this, 0.7D, Items.RABBIT, true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, this.aiTempt);
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.1D, 10.0F, 2.0F));
        this.tasks.addTask(6, new EntityAILeapAtTarget(this, 0.3F));
        this.tasks.addTask(7, new EntityAIPlayFox(this, 0.5D));
        this.tasks.addTask(8, new EntityAIFoxAttack(this));
        this.tasks.addTask(9, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(10, new EntityAIBegFox(this, 8.0F));
        this.tasks.addTask(11, new EntityAIWanderAvoidWater(this, 1.0D, 1.0000001E-5F));
        this.tasks.addTask(12, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.targetTasks.addTask(2, new EntityAITargetNonTamed(this, EntityChicken.class, true, (Predicate)null));
      }
	
  	@Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(WANDERING, Boolean.valueOf(true));
        this.dataManager.register(GENDER, Integer.valueOf(0));
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(BEGGING, Boolean.valueOf(false));
        this.dataManager.register(SLEEPING, Boolean.valueOf(false));
        this.dataManager.register(DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
    }  
	
	@Override
  	protected void updateAITasks()
      	{
          this.dataManager.set(DATA_HEALTH_ID, Float.valueOf(this.getHealth()));
          super.updateAITasks();
      	}
	
	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
        EntityFox entityfox = new EntityFox(this.world);
        entityfox.setGrowingAge(-24000);
	    this.setGender(this.rand.nextInt(2));
        this.setVariant(new Random().nextInt(setFoxVariants()));
	    this.setSleeping(false);
	    return super.onInitialSpawn(difficulty, livingdata);
	}	
	
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(56.0D);
	}
	
	public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Wandering", this.isWandering());
        compound.setInteger("Gender", this.getGender());
        compound.setInteger("Variant", this.getVariant());
        compound.setBoolean("Sleeping", this.isSleeping());
        compound.setBoolean("TamedFox", this.isTamed());
        if (this.getCustomNameTag() != null && !this.getCustomNameTag().isEmpty()) {
            compound.setString("CustomName", this.getCustomNameTag());
        }
    }

	public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setWandering(compound.getBoolean("Wandering"));
        this.setGender(compound.getInteger("Gender"));
        this.setVariant(compound.getInteger("Variant"));
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.setTamed(compound.getBoolean("TamedFox"));
        if (!compound.getString("CustomName").isEmpty()) {
            this.setCustomNameTag(compound.getString("CustomName"));
        }
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
	        return ((Integer)this.dataManager.get(VARIANT)).intValue();
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
	
	public int setFoxVariants() {
		return 2;
	}
	
	public boolean isSleeping() {
        if (world.isRemote) {
            boolean isSleeping = this.dataManager.get(SLEEPING).booleanValue();
            this.isSleeping = isSleeping;
            return isSleeping;
        }
        return isSleeping;
    }

    public void setSleeping(boolean sleeping) {
		this.dataManager.set(SLEEPING, sleeping);
		    if (!world.isRemote) {
		    	this.isSleeping = sleeping;
		    }
    }
    
    public void shouldFindShelter()
    {
    	if (!this.isSleeping() && this.isDaytime()) {
    		if (this.world.canSeeSky(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)))
    		{
    			this.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, 1.5F);
    		}
    	}
    }
    
    @Nullable
    private Vec3d findPossibleShelter()
    {
        Random random = this.getRNG();
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

        for (int i = 0; i < 10; ++i)
        {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

            if (!this.world.canSeeSky(blockpos1) && this.getBlockPathWeight(blockpos1) < 0.0F)
            {
                return new Vec3d((double)blockpos1.getX(), (double)blockpos1.getY(), (double)blockpos1.getZ());
            }
        }

        return null;
    }
    
    public boolean isDaytime() {
        return this.world.isDaytime();
    }
	
	//Begging	 
	public void setBegging(boolean beg)
	{
		this.dataManager.set(BEGGING, Boolean.valueOf(beg));
	}

	public boolean isBegging()
	{
		if (this.isSleeping()) 
		{
			return false;
		}
		else
		{
		return ((Boolean)this.dataManager.get(BEGGING)).booleanValue();
		}
	}
	
    public void setPlaying(boolean playing)
    {
        this.isPlaying = playing;
    }

    public boolean isPlaying()
    {
        return this.isPlaying;
    }
    
	
	public void onUpdate()
    {
        super.onUpdate();
        logic.updateFoxCommon();
        
        if (world.isRemote) {
            logic.updateFoxClient();
        } else {
            logic.updateFoxServer();
        }

        if (this.isWet()) 
        {

            this.isWet = true;
            this.isShaking = false;
            this.timeFoxIsShaking = 0.0F;
            this.prevTimeFoxIsShaking = 0.0F;
        }
        else if (this.isShaking) {

            if (this.timeFoxIsShaking == 0.0F) {

                this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.prevTimeFoxIsShaking = this.timeFoxIsShaking;
            this.timeFoxIsShaking += 0.05F;

            if (this.prevTimeFoxIsShaking >= 2.0F) {

                this.isWet = false;
                this.isShaking = false;
                this.prevTimeFoxIsShaking = 0.0F;
                this.timeFoxIsShaking = 0.0F;
            }

            if (this.prevTimeFoxIsShaking > 0.4F && this.world.isRemote) {

                float py = (float)this.getEntityBoundingBox().minY;

                int i = (int)(MathHelper.sin((this.timeFoxIsShaking - 0.4F) * (float)Math.PI) * 7.0F);
                for (int j = 0; j < i; ++j) {

                    float px = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float pz = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float vx = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.1F;
                    float vz = (this.rand.nextFloat() * 2.0F - 1.0F) * 0.1F;
                    JanuaryMod.proxy.doParticleEffect("shake", this, this.posX + px, py + 0.8F, this.posZ + pz, vx, this.motionY, vz);
                }
            }
        }
    }
	 
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (!this.world.isRemote && this.isWet && !this.isShaking && !this.isSleeping() && this.onGround && !this.hasPath()) {

            this.isShaking = true;
            this.timeFoxIsShaking = 0.0F;
            this.prevTimeFoxIsShaking = 0.0F;
            this.world.setEntityState(this, (byte)8);
        }
	}  
	
	public void setWandering(boolean value) {
		
	        this.dataManager.set(WANDERING, Boolean.valueOf(value));
	}

	public boolean isWandering() 
	{		
	        return this.dataManager.get(WANDERING);
	}
    
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            Entity entity = source.getTrueSource();

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
            {
                amount = (amount + 1.0F) / 2.0F;
            }
            if (amount > 0) {
                if (this.isSleeping())
                {
                    this.setSleeping(false);
                }
                else if (this.aiSit != null)
                {
                    this.aiSit.setSitting(false);
                }
            }
            return super.attackEntityFrom(source, amount);
        }
    }

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
        if (id == 8) {

            this.isShaking = true;
            this.timeFoxIsShaking = 0.0F;
            this.prevTimeFoxIsShaking = 0.0F;
        }
        else if (id == 9) {

            Vec3d.fromPitchYaw(0.0F, this.getRotationYawHead());
        }
        else {
			super.handleStatusUpdate(id);
        }
	}
	
	@SideOnly(Side.CLIENT)
    protected boolean isFoxWet() {

        return this.isWet;
    }

	 @SideOnly(Side.CLIENT)
	    protected float getShadingWhileWet(float partialTicks) {

	        return 0.75F + (this.prevTimeFoxIsShaking + (this.timeFoxIsShaking - this.prevTimeFoxIsShaking) * partialTicks) / 2.0F * 0.25F;
	    }

	    @SideOnly(Side.CLIENT)
	    protected float getShakeAngle(float partialTicks, float offset) {

	        float f = (this.prevTimeFoxIsShaking + (this.timeFoxIsShaking - this.prevTimeFoxIsShaking) * partialTicks + offset) / 1.8F;

	        if (f < 0.0F) { f = 0.0F; }
	        else if (f > 1.0F) { f = 1.0F; }

	        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0F) * 0.15F * (float)Math.PI;
	    }

	
    public int getMaxSpawnedInChunk()
    {
        return 5;
    }
  	
    public boolean canMove() {
        return !this.isSitting() && !this.isSleeping() && this.getControllingPassenger() == null && sleepProgress == 0;
    }
  	
  	protected void clearAITasks()
  		{
  		tasks.taskEntries.clear();
  		targetTasks.taskEntries.clear();
  		}
  	
    public boolean attackEntityAsMob(Entity entityIn)
    {
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
    }

  //Interaction with Player	 
  	 public boolean processInteract(EntityPlayer player, EnumHand hand) 
  		{
         ItemStack item = player.getHeldItem(hand);

         if (this.isTamed()) 
         {
             if (!item.isEmpty()) 
             {
                 if (item.getItem() instanceof ItemFood) 
                 {
                     ItemFood food = (ItemFood)item.getItem();
                     
                     if (TAME_ITEMS.contains(item.getItem()) && ((Float)this.dataManager.get(DATA_HEALTH_ID)).floatValue() < 15.0F)
                     {
                    	 if (!player.capabilities.isCreativeMode)
                    	 {
                    		 item.shrink(1);
                    	 }
                    	 this.heal(food.getHealAmount(item));
                    	 this.setSleeping(false);
		                 return true;
                     }
                 }
                 else if (item.getItem() == Items.BOOK)
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
             }
             
             if (this.isOwner(player) && !this.world.isRemote && !this.isBreedingItem(item))
             {

                 this.aiSit.setSitting(!this.isSitting());
            	 this.setSleeping(false);
                 this.isJumping = false;
                 this.navigator.clearPath();
                 return true;
             }
         }
         else if ((this.aiTempt == null || this.aiTempt.isRunning()) && TAME_ITEMS.contains(item.getItem()) && player.getDistanceSq(this) < 9.0D) 
         { // tame
             if (!player.capabilities.isCreativeMode) 
             { 
            	 item.shrink(1); 
             }

             if (!this.world.isRemote) 
             {
                 if (this.rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player))
                { // success

                     this.setTamedBy(player);
                     this.navigator.clearPath();
                     this.playTameEffect(true);
                     this.aiSit.setSitting(true);
                     this.setHealth(15.0F);
                     this.world.setEntityState(this, (byte)7);
                 }
                 else 
                 { // failure
                     this.playTameEffect(false);
                     this.world.setEntityState(this, (byte)6);
                 }
             }
             return true;
         	}
  	 
         return super.processInteract(player, hand);
  		}
         
         protected void setupTamedAI() {

             if (this.aiAvoid == null && !this.isSleeping)
             {
             	this.aiAvoid = new EntityAIAvoidFox<EntityPlayer>(this, EntityPlayer.class, 16.0F, 0.8D, 1.33D); 
             }
             if (this.aiAvoid == null && this.isSleeping)
             {
             	this.aiAvoid = new EntityAIAvoidFox<EntityPlayer>(this, EntityPlayer.class, 5.0F, 0.8D, 1.33D); 
             }
             
             this.tasks.removeTask(this.aiAvoid);
             
             if (!this.isTamed()) 
             { 
            	 this.tasks.addTask(4, this.aiAvoid); 
             }
         }
         
         public boolean isNotColliding()
         {
             if (this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox()))
             {
                 BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

                 if (blockpos.getY() < this.world.getSeaLevel())
                 {
                     return false;
                 }

                 IBlockState iblockstate = this.world.getBlockState(blockpos.down());
                 Block block = iblockstate.getBlock();

                 if (block == Blocks.GRASS || block.isLeaves(iblockstate, this.world, blockpos.down()))
                 {
                     return true;
                 }
             }

             return false;
         }
 
  	//Breeding	 
         public boolean isBreedingItem(ItemStack item) {

             return (item.getItem().equals(Items.RABBIT) || item.getItem().equals(Items.COOKED_RABBIT));
         }
  	 
  		  
         public boolean canMateWith(EntityAnimal otheranimal) {

             return this.isTamed() && !this.isSitting() && super.canMateWith(otheranimal) && !((EntityTameable)otheranimal).isSitting();
         }
  		 
  		 @Override
  			public EntityAgeable createChild(EntityAgeable ageable) 
  		 {
  			 EntityFox fox = new EntityFox(this.world);
  				UUID uuid = this.getOwnerId();
  				
  				if (uuid != null)
  		        {
  		            fox.setOwnerId(uuid);
  		            fox.setTamed(true);
  		        }
  				fox.setVariant(this.getVariant());
  		        int j = this.rand.nextInt(2);
  		        
  		        if (j == 0) 
  		        {
  		            fox.setGender(1);
  		        } 
  		        else {
  		            fox.setGender(2);
  		        }
  		        return fox;
  		        
  		 }
  		 
  		    public float getEyeHeight()
  		    {
  		    	if (this.getLeashed())
  		    	{
  		    		 return this.height = 0.1F;
  		    	}
  		    	else
  		    	{
  		        return this.isChild() ? this.height : 0.4F;
  		    	}
  		    }

  		//Sounds
  			 
  			@Override
  		    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
  				{
  		        return SoundHandler.FOX_HURT;
  				}
  			
  			@Override
  		    protected SoundEvent getDeathSound()
  				{
  		        return SoundHandler.FOX_DEATH;
  				}
  			
  			@Override
  		    protected SoundEvent getAmbientSound()
  			{
  				if (this.isSleeping())
  				{
  					return null;
  				}
  				else if (this.isBegging())
  				{
  					return SoundHandler.FOX_SNIFFING;
  				}
  				return SoundHandler.FOX_LIVING; 
  			}
  			

			protected float getSoundVolume()
  		    {
  		        return 0.4F;
  		    }
  		    
  		    public int getTalkInterval()
  		    {
  		        return 400;
  		    }
  		    
  		   protected void playStepSound(BlockPos pos, Block blockIn) {

  		        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
  		    }
  		   
  		    public boolean canBeLeashedTo(EntityPlayer player)
  		    {
  		        return super.canBeLeashedTo(player);
  		    }
  			
  		  public void updateCheckPlayer() {
  	        double checklength = this.getEntityBoundingBox().getAverageEdgeLength() * 1;
  	        EntityPlayer player = world.getClosestPlayerToEntity(this, checklength);
  	        if (this.isSleeping()) {
  	            if (player != null && !this.isOwner(player) && !player.isCreative()) {
  	                this.setSleeping(false);
  	            }
  	        }
  	    }
  		  
  		 @Override
  	    public void travel(float strafe, float forward, float vertical) {
  	        if (!this.canMove() || this.isSitting()) {
  	            strafe = 0;
  	            forward = 0;
  	            vertical = 0;
  	            moveVertical = 0;
  	            moveStrafing = 0;
  	            moveForward = 0;
  	            super.travel(strafe, forward, vertical);
  	            return;
  	        }
  	        super.travel(strafe, forward, vertical);
  	    }
  		 
  		protected FoxLogicHandler createFoxLogic(){
  	        return new FoxLogicHandler(this);
  	    }
	}
