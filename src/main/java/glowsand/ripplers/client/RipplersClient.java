package glowsand.ripplers.client;

import glowsand.ripplers.Ripplers;
import glowsand.ripplers.client.renderer.RipplerEntityModel;
import glowsand.ripplers.client.renderer.RipplerEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;


@Environment(EnvType.CLIENT)
public class RipplersClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(Ripplers.RIPPLER_ENTITY_TYPE, (dispatcher,ctx) ->
                new RipplerEntityRenderer(dispatcher, new RipplerEntityModel()));
    }
}
