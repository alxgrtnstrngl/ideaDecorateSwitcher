package by.vkatz;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by vKatz on 02.03.2015.
 */
public class Configuration implements Configurable {
    public static final String AUTO_RUN = "by.vkatz.autoundecorate";
    private JCheckBox auto_run;

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
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        auto_run = new JCheckBox("Auto undecorate on start");
        panel.add(auto_run);
        PropertiesComponent properties = PropertiesComponent.getInstance();
        auto_run.setSelected(properties.getBoolean(AUTO_RUN, false));
        return panel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue(AUTO_RUN, String.valueOf(auto_run.isSelected()));
    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }
}
