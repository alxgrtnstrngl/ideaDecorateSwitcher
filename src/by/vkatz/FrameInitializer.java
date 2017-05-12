package by.vkatz;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by vKatz on 02.03.2015.
 */
public class FrameInitializer implements ProjectComponent {
    private Project project;

    public FrameInitializer(Project project) {
        this.project = project;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "FrameInitializer";
    }

    public void projectOpened() {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        ((DecorateModeSwitcher) ActionManager.getInstance().getAction("by.vkatz.decorate_mode_switcher")).setMode(project, properties.getBoolean(Configuration.AUTO_RUN, false));
    }

    public void projectClosed() {
    }
}
