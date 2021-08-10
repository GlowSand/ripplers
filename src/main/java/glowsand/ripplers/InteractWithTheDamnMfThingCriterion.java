package glowsand.ripplers;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InteractWithTheDamnMfThingCriterion extends AbstractCriterion<InteractWithTheDamnMfThingCriterion.Conditions> {
    static final Identifier ID = Ids.SETSPAWN_CRITERA;



    @Override
    public Identifier getId() {
        return ID;
    }


    @Override
    protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(playerPredicate);
    }


    public void trigger(ServerPlayerEntity playerEntity){
        this.trigger(playerEntity,(conditions)-> true);
    }



    public static class Conditions extends AbstractCriterionConditions {


        public Conditions(EntityPredicate.Extended playerPredicate) {
            super(InteractWithTheDamnMfThingCriterion.ID, playerPredicate);
        }

        @Override
        public Identifier getId() {
            return InteractWithTheDamnMfThingCriterion.ID;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            return super.toJson(predicateSerializer);
        }
    }
}
