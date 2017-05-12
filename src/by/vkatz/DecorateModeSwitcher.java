package by.vkatz;

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
 * Created by vKatz on 01.03.2015.
 */
public class DecorateModeSwitcher extends AnAction {
    private boolean undecorated = false;
    private JPanel buttons;
    private JLabel icon;

    public void actionPerformed(final AnActionEvent event) {
        setMode(event.getProject(), !undecorated);
    }

    public void setMode(Project project, boolean undecorated) {
        if (undecorated != this.undecorated) {
            this.undecorated = undecorated;
            update(project);
        }
    }

    private void update(final Project project) {
        IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(project);
        final JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ideFrame.getComponent());
        if (undecorated) {
            buttons = new JPanel();
            icon = new JLabel(IconLoader.getIcon("/by/vkatz/idea_16.png"));
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
            icon.setSize(new Dimension(32, 32));
            menu.add(icon, 0);
            menu.add(buttons, menu.getMenuCount() + 1);
        } else {
            frame.getJMenuBar().remove(buttons);
            frame.getJMenuBar().remove(icon);
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
