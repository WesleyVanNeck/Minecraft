package net.minecraft.server;

import java.util.UUID;
import javax.annotation.Nullable;

public class EntityPigZombie extends EntityZombie {
    private static final UUID a = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier b = (new AttributeModifier(a, "Attacking speed boost", 0.05D, 0)).a(false);
    public int angerLevel;
    private int soundDelay;
    private UUID hurtBy;

    public EntityPigZombie(World world) {
        super(EntityTypes.ZOMBIE_PIGMAN, world);
        this.fireProof = true;
    }

    public void a(@Nullable EntityLiving entityliving) {
        super.a(entityliving);
        if (entityliving != null) {
            this.hurtBy = entityliving.getUniqueID();
        }

    }

    protected void l() {
        this.goalSelector.a(2, new PathfinderGoalZombieAttack(this, 1.0D, false));
        this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D));
        this.targetSelector.a(1, new EntityPigZombie.PathfinderGoalAngerOther(this));
        this.targetSelector.a(2, new EntityPigZombie.PathfinderGoalAnger(this));
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(c).setValue(0.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue((double)0.23F);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(5.0D);
    }

    protected boolean dC() {
        return false;
    }

    protected void mobTick() {
        AttributeInstance attributeinstance = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        if (this.dF()) {
            if (!this.isBaby() && !attributeinstance.a(b)) {
                attributeinstance.b(b);
            }

            --this.angerLevel;
        } else if (attributeinstance.a(b)) {
            attributeinstance.c(b);
        }

        if (this.soundDelay > 0 && --this.soundDelay == 0) {
            this.a(SoundEffects.ENTITY_ZOMBIE_PIGMAN_ANGRY, this.cD() * 2.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        if (this.angerLevel > 0 && this.hurtBy != null && this.getLastDamager() == null) {
            EntityHuman entityhuman = this.world.b(this.hurtBy);
            this.a((EntityLiving)entityhuman);
            this.killer = entityhuman;
            this.lastDamageByPlayerTime = this.cg();
        }

        super.mobTick();
    }

    public boolean a(GeneratorAccess generatoraccess, boolean var2) {
        return generatoraccess.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    public boolean a(IWorldReader iworldreader) {
        return iworldreader.a_(this, this.getBoundingBox()) && iworldreader.getCubes(this, this.getBoundingBox()) && !iworldreader.containsLiquid(this.getBoundingBox());
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setShort("Anger", (short)this.angerLevel);
        if (this.hurtBy != null) {
            nbttagcompound.setString("HurtBy", this.hurtBy.toString());
        } else {
            nbttagcompound.setString("HurtBy", "");
        }

    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.angerLevel = nbttagcompound.getShort("Anger");
        String s = nbttagcompound.getString("HurtBy");
        if (!s.isEmpty()) {
            this.hurtBy = UUID.fromString(s);
            EntityHuman entityhuman = this.world.b(this.hurtBy);
            this.a((EntityLiving)entityhuman);
            if (entityhuman != null) {
                this.killer = entityhuman;
                this.lastDamageByPlayerTime = this.cg();
            }
        }

    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            Entity entity = damagesource.getEntity();
            if (entity instanceof EntityHuman && !((EntityHuman)entity).u()) {
                this.a(entity);
            }

            return super.damageEntity(damagesource, f);
        }
    }

    private void a(Entity entity) {
        this.angerLevel = 400 + this.random.nextInt(400);
        this.soundDelay = this.random.nextInt(40);
        if (entity instanceof EntityLiving) {
            this.a((EntityLiving)entity);
        }

    }

    public boolean dF() {
        return this.angerLevel > 0;
    }

    protected SoundEffect D() {
        return SoundEffects.ENTITY_ZOMBIE_PIGMAN_AMBIENT;
    }

    protected SoundEffect d(DamageSource var1) {
        return SoundEffects.ENTITY_ZOMBIE_PIGMAN_HURT;
    }

    protected SoundEffect cs() {
        return SoundEffects.ENTITY_ZOMBIE_PIGMAN_DEATH;
    }

    @Nullable
    protected MinecraftKey getDefaultLootTable() {
        return LootTables.au;
    }

    public boolean a(EntityHuman var1, EnumHand var2) {
        return false;
    }

    protected void a(DifficultyDamageScaler var1) {
        this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
    }

    protected ItemStack dB() {
        return ItemStack.a;
    }

    public boolean c(EntityHuman var1) {
        return this.dF();
    }

    static class PathfinderGoalAnger extends PathfinderGoalNearestAttackableTarget<EntityHuman> {
        public PathfinderGoalAnger(EntityPigZombie entitypigzombie) {
            super(entitypigzombie, EntityHuman.class, true);
        }

        public boolean a() {
            return ((EntityPigZombie)this.e).dF() && super.a();
        }
    }

    static class PathfinderGoalAngerOther extends PathfinderGoalHurtByTarget {
        public PathfinderGoalAngerOther(EntityPigZombie entitypigzombie) {
            super(entitypigzombie, true);
        }

        protected void a(EntityCreature entitycreature, EntityLiving entityliving) {
            super.a(entitycreature, entityliving);
            if (entitycreature instanceof EntityPigZombie) {
                ((EntityPigZombie)entitycreature).a((Entity)entityliving);
            }

        }
    }
}