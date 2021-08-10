package glowsand.ripplers.client.renderer;

import glowsand.ripplers.entity.RipplerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RipplerEntityModel extends AnimatedGeoModel<RipplerEntity> {
    @Override
    public Identifier getModelLocation(RipplerEntity object) {
        return new Identifier("ripplers:geo/entity/ripplerentity.geo.json");
    }

    @Override
    public Identifier getTextureLocation(RipplerEntity object) {
        return new Identifier("ripplers:textures/entity/rippler"+ object.getVariant()+".png");
    }

    @Override
    public Identifier getAnimationFileLocation(RipplerEntity animatable) {
        return new Identifier("ripplers","animations/rippler.animation.json");
    }


    @Override
    public void setLivingAnimations(RipplerEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }

    }
}
