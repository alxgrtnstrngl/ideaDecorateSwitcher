package by.vkatz;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by vKatz on 06.11.2017
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
        ((DecorateModeSwitcher) ActionManager.getInstance()
                .getAction("by.vkatz.decorate_mode_switcher"))
                .setMode(project, PropertiesComponent.getInstance()
                        .getBoolean(Configuration.AUTO_RUN, false));
    }

    public void projectClosed() {
    }
}
