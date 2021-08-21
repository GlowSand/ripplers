package glowsand.ripplers;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RipplersConfig {
    public List<String> biomes;
    public int weight;
    public int groupSizeMin;
    public int groupSizeMax;
    public boolean isDefaultSettings;
    public RipplersConfig(List<String> biomes, int weight, int groupSizeMin, int groupSizeMax){
        this.biomes=biomes;
        this.weight=weight;
        this.groupSizeMin=groupSizeMin;
        this.groupSizeMax=groupSizeMax;
        this.isDefaultSettings = false;
    }

    public RipplersConfig(List<String> biomes, int weight, int groupSizeMin, int groupSizeMax,boolean isDefaultSettings){
        this.biomes=biomes;
        this.weight=weight;
        this.groupSizeMin=groupSizeMin;
        this.groupSizeMax=groupSizeMax;
        this.isDefaultSettings = isDefaultSettings;
    }

    public static RipplersConfig getDefaultConfig(){
        List<String> ids = new ArrayList<>();
        for (Map.Entry<RegistryKey<Biome>, Biome> biomeEntry : BuiltinRegistries.BIOME.getEntries()) {
            if (biomeEntry.getValue().getCategory().equals(Biome.Category.THEEND) && !biomeEntry.getKey().equals(BiomeKeys.THE_END)){
                ids.add(biomeEntry.getKey().getValue().toString());
            }
        }
        return new RipplersConfig(ids,7,2,10,true);
    }
}
