package glowsand.ripplers.mixin;

;
import glowsand.ripplers.Ripplers;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeMixin {
    @Inject(method = "addEndMobs",at = @At("TAIL"))
    private static void endMobsMixin(SpawnSettings.Builder arg, CallbackInfo ci){
        arg.spawn(SpawnGroup.CREATURE,new SpawnSettings.SpawnEntry(Ripplers.RIPPLER_ENTITY_TYPE,7,2,10));
    }
}
