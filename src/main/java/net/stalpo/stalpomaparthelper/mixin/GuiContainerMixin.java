package net.stalpo.stalpomaparthelper.mixin;

import net.minecraft.client.gui.screen.ingame.CartographyTableScreen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.screen.CartographyTableScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.stalpo.stalpomaparthelper.StalpoMapartHelper;
import net.stalpo.stalpomaparthelper.MapartShulker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreens.class)
public class GuiContainerMixin {

    @Inject(method="open", at=@At("HEAD"), cancellable = true)
    private static void checkChestScreen(ScreenHandlerType type, MinecraftClient client, int any, Text component, CallbackInfo ci) {
        StalpoMapartHelper.LOG("Trying to open container: "+type+" with name "+component.getString());
        if (type == ScreenHandlerType.SHULKER_BOX) {
            ShulkerBoxScreenHandler container = ScreenHandlerType.SHULKER_BOX.create(any, client.player.getInventory());
            client.player.currentScreenHandler = container;
            MapartShulker.sh = container;
            ShulkerBoxScreen screen = new ShulkerBoxScreen(container, client.player.getInventory(), component);
            client.setScreen(screen);
            if(StalpoMapartHelper.mapCopierToggled){
                MapartShulker.copyShulkerCheck(client);
            }else if(StalpoMapartHelper.mapLockerToggled){
                MapartShulker.lockShulkerCheck(client);
            }
            ci.cancel();
        } else if(type == ScreenHandlerType.CRAFTING){
            CraftingScreenHandler container = ScreenHandlerType.CRAFTING.create(any, client.player.getInventory());
            client.player.currentScreenHandler = container;
            MapartShulker.sh = container;
            CraftingScreen screen = new CraftingScreen(container, client.player.getInventory(), component);
            client.setScreen(screen);
            if(StalpoMapartHelper.mapCopierToggled){
                MapartShulker.copyMaps(client);
            }
            ci.cancel();
        } else if(type == ScreenHandlerType.CARTOGRAPHY_TABLE){
            CartographyTableScreenHandler container = ScreenHandlerType.CARTOGRAPHY_TABLE.create(any, client.player.getInventory());
            client.player.currentScreenHandler = container;
            MapartShulker.sh = container;
            CartographyTableScreen screen = new CartographyTableScreen(container, client.player.getInventory(), component);
            client.setScreen(screen);
            if(StalpoMapartHelper.mapLockerToggled){
                MapartShulker.lockMaps(client);
            }
            ci.cancel();
        }
    }
}
