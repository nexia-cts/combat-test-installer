package com.nexia.installer;

import com.nexia.installer.util.InstallerHelper;
import com.nexia.installer.util.fabric.FabricInstallerHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InstallerGUI extends JFrame {
    public static InstallerGUI instance;

    public JPanel vanilla;

    public JPanel fabric;

    public JTabbedPane pane;

    public InstallerGUI() {
        this.vanilla = new InstallerHelper().setPanel(this);
        this.fabric = new FabricInstallerHelper().setPanel(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        instance = this;
    }

    public static void selectInstallLocation(Supplier<String> initalDir, Consumer<String> selectedDir) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(initalDir.get()));
        chooser.setDialogTitle(Main.BUNDLE.getString("installer.prompt.select.location"));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedDir.accept(chooser.getSelectedFile().getAbsolutePath());
        }
    }


    public static void load() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String lafCls = UIManager.getSystemLookAndFeelClassName();
        UIManager.setLookAndFeel(lafCls);

        if (lafCls.endsWith("AquaLookAndFeel")) {
            UIManager.put("TabbedPane.foreground", Color.BLACK);
        }

        InstallerGUI gui = new InstallerGUI();

        gui.pane = new JTabbedPane(JTabbedPane.TOP);

        gui.pane.addTab(Main.BUNDLE.getString("installer.tab.vanilla"), gui.vanilla);
        gui.pane.addTab(Main.BUNDLE.getString("installer.tab.fabric"), gui.fabric);

        gui.setContentPane(gui.pane);

        gui.updateSize(true);
        gui.setTitle(Main.BUNDLE.getString("installer.title"));

        gui.setIconImage(Main.icon);
        gui.setTaskBarImage(Main.icon);

        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
    }

    private void setTaskBarImage(Image image) {
        try {
            // Only supported in Java 9 +
            Class<?> taskbarClass = Class.forName("java.awt.Taskbar");
            Method getTaskbar = taskbarClass.getDeclaredMethod("getTaskbar");
            Method setIconImage = taskbarClass.getDeclaredMethod("setIconImage", Image.class);
            Object taskbar = getTaskbar.invoke(null);
            setIconImage.invoke(taskbar, image);
        } catch (Exception e) {
            // Ignored, running on Java 8
        }
    }

    private void updateSize(boolean updateMinimum) {
        if (updateMinimum) setMinimumSize(null);
        setPreferredSize(null);
        pack();
        Dimension size = getPreferredSize();
        if (updateMinimum) setMinimumSize(size);
        setPreferredSize(new Dimension(Math.max(450, size.width), size.height));
        setSize(getPreferredSize());
    }
}
