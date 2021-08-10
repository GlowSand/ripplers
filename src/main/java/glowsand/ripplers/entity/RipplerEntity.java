package glowsand.ripplers.entity;

import glowsand.ripplers.Ids;
import glowsand.ripplers.Ripplers;
import glowsand.ripplers.block.TrimmedChorusBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Random;

@SuppressWarnings("EntityConstructor")
public class RipplerEntity extends AnimalEntity implements Flutterer, IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    public World chorusWorld;
    public BlockPos chorusBlockPos;
    public boolean isOnTheMfGroundBcItSeemsToNotWorkIdkWhy = false;
    private static final TrackedData<Integer> VARIANT;
    public RipplerEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.ignoreCameraFrustum = true;
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.LAVA, -1.0F);

    }


    @Override
    public boolean canSpawn(WorldView world) {
        return true;
    }




    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        return true;
    }

    public static boolean isValidNaturalSpawn(EntityType<RipplerEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        int height = world.getChunk(pos).sampleHeightmap(Heightmap.Type.WORLD_SURFACE, pos.getX() & 15, pos.getY() & 15);
        return height > 0 && pos.getY() >= height;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(VARIANT,this.getRandomVariant());
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new TemptGoal(this, 1.25D,Ingredient.ofItems(Items.CHORUS_FRUIT) , false){
            @Override
            public boolean canStart() {
                return RipplerEntity.this.chorusBlockPos == null && RipplerEntity.this.chorusWorld == null &&  super.canStart();
            }
        });
        this.goalSelector.add(1,new EscapeDangerGoal(this,1.25D){
            @Override
            public boolean canStart() {
                return RipplerEntity.this.chorusBlockPos == null && RipplerEntity.this.chorusWorld == null &&  super.canStart();
            }
        });
        this.goalSelector.add(4,new LookAtEntityGoal(this, MobEntity.class,15,.75f));
        this.goalSelector.add(4,new LookAtEntityGoal(this, PlayerEntity.class,15,.75f));
        this.goalSelector.add(0,new RipplerGoToBlockAndFuckingDieGoal(this,.75D,10,4));
        this.goalSelector.add(8,new RipplerWanderAroundGoal());
        super.initGoals();
    }



    @Override
    public boolean hurtByWater() {
        return true;
    }


    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return Registry.SOUND_EVENT.get(Ids.RIPPLER_DEATH_SOUND);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return Registry.SOUND_EVENT.get(Ids.RIPPLER_HURT_SOUND);
    }

    @Override
    public float getSoundPitch() {
        return 1-this.random.nextFloat()*2;
    }

    @Override
    protected float getSoundVolume() {
        return 1-this.random.nextFloat();
    }

    @Override
    protected boolean hasWings() {
        return true;
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(false);

        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }


    @Override
    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean isInAir() {
        return !this.isOnTheMfGroundBcItSeemsToNotWorkIdkWhy;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }



    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (!this.isOnTheMfGroundBcItSeemsToNotWorkIdkWhy) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("rippler.fly.animation", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;

    }

    @Override
    public void registerControllers(AnimationData animationData) {
       animationData.addAnimationController(new AnimationController<>(this,"controller",0,this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public int ticks = 0;
    @Override
    public void tick() {
        if (this.world.isClient && ticks%5==0) {
            for(int i = 0; i < 2 ; ++i) {
                this.world.addParticle(ParticleTypes.PORTAL, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        }
        if((chorusWorld== null && chorusBlockPos!=null) || (chorusWorld !=null && chorusBlockPos==null)){
            chorusWorld=null;
            chorusBlockPos=null;
        }
        if (ticks%6==0){
            this.isOnTheMfGroundBcItSeemsToNotWorkIdkWhy = this.getEntityWorld().getBlockState(this.getBlockPos().down()).getMaterial().blocksMovement();

        }


        if (ticks >= 60 && chorusWorld != null){
            ticks=0;
            BlockEntity blockEntity = chorusWorld.getBlockEntity(chorusBlockPos);
            if (blockEntity != null){
                ((TrimmedChorusBlockEntity)blockEntity).rippler = this.getUuid();
                this.getNavigation().startMovingTo(chorusBlockPos.getX(),chorusBlockPos.getY(),chorusBlockPos.getZ(),1.25D);
            }

            if (!this.getBlockPos().isWithinDistance(chorusBlockPos,10) ||blockEntity == null || this.world!= chorusWorld){
                chorusWorld=null;
                chorusBlockPos=null;
                if (blockEntity!=null){
                    ((TrimmedChorusBlockEntity)blockEntity).rippler =null;
                }
            }
        }
        ticks++;
        if (chorusBlockPos !=null){
            this.lookControl.lookAt(Vec3d.ofCenter(chorusBlockPos));

        }
        super.tick();
    }

    @Override
    public void onDeath(DamageSource source) {
        if (chorusWorld != null && chorusBlockPos !=null) {
            BlockEntity blockEntity = chorusWorld.getBlockEntity(chorusBlockPos);
            if (blockEntity != null) {
                ((TrimmedChorusBlockEntity) blockEntity).rippler = null;
                if (((TrimmedChorusBlockEntity) blockEntity).player !=null && world instanceof ServerWorld serverWorld && serverWorld.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES)){
                    ServerPlayerEntity playerEntity = serverWorld.getServer().getPlayerManager().getPlayer(((TrimmedChorusBlockEntity) blockEntity).player);
                    if (playerEntity!=null){
                        playerEntity.sendMessage(new TranslatableText("ripplers.rippler.fucking.died.message"), false);
                    }
                }
            }
        }
        super.onDeath(source);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        if (chorusWorld !=null) {
            nbt.putString("chorusWorld", chorusWorld.getRegistryKey().getValue().toString());
        }
        if (chorusBlockPos !=null) {
            nbt.putInt("chorusPosX", chorusBlockPos.getX());
            nbt.putInt("chorusPosX", chorusBlockPos.getY());
            nbt.putInt("chorusPosX", chorusBlockPos.getZ());
        }
        nbt.putInt("variant",this.dataTracker.get(VARIANT));
        return super.writeNbt(nbt);
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("chorusWorld")) {

            String registrykey = nbt.getString("chorusWorld");
            if (!this.world.isClient&&this.getServer() !=null ) {

                this.chorusWorld = getServer().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(registrykey)));
            }
        }
        if (nbt.contains("chorusPosX")&&nbt.contains("chorusPosY")&&nbt.contains("chorusPosZ")){
            this.chorusBlockPos = new BlockPos(nbt.getInt("chorusPosX"),nbt.getInt("chorusPosY"),nbt.getInt("chorusPosZ"));
        }
        if (nbt.contains("variant")){
            this.dataTracker.set(VARIANT,nbt.getInt("variant"));
        }
        super.readNbt(nbt);
    }

    public static DefaultAttributeContainer.Builder createRipplerAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 7.0D).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6000000238418579D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D);
    }

    class RipplerWanderAroundGoal extends Goal {


        RipplerWanderAroundGoal() {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            return RipplerEntity.this.navigation.isIdle() && RipplerEntity.this.random.nextInt(10) == 0 && RipplerEntity.this.chorusBlockPos == null && RipplerEntity.this.chorusWorld == null;
        }

        public boolean shouldContinue() {
            return RipplerEntity.this.navigation.isFollowingPath() && RipplerEntity.this.chorusBlockPos == null && RipplerEntity.this.chorusWorld == null;
        }

        public void start() {
            Vec3d vec3d = this.getRandomLocation();
            if (vec3d != null) {
                RipplerEntity.this.navigation.startMovingAlong(RipplerEntity.this.navigation.findPathTo(new BlockPos(vec3d), 1), .50D);
            }


        }

        @Nullable
        private Vec3d getRandomLocation() {
            Vec3d vec3d3 = RipplerEntity.this.getRotationVec(0.0F);
            Vec3d vec3d4 = AboveGroundTargeting.find(RipplerEntity.this, 8, 7, vec3d3.x, vec3d3.z, 1.5707964F, 3, 1);
            return vec3d4 != null ? vec3d4 : NoPenaltySolidTargeting.find(RipplerEntity.this, 8, 4, -2, vec3d3.x, vec3d3.z, 1.5707963705062866D);
        }
    }

    class RipplerGoToBlockAndFuckingDieGoal extends MoveToTargetPosGoal {


        public RipplerGoToBlockAndFuckingDieGoal(PathAwareEntity mob, double speed, int range, int maxYDifference) {
            super(mob, speed, range, maxYDifference);
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            return blockEntity != null && blockEntity.getType().equals(Ripplers.TRIMMED_CHORUS_BLOCK_ENTITY) && ((TrimmedChorusBlockEntity)blockEntity).rippler == null;
        }

        @Override
        public void tick() {
            if (this.hasReached()) {
                if (!this.mob.world.isClient) {
                    BlockEntity blockEntity = this.mob.world.getBlockEntity(this.targetPos);
                    if (blockEntity != null && blockEntity.getType().equals(Ripplers.TRIMMED_CHORUS_BLOCK_ENTITY)) {
                        ((TrimmedChorusBlockEntity) blockEntity).rippler = this.mob.getUuid();
                        RipplerEntity.this.chorusBlockPos = this.targetPos;
                        RipplerEntity.this.chorusWorld = this.mob.world;
                    }
                }
            }
            super.tick();
        }
    }

    public int getRandomVariant(){

        if (this.random.nextInt(50)==0){
            return 2;
        }
        return 1;
    }



    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    static{
        VARIANT = DataTracker.registerData(RipplerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
