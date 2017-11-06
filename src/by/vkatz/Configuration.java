package by.vkatz;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.uiDesigner.core.Spacer;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by vKatz on 06.11.2017
 */
public class Configuration implements Configurable {
    static final String AUTO_RUN = "by.vkatz.decoratemodeswitcher.autoundecorate";
    static final String DISPLAY_ICON = "by.vkatz.decoratemodeswitcher.deispla.icon";
    static final String ICON_SIZE = "by.vkatz.decoratemodeswitcher.icon.size";
    private JCheckBox autoRun;
    private JCheckBox displayIcon;
    private ComboBox iconSize;

    @Nls
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        PropertiesComponent properties = PropertiesComponent.getInstance();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        autoRun = new JCheckBox("Auto undecorate on start");
        autoRun.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(autoRun);
        autoRun.setSelected(properties.getBoolean(AUTO_RUN, false));

        displayIcon = new JCheckBox("Display Ide icon");
        displayIcon.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(displayIcon);
        displayIcon.setSelected(properties.getBoolean(DISPLAY_ICON, true));

        JPanel sizeInputPane = new JPanel();
        sizeInputPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        sizeInputPane.setLayout(new BoxLayout(sizeInputPane, BoxLayout.X_AXIS));
        sizeInputPane.add(new JLabel("Icon size: "));
        Vector<Integer> availableIconSizes = new Vector<>();
        for (int i = 6; i <= 32; i += 2) availableIconSizes.add(i);
        iconSize = new ComboBox<>(new DefaultComboBoxModel<>(availableIconSizes));
        sizeInputPane.add(iconSize);
        iconSize.setSelectedItem(properties.getInt(ICON_SIZE, 12));
        sizeInputPane.setMaximumSize(sizeInputPane.getMinimumSize());
        panel.add(sizeInputPane);

        panel.add(new Spacer());
        panel.doLayout();

        return panel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void apply() throws ConfigurationException {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue(AUTO_RUN, String.valueOf(autoRun.isSelected()));
        properties.setValue(DISPLAY_ICON, String.valueOf(displayIcon.isSelected()));
        properties.setValue(ICON_SIZE, iconSize.getSelectedItem().toString());
    }

    @Override
    public void reset() {
    }

    @Override
    public void disposeUIResources() {
    }
}
