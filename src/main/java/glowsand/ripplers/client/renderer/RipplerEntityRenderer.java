package glowsand.ripplers.client.renderer;


import glowsand.ripplers.entity.RipplerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


public class RipplerEntityRenderer extends GeoEntityRenderer<RipplerEntity>  {
    public RipplerEntityRenderer(EntityRenderDispatcher ctx, AnimatedGeoModel<RipplerEntity> modelProvider) {
        super(ctx, modelProvider);
        this.shadowRadius=.5f;
    }


    @Override
    public Identifier getTexture(RipplerEntity arg) {
        return new Identifier("ripplers:textures/entity/rippler"+ arg.getVariant()+".png");
    }

    public static class Factory implements IRenderFactory<RipplerEntity>{

        @Override
        public EntityRenderer<? super RipplerEntity> createRenderFor(EntityRenderDispatcher arg) {
            return new RipplerEntityRenderer(arg,new RipplerEntityModel());
        }
    }
}
