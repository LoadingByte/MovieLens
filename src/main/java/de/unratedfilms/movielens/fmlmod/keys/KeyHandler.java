
package de.unratedfilms.movielens.fmlmod.keys;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import de.unratedfilms.movielens.fmlmod.gui.FormatConfigScreen;

public class KeyHandler {

    // We catch keyboard events AND mouse events in case the key has been set to a mouse button
    @SubscribeEvent
    public void onKeyInput(InputEvent event) {

        if (KeyBindings.CONFIG.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new FormatConfigScreen(null));
        }
    }

}
