package glowsand.ripplers;

import glowsand.ripplers.block.TrimmedChorusBlock;
import glowsand.ripplers.block.TrimmedChorusBlockEntity;
import glowsand.ripplers.entity.RipplerEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;


public class Ripplers implements ModInitializer {
    public static InteractWithTheDamnMfThingCriterion criterion;
    public static TrimTheDamnMfThingCriterion criterion2;
    public static final TrimmedChorusBlock TRIMMED_CHORUS_BLOCK =  new TrimmedChorusBlock(AbstractBlock.Settings.copy(Blocks.CHORUS_FLOWER));
    public static BlockEntityType<TrimmedChorusBlockEntity> TRIMMED_CHORUS_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(TrimmedChorusBlockEntity::new, TRIMMED_CHORUS_BLOCK).build();
    public static EntityType<RipplerEntity> RIPPLER_ENTITY_TYPE = FabricEntityTypeBuilder.create(SpawnGroup.CREATURE,RipplerEntity::new).dimensions(EntityDimensions.fixed(0.6F,0.5F)).trackRangeBlocks(8).build();
    @Override
    public void onInitialize() {
        criterion= CriterionRegistry.register(new InteractWithTheDamnMfThingCriterion());
        criterion2=CriterionRegistry.register(new TrimTheDamnMfThingCriterion());
        Registry.register(Registry.ENTITY_TYPE,Ids.RIPPLER_ENTITY,RIPPLER_ENTITY_TYPE);
        Registry.register(Registry.BLOCK_ENTITY_TYPE,Ids.TRIMMED_CHORUS_FLOWER_ENTITY,TRIMMED_CHORUS_BLOCK_ENTITY);
        Registry.register(Registry.BLOCK,Ids.TRIMMED_CHORUS_FLOWER,TRIMMED_CHORUS_BLOCK);
        Registry.register(Registry.ITEM,Ids.TRIMMED_CHORUS_FLOWER,new BlockItem(TRIMMED_CHORUS_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        FabricDefaultAttributeRegistry.register(RIPPLER_ENTITY_TYPE,RipplerEntity.createRipplerAttributes());
        Registry.register(Registry.ITEM,Ids.RIPPLER_SPAWN_EGG,new SpawnEggItem(RIPPLER_ENTITY_TYPE,2888732, 3423236, (new Item.Settings()).group(ItemGroup.MISC)));
        Registry.register(Registry.SOUND_EVENT,Ids.CHORUS_HEART_BEAT,new SoundEvent(Ids.CHORUS_HEART_BEAT));
        Registry.register(Registry.SOUND_EVENT,Ids.CHORUS_TRIM,new SoundEvent(Ids.CHORUS_TRIM));
        Registry.register(Registry.SOUND_EVENT,Ids.RIPPLER_HURT_SOUND,new SoundEvent(Ids.RIPPLER_HURT_SOUND));
        Registry.register(Registry.SOUND_EVENT,Ids.RIPPLER_DEATH_SOUND,new SoundEvent(Ids.RIPPLER_DEATH_SOUND));
        BiomeModifications.addSpawn((context)-> context.getBiome().getCategory().equals(Biome.Category.THEEND) && !context.getBiomeKey().equals(BiomeKeys.THE_END),SpawnGroup.CREATURE,RIPPLER_ENTITY_TYPE,7,2,10);
        SpawnRestrictionAccessor.callRegister(RIPPLER_ENTITY_TYPE, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING,RipplerEntity::isValidNaturalSpawn);
        DispenserBehavior.registerDefaults();

    }
}
