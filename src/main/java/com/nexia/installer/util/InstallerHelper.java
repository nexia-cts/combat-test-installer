package com.nexia.installer.util;

import com.nexia.installer.InstallerGUI;
import com.nexia.installer.Main;
import com.nexia.installer.game.VersionHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InstallerHelper {
    public static JButton buttonInstall;

    public JComboBox<String> gameVersionComboBox;
    public JTextField installLocation;
    public JButton selectFolderButton;

    private JPanel panel;

    public static JCheckBox createProfile;

    public JPanel setPanel(InstallerGUI gui) {
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(4, 4, 4, 4));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 4, 6, 4);
        c.gridx = c.gridy = 0;

        addRow(panel, c, "installer.prompt.game.version",
                gameVersionComboBox = new JComboBox<>(),
                createSpacer()
        );

        for(VersionHandler.GameVersion version : VersionHandler.versions) {
            gameVersionComboBox.addItem(version.getVersion());
        }

        addRow(panel, c, "installer.prompt.select.location",
                installLocation = new JTextField(20),
                selectFolderButton = new JButton());
        selectFolderButton.setText("...");
        selectFolderButton.setPreferredSize(new Dimension(installLocation.getPreferredSize().height, installLocation.getPreferredSize().height));
        selectFolderButton.addActionListener(e -> InstallerGUI.selectInstallLocation(() -> installLocation.getText(), s -> installLocation.setText(s)));

        addRow(panel, c, null,
                createProfile = new JCheckBox(Main.BUNDLE.getString("installer.option.create.profile"), true));

        installLocation.setText(InstallerUtils.findDefaultInstallDir().toString());

        addLastRow(panel, c, null,
                buttonInstall = new JButton(Main.BUNDLE.getString("installer.button.install")));
        buttonInstall.addActionListener(e -> {
            buttonInstall.setEnabled(false);
            try {
                install();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return panel;
    }

    protected static Component createSpacer() {
        return Box.createRigidArea(new Dimension(4, 0));
    }
    protected void addRow(Container parent, GridBagConstraints c, String label, Component... components) {
        addRow(parent, c, false, label, components);
    }

    protected void addLastRow(Container parent, GridBagConstraints c, String label, Component... components) {
        addRow(parent, c, true, label, components);
    }

    public void addRow(Container parent, GridBagConstraints c, boolean last, String label, Component... components) {
        if (label != null) {
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.LINE_END;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0;
            parent.add(new JLabel(Main.BUNDLE.getString(label)), c);
            c.gridx++;
            c.anchor = GridBagConstraints.LINE_START;
            c.fill = GridBagConstraints.HORIZONTAL;
        } else {
            c.gridwidth = 2;
            if (last) c.weighty = 1;
            c.anchor = last ? GridBagConstraints.PAGE_START : GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.NONE;
        }

        c.weightx = 1;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        for (Component comp : components) {
            panel.add(comp);
        }

        parent.add(panel, c);

        c.gridy++;
        c.gridx = 0;
    }

    private void install() throws IOException {

        String stringGameVersion = (String) gameVersionComboBox.getSelectedItem();
        VersionHandler.GameVersion gameVersion = VersionHandler.identifyGameVersion(stringGameVersion);
        if(gameVersion == null) return;

        Path mcPath = Paths.get(installLocation.getText());

        if (!Files.exists(mcPath)) {
            throw new RuntimeException(Main.BUNDLE.getString("installer.exception.no.launcher.directory"));
        }

        InstallerUtils.install(mcPath, gameVersion);
    }
}
