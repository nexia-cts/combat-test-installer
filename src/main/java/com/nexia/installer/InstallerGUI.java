package com.nexia.installer;

import com.nexia.installer.util.InstallerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class InstallerGUI extends JFrame implements ActionListener {
    public static InstallerGUI instance;
    private JLabel infoLabel;

    private JLabel titleLabel;
    private JFrame frame;
    private JPanel panel;

    public InstallerGUI() {
        frame = new JFrame();
        panel = new JPanel();

        infoLabel = new JLabel(Main.BUNDLE.getString("installer.info"));
        titleLabel = new JLabel(Main.BUNDLE.getString("installer.title"));
        titleLabel.setHorizontalTextPosition(JLabel.TOP);
        titleLabel.setVerticalTextPosition(JLabel.TOP);
        infoLabel.setHorizontalTextPosition(JLabel.CENTER);
        infoLabel.setVerticalTextPosition(JLabel.CENTER);

        JButton button = new JButton(Main.BUNDLE.getString("installer.button.install"));

        button.addActionListener(this);

        //panel.setBorder(BorderFactory.createEmptyBorder(100, 30, 10, 315));
        panel.setPreferredSize(new Dimension(315, 250));
        //panel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));

        panel.add(titleLabel);
        panel.add(infoLabel);

        panel.add(button);


        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setTitle(Main.BUNDLE.getString("installer.title"));
        frame.pack();
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png")));
        frame.setVisible(true);

        instance = this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            InstallerUtils.installTest();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
