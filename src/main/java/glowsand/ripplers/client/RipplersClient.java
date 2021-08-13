package glowsand.ripplers.client;

import glowsand.ripplers.Ripplers;
import glowsand.ripplers.client.renderer.RipplerEntityModel;
import glowsand.ripplers.client.renderer.RipplerEntityRenderer;
import i.am.cal.antisteal.Antisteal;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.renderer.registry.EntityRendererRegistryImpl;
import net.fabricmc.loader.ModContainer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class RipplersClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistryImpl.INSTANCE.register(Ripplers.RIPPLER_ENTITY_TYPE, ctx ->
                new RipplerEntityRenderer(ctx, new RipplerEntityModel()));

        // Antisteal Generated Code

        @SuppressWarnings("OptionalGetWithoutIsPresent") ModContainer mC = (ModContainer) FabricLoader.getInstance().getModContainer("ripplers").get();
        Path pTM = null;
        try {
            pTM = Paths.get(mC.getOriginUrl().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("Curseforge", "https://www.curseforge.com/minecraft/mc-mods/ripplers");
        hm.put("Modrinth", "https://modrinth.com/mod/ripplers");
        Antisteal.check(pTM, () -> MinecraftClient.getInstance().close(), hm, RipplersClient.class);

    }
}
