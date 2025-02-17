package cope.cosmos.asm.mixins;

import cope.cosmos.client.Cosmos;
import cope.cosmos.client.events.EntityCollisionEvent;
import cope.cosmos.client.events.TravelEvent;
import cope.cosmos.client.events.WaterCollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {
    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(float strafe, float vertical, float forward, CallbackInfo info) {
        TravelEvent travelEvent = new TravelEvent(strafe, vertical, forward);
        Cosmos.EVENT_BUS.dispatch(travelEvent);

        if (travelEvent.isCanceled()) {
            move(MoverType.SELF, motionX, motionY, motionZ);
            info.cancel();
        }
    }

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void applyEntityCollision(Entity entity, CallbackInfo info) {
        EntityCollisionEvent entityCollisionEvent = new EntityCollisionEvent();
        Cosmos.EVENT_BUS.dispatch(entityCollisionEvent);

        if (entityCollisionEvent.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "isPushedByWater()Z", at = @At("HEAD"), cancellable = true)
    public void isPushedByWater(CallbackInfoReturnable<Boolean> info) {
        WaterCollisionEvent waterCollisionEvent = new WaterCollisionEvent();
        Cosmos.EVENT_BUS.dispatch(waterCollisionEvent);

        if (waterCollisionEvent.isCanceled()) {
            info.cancel();
            info.setReturnValue(false);
        }
    }
}
