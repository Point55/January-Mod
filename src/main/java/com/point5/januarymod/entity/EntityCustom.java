package com.point5.januarymod.entity;

import java.util.UUID;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityCustom extends EntityTameable {
	
    private static final DataParameter<Integer> MOVEMENT_STATE;
	private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityCustom.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> GENDER = EntityDataManager.<Integer>createKey(EntityCustom.class, DataSerializers.VARINT);
    
	public EntityCustom(World worldIn)
	{
		super(worldIn);
		this.setMovement(0);
	}
	
    @Override
    public void setUniqueId(UUID uuid) 
    {
    	super.setUniqueId(UUID.randomUUID());
    }
	
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register((DataParameter)EntityCustom.MOVEMENT_STATE, (Object)0);
    }  
	
	public void writeEntityToNBT(final NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Movement", (int)this.getMovement());
    }

	public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setMovement(compound.getInteger("Movement"));
    }
	
	public Integer getMovement() {
        return (Integer)this.dataManager.get((DataParameter)EntityCustom.MOVEMENT_STATE);
    }
	
    public void setMovement(final int state) {
        this.dataManager.set((DataParameter)EntityCustom.MOVEMENT_STATE, (Object)state);
    }
	 
    public boolean isMobile() {
        return this.getMovement() == 0;
    }
    
    static
    {
    	 MOVEMENT_STATE = EntityDataManager.createKey((Class)EntityCustom.class, DataSerializers.VARINT);
    }
    
	public int getVariant()
	{
	        return MathHelper.clamp(((Integer)this.dataManager.get(VARIANT)).intValue(), 0, 3);
	}
	 
	public void setVariant(int p_191997_1_)
	{
	        this.dataManager.set(VARIANT, Integer.valueOf(p_191997_1_));
	}

	public int getGender()
	{
	        return MathHelper.clamp(((Integer)this.dataManager.get(GENDER)).intValue(), 1, 3);
	}

	public void setGender(int p_191997_1_)
	{
	        this.dataManager.set(GENDER, Integer.valueOf(p_191997_1_));
	}
	

	 @Override
		public EntityAgeable createChild(EntityAgeable ageable) 
	 {
			EntityCustom entitycustom = new EntityCustom(this.world);
			UUID uuid = this.getOwnerId();
			if (uuid != null)
	        {
	            entitycustom.setOwnerId(uuid);
	            entitycustom.setTamed(true);
	        }
			
	        entitycustom.setVariant(this.getVariant());
	        int j = this.rand.nextInt(2);
	        
	        if (j == 0) 
	        {
	            entitycustom.setGender(1);
	        } 
	        else {
	            entitycustom.setGender(2);
	        }

	        return entitycustom;
	        
	 }

	 
}
