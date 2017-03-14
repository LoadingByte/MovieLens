
package de.unratedfilms.movielens.fmlmod.keys;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;
import de.unratedfilms.movielens.shared.Consts;

public class KeyBindings {

    public static final KeyBinding CONFIG = createKeyBinding("config", Keyboard.KEY_L);

    private static KeyBinding createKeyBinding(String name, int key) {

        return new KeyBinding("key." + Consts.MOD_ID + "." + name, key, "key.categories." + Consts.MOD_ID);
    }

    public static void initialize() {

        ClientRegistry.registerKeyBinding(CONFIG);
    }

}
