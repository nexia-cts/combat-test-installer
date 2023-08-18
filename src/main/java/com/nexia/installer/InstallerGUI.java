package com.nexia.installer;

import com.nexia.installer.util.InstallerHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InstallerGUI extends JFrame {
    public static InstallerGUI instance;

    private InstallerHelper helper = new InstallerHelper();

    public InstallerGUI() {
        JPanel panel = helper.setPanel(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        add(panel);
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
        gui.updateSize(true);
        gui.setTitle(Main.BUNDLE.getString("installer.title"));
        gui.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png")));
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
    }

    public void updateSize(boolean updateMinimum) {
        if (updateMinimum) setMinimumSize(null);
        setPreferredSize(null);
        pack();
        Dimension size = getPreferredSize();
        if (updateMinimum) setMinimumSize(size);
        setPreferredSize(new Dimension(Math.max(450, size.width), size.height));
        setSize(getPreferredSize());
    }
}
