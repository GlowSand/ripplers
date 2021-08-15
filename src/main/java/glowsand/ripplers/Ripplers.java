package glowsand.ripplers;

import glowsand.ripplers.block.TrimmedChorusBlock;
import glowsand.ripplers.block.TrimmedChorusBlockEntity;
import glowsand.ripplers.client.renderer.RipplerEntityRenderer;
import glowsand.ripplers.entity.RipplerEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.GeckoLib;


@Mod(Ids.MOD_ID)
public class Ripplers {
    public static InteractWithTheDamnMfThingCriterion criterion;
    public static TrimTheDamnMfThingCriterion criterion2;
    public static final TrimmedChorusBlock TRIMMED_CHORUS_BLOCK =  new TrimmedChorusBlock(AbstractBlock.Settings.copy(Blocks.CHORUS_FLOWER));
    public static final BlockEntityType<TrimmedChorusBlockEntity> TRIMMED_CHORUS_BLOCK_ENTITY = BlockEntityType.Builder.create(TrimmedChorusBlockEntity::new, TRIMMED_CHORUS_BLOCK).build(null);
    public static final EntityType<RipplerEntity> RIPPLER_ENTITY_TYPE = EntityType.Builder.create(RipplerEntity::new,SpawnGroup.CREATURE).setDimensions(0.6F,0.5F).setTrackingRange(8).build(Ids.RIPPLER_ENTITY.getPath());
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES,Ids.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,Ids.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,Ids.MOD_ID);
    public static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS,Ids.MOD_ID);
    public static final DeferredRegister<Block> BLOCK_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS,Ids.MOD_ID);
    public Ripplers() {
        GeckoLib.initialize();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        criterion= Criteria.register(new InteractWithTheDamnMfThingCriterion());
        criterion2 = Criteria.register(new TrimTheDamnMfThingCriterion());
        ENTITY_TYPE_DEFERRED_REGISTER.register(Ids.RIPPLER_ENTITY.getPath(),()->RIPPLER_ENTITY_TYPE);
        BLOCK_ENTITY_TYPE_DEFERRED_REGISTER.register(Ids.TRIMMED_CHORUS_FLOWER_ENTITY.getPath(),()->TRIMMED_CHORUS_BLOCK_ENTITY);
        BLOCK_DEFERRED_REGISTER.register(Ids.TRIMMED_CHORUS_FLOWER.getPath(),()->TRIMMED_CHORUS_BLOCK);
        ITEM_DEFERRED_REGISTER.register(Ids.TRIMMED_CHORUS_FLOWER.getPath(),()->new BlockItem(TRIMMED_CHORUS_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        ITEM_DEFERRED_REGISTER.register(Ids.RIPPLER_SPAWN_EGG.getPath(),()->new SpawnEggItem(RIPPLER_ENTITY_TYPE,2888732, 3423236, (new Item.Settings()).group(ItemGroup.MISC)));
        SOUND_EVENT_DEFERRED_REGISTER.register(Ids.CHORUS_HEART_BEAT.getPath(),()->new SoundEvent(Ids.CHORUS_HEART_BEAT));
        SOUND_EVENT_DEFERRED_REGISTER.register(Ids.CHORUS_TRIM.getPath(),()->new SoundEvent(Ids.CHORUS_TRIM));
        SOUND_EVENT_DEFERRED_REGISTER.register(Ids.RIPPLER_HURT_SOUND.getPath(),()->new SoundEvent(Ids.RIPPLER_HURT_SOUND));
        SOUND_EVENT_DEFERRED_REGISTER.register(eventBus);
        BLOCK_DEFERRED_REGISTER.register(eventBus);
        ITEM_DEFERRED_REGISTER.register(eventBus);
        BLOCK_ENTITY_TYPE_DEFERRED_REGISTER.register(eventBus);
        ENTITY_TYPE_DEFERRED_REGISTER.register(eventBus);
        eventBus.addListener(this::entityAttributeEventThing);
        eventBus.addListener(this::clientThings);
        SpawnRestriction.register(RIPPLER_ENTITY_TYPE, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING,RipplerEntity::isValidNaturalSpawn);
        DispenserBehavior.registerDefaults();
    }


    @SubscribeEvent
    public void entityAttributeEventThing(EntityAttributeCreationEvent entityAttributeCreationEvent){
        entityAttributeCreationEvent.put(RIPPLER_ENTITY_TYPE,RipplerEntity.createRipplerAttributes().build());
    }

    @SubscribeEvent
    public void clientThings(FMLClientSetupEvent clientSetupEvent){
        RenderingRegistry.registerEntityRenderingHandler(RIPPLER_ENTITY_TYPE,new RipplerEntityRenderer.Factory());
    }




}
