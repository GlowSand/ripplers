package glowsand.ripplers.client.renderer;


import glowsand.ripplers.entity.RipplerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;


public class RipplerEntityRenderer extends GeoEntityRenderer<RipplerEntity>  {
    public RipplerEntityRenderer(EntityRenderDispatcher ctx, AnimatedGeoModel<RipplerEntity> modelProvider) {
        super(ctx, modelProvider);
        this.shadowRadius=.5f;
    }
}
