package com.nexia.installer;

import com.nexia.installer.util.InstallerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class InstallerGUI extends JFrame implements ActionListener {

    public static InstallerGUI instance;

    private JTabbedPane contentPane;

    private JLabel label;
    private JFrame frame;

    private JPanel panel;

    private void addComponents() {
        contentPane = new JTabbedPane(JTabbedPane.TOP);
        //Main.HANDLERS.forEach(handler -> contentPane.addTab(Utils.BUNDLE.getString("tab." + handler.name().toLowerCase(Locale.ROOT)), handler.makePanel(this)));
    }

    public InstallerGUI() {
        frame = new JFrame();
        panel = new JPanel();

        label = new JLabel("installer.info");
        JButton button = new JButton("installer.test");

        button.addActionListener(this);

        panel.setBorder(BorderFactory.createEmptyBorder(175, 30, 10, 315));
        panel.setLayout(new GridLayout(0, 1));

        panel.add(button);
        panel.add(label);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("installer.gui");
        frame.pack();
        frame.setVisible(true);

        instance = this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("lesgooo its been clicked bro");
        try {
            InstallerUtils.installTest();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
