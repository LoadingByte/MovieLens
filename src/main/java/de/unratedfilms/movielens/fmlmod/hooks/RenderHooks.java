
package de.unratedfilms.movielens.fmlmod.hooks;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import de.unratedfilms.movielens.fmlmod.config.Config;

public class RenderHooks {

    private static final Minecraft MC = Minecraft.getMinecraft();

    public static void adjustViewport() {

        if (!Config.enabled) {
            return;
        }

        double virtualFormat = Config.selectedFormat.getAspectRatio();

        int physicalWidth = MC.displayWidth;
        int physicalHeight = MC.displayHeight;
        double physicalFormat = (double) physicalWidth / physicalHeight;

        // Letterbox needed
        if (virtualFormat > physicalFormat) {
            int virtualHeight = (int) (physicalWidth / virtualFormat);
            int boxHeight = (physicalHeight - virtualHeight) / 2; // height of each of the two boxes

            GL11.glViewport(0, boxHeight, physicalWidth, virtualHeight);
        }
        // Pillarbox needed
        else if (virtualFormat < physicalFormat) {
            int virtualWidth = (int) (physicalHeight * virtualFormat);
            int boxWidth = (physicalWidth - virtualWidth) / 2; // width of each of the two boxes

            GL11.glViewport(boxWidth, 0, virtualWidth, physicalHeight);
            GL11.glScissor(boxWidth, 0, virtualWidth, physicalHeight);
        }
    }

    public static float getAspectRatio() {

        return Config.selectedFormat.getAspectRatio();
    }

    public static void renderBlackBars() {

        if (!Config.enabled) {
            return;
        }

        // Setup orthographic projection for 2D rendering [mostly copied from EntityRenderer.setupOverlayRendering()]
        {
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, MC.displayWidth, MC.displayHeight, 0, 1000, 3000);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0, 0, -2000);
        }

        double virtualFormat = Config.selectedFormat.getAspectRatio();

        int physicalWidth = MC.displayWidth;
        int physicalHeight = MC.displayHeight;
        double physicalFormat = (double) physicalWidth / physicalHeight;

        // Letterbox needed
        if (virtualFormat > physicalFormat) {
            int virtualHeight = (int) (physicalWidth / virtualFormat);
            int boxHeight = (physicalHeight - virtualHeight) / 2 + 1; // height of each of the two boxes, +1 to catch rounding errors

            Gui.drawRect(0, 0, physicalWidth, boxHeight, 0xFF_00_00_00);
            Gui.drawRect(0, physicalHeight - boxHeight, physicalWidth, physicalHeight, 0xFF_00_00_00);
        }
        // Pillarbox needed
        else if (virtualFormat < physicalFormat) {
            int virtualWidth = (int) (physicalHeight * virtualFormat);
            int boxWidth = (physicalWidth - virtualWidth) / 2 + 1; // width of each of the two boxes, +1 to catch rounding errors

            Gui.drawRect(0, 0, boxWidth, physicalHeight, 0xFF_00_00_00);
            Gui.drawRect(physicalWidth - boxWidth, 0, physicalWidth, physicalHeight, 0xFF_00_00_00);
        }
    }

    private RenderHooks() {}

}
