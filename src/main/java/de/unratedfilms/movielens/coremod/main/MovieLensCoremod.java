
package de.unratedfilms.movielens.coremod.main;

import java.util.Map;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import de.unratedfilms.movielens.coremod.asm.MainClassTransformer;
import de.unratedfilms.movielens.shared.Consts;

@IFMLLoadingPlugin.Name (Consts.MOD_ID)
@IFMLLoadingPlugin.TransformerExclusions ("de.unratedfilms." + Consts.MOD_ID + ".coremod.")
public class MovieLensCoremod implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {

        return new String[] { MainClassTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {

        return null;
    }

    @Override
    public String getSetupClass() {

        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {

        return null;
    }

}
