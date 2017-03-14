
package de.unratedfilms.movielens.fmlmod.gui;

import static de.unratedfilms.movielens.shared.Consts.MOD_ID;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import de.unratedfilms.guilib.core.Axis;
import de.unratedfilms.guilib.core.MouseButton;
import de.unratedfilms.guilib.integration.BasicScreen;
import de.unratedfilms.guilib.layouts.FlowLayout;
import de.unratedfilms.guilib.widgets.model.Button;
import de.unratedfilms.guilib.widgets.model.Button.FilteredButtonHandler;
import de.unratedfilms.guilib.widgets.model.Checkbox;
import de.unratedfilms.guilib.widgets.model.Container;
import de.unratedfilms.guilib.widgets.model.ContainerFlexible;
import de.unratedfilms.guilib.widgets.model.Dropdown;
import de.unratedfilms.guilib.widgets.model.Dropdown.GenericUserObjectOption;
import de.unratedfilms.guilib.widgets.model.Label;
import de.unratedfilms.guilib.widgets.view.impl.ButtonLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.CheckboxImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerAdjustingImpl;
import de.unratedfilms.guilib.widgets.view.impl.ContainerClippingImpl;
import de.unratedfilms.guilib.widgets.view.impl.DropdownLabelImpl;
import de.unratedfilms.guilib.widgets.view.impl.LabelImpl;
import de.unratedfilms.movielens.fmlmod.config.Config;
import de.unratedfilms.movielens.fmlmod.config.Format;

public class FormatConfigScreen extends BasicScreen {

    private static final int                                  PADDING = 5;

    private Container                                         mainContainer;

    private Label                                             titleLabel;
    private Checkbox                                          enabledCheckbox;
    private Button                                            closeButton;

    private Container                                         selectedFormatContainer;
    private Label                                             selectedFormatLabel;
    private Dropdown<GenericUserObjectOption<Format, String>> selectedFormatDropdown;

    public FormatConfigScreen(GuiScreen parent) {

        super(parent);
    }

    @Override
    protected void createGui() {

        ContainerFlexible rootContainer = new ContainerClippingImpl();
        setRootWidget(rootContainer);

        mainContainer = new ContainerAdjustingImpl();
        rootContainer.addWidgets(mainContainer);

        titleLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".config.title"));
        enabledCheckbox = new CheckboxImpl(I18n.format("gui." + MOD_ID + ".config.enabled")).setHandler((cb, checked) -> Config.enabled = checked);
        closeButton = new ButtonLabelImpl(I18n.format("gui." + MOD_ID + ".config.close"), new FilteredButtonHandler(MouseButton.LEFT, (b, mb) -> close()));

        selectedFormatLabel = new LabelImpl(I18n.format("gui." + MOD_ID + ".config.selectedFormat"));

        List<GenericUserObjectOption<Format, String>> options = new ArrayList<>();
        for (Format format : Format.KNOWN_FORMATS) {
            String label = String.format("%.2f:1 (%s)", format.getAspectRatio(), format.getName());
            options.add(new GenericUserObjectOption<>(format, label));
        }
        selectedFormatDropdown = new DropdownLabelImpl<>(options).setHandler((dp, option) -> Config.selectedFormat = option.getUserObject());

        selectedFormatContainer = new ContainerAdjustingImpl(selectedFormatLabel, selectedFormatDropdown);

        mainContainer.addWidgets(titleLabel, enabledCheckbox, selectedFormatContainer, closeButton);

        // ----- Load the widgets with the current config values -----

        enabledCheckbox.setChecked(Config.enabled);

        for (GenericUserObjectOption<Format, String> option : selectedFormatDropdown.getOptions()) {
            if (option.getUserObject().equals(Config.selectedFormat)) {
                selectedFormatDropdown.setSelectedOption(option);
            }
        }

        // ----- Revalidation -----

        rootContainer
                .appendLayoutManager(c -> {
                    // Center the main container on the screen
                    mainContainer.setPosition( (rootContainer.getWidth() - mainContainer.getWidth()) / 2, (rootContainer.getHeight() - mainContainer.getHeight()) / 2);
                });

        mainContainer
                .appendLayoutManager(new FlowLayout(Axis.Y, 0, 5));

        selectedFormatContainer
                .appendLayoutManager(new FlowLayout(Axis.X, 0, 5));
    }

    @Override
    public void drawBackground() {

        // We can simply query these coordinates because the main container is a direct child of the root container, meaning that the coordinates are absolute
        drawRect(mainContainer.getX() - PADDING, mainContainer.getY() - PADDING, mainContainer.getRight() + PADDING, mainContainer.getBottom() + PADDING, 0xC0_10_10_10);
    }

}
