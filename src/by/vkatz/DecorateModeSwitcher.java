package by.vkatz;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.ScreenUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/**
 * Created by vKatz on 06.11.2017
 */
public class DecorateModeSwitcher extends AnAction {
    private boolean undecorated = false;
    private JLabel icon;
    private JPanel buttons;
    private JLabel iconOffset1;
    private JLabel iconOffset2;

    public void actionPerformed(final AnActionEvent event) {
        setMode(event.getProject(), !undecorated);
    }

    void setMode(Project project, boolean undecorated) {
        if (undecorated != this.undecorated) {
            this.undecorated = undecorated;
            update(project);
        }
    }

    private void update(final Project project) {
        IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(project);
        final JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ideFrame.getComponent());
        PropertiesComponent properties = PropertiesComponent.getInstance();
        boolean showIcon = properties.getBoolean(Configuration.DISPLAY_ICON, true);
        int iconSize = properties.getInt(Configuration.ICON_SIZE, 12);
        if (undecorated) {
            icon = new JLabel(new ImageIcon(frame.getIconImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH)));
            buttons = new JPanel();
            iconOffset1 = new JLabel(" ");
            iconOffset2 = new JLabel(" ");
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(FlowLayout.RIGHT);
            buttons.setLayout(flowLayout);
            buttons.setBackground(null);
            JLabel minimize = new JLabel(IconLoader.getIcon("/by/vkatz/ic_minimize.png"));
            minimize.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    frame.setExtendedState(Frame.ICONIFIED);
                }
            });
            JLabel decorate = new JLabel(IconLoader.getIcon("/by/vkatz/ic_decorate_mode.png"));
            decorate.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    setMode(project, !undecorated);
                }
            });
            JLabel exit = new JLabel(IconLoader.getIcon("/by/vkatz/ic_exit.png"));
            exit.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
            });
            buttons.add(minimize);
            buttons.add(decorate);
            buttons.add(exit);
            JMenuBar menu = frame.getJMenuBar();
            menu.add(buttons, menu.getMenuCount() + 1);
            if (showIcon) {
                menu.add(iconOffset1, 0);
                menu.add(icon, 1);
                menu.add(iconOffset2, 2);
            }
        } else {
            frame.getJMenuBar().remove(icon);
            frame.getJMenuBar().remove(buttons);
            frame.getJMenuBar().remove(iconOffset1);
            frame.getJMenuBar().remove(iconOffset2);
            buttons = null;
        }
        setDecorateMode(frame, undecorated);
        if (undecorated) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle rect = ge.getMaximumWindowBounds();
            frame.setBounds(0, 0, rect.width, rect.height);
        }
    }

    private static void setDecorateMode(JFrame frame, boolean state) {
        if (frame == null) return;
        GraphicsDevice device = ScreenUtil.getScreenDevice(frame.getBounds());
        if (device == null) return;
        try {
            frame.getRootPane().putClientProperty(ScreenUtil.DISPOSE_TEMPORARY, Boolean.TRUE);
            if (state) frame.getRootPane().putClientProperty("oldBounds", frame.getBounds());
            frame.dispose();
            frame.setUndecorated(state);
        } finally {
            if (state) {
                frame.setBounds(device.getDefaultConfiguration().getBounds());
            } else {
                Object o = frame.getRootPane().getClientProperty("oldBounds");
                if (o instanceof Rectangle) frame.setBounds((Rectangle) o);
            }
            frame.setVisible(true);
            frame.getRootPane().putClientProperty(ScreenUtil.DISPOSE_TEMPORARY, null);
        }
    }
}
