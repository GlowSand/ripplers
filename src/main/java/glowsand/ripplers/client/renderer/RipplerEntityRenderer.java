package glowsand.ripplers.client.renderer;


import glowsand.ripplers.entity.RipplerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RipplerEntityRenderer extends GeoEntityRenderer<RipplerEntity>  {
    public RipplerEntityRenderer(EntityRendererFactory.Context ctx, AnimatedGeoModel<RipplerEntity> modelProvider) {
        super(ctx, modelProvider);

        this.shadowRadius=.5f;
    }
}
