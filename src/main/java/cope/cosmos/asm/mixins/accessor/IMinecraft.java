package cope.cosmos.asm.mixins.accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface IMinecraft {

	@Accessor("session")
	void setSession(Session session);

	@Accessor("rightClickDelayTimer")
	void setRightClickDelayTimer(int rightClickDelayTimer);

	@Accessor("timer")
	Timer getTimer();
}
