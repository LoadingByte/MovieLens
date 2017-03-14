
package de.unratedfilms.movielens.fmlmod.main;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import de.unratedfilms.movielens.fmlmod.keys.KeyBindings;
import de.unratedfilms.movielens.fmlmod.keys.KeyHandler;
import de.unratedfilms.movielens.shared.Consts;

@Mod (modid = Consts.MOD_ID, version = Consts.MOD_VERSION)
public class MovieLensFmlmod {

    @EventHandler
    public void init(FMLInitializationEvent event) {

        // Initialize the key bindings
        KeyBindings.initialize();
        FMLCommonHandler.instance().bus().register(new KeyHandler());
    }

}
