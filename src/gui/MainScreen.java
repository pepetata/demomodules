package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

public class MainScreen implements ActionListener {
    String imgURL = "favicon-16x16.png";
    JFrame frame;
    JButton mod1;
    JButton mod2;
    JButton mod3;
    Locale lang;
    boolean isAppProtected;

    public MainScreen(Locale lang, boolean isAppProtected) {
        this.lang = lang;
        this.isAppProtected = isAppProtected;
        prepareGUI();
    }

    private void prepareGUI() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.messages", lang);

        String imgURL = "favicon-16x16.png";
        frame = new JFrame(bundle.getString("main.title"));
        frame.setIconImage(new ImageIcon(imgURL).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 200);

        JPanel p1 = new JPanel();
        Label l1 = new Label(bundle.getString("main.title2"));
        Label l2 = new Label(bundle.getString("main.modules"));
        l1.setFont(new Font("Serif", Font.BOLD + Font.PLAIN, 26));
        l1.setForeground(Color.red);
        l2.setFont(new Font("Serif", Font.BOLD + Font.PLAIN, 18));
        l2.setForeground(Color.blue);
        p1.setLayout(new BorderLayout());
        // p2.setLayout(new BorderLayout());
        // p1.add(new Button("Okay"), BorderLayout.SOUTH);
        p1.add(l1, BorderLayout.NORTH);
        p1.add(l2, BorderLayout.SOUTH);
        frame.getContentPane().add(p1, BorderLayout.NORTH);
        // frame.getContentPane().add(p2, BorderLayout.SOUTH);
        // frame.getContentPane().add(BorderLayout.NORTH, p1);
        // frame.getContentPane().add(p2);
        JPanel p3 = new JPanel();
        mod1 = new JButton(bundle.getString("main.button.module1"));
        mod2 = new JButton(bundle.getString("main.button.module2"));
        mod3 = new JButton(bundle.getString("main.button.module3"));
        mod1.addActionListener(this);
        mod2.addActionListener(this);
        mod3.addActionListener(this);
        p3.add(mod1);
        p3.add(mod2);
        p3.add(mod3);
        frame.getContentPane().add(p3, BorderLayout.SOUTH);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setVisible(false);
        try {
            if (e.getSource() == mod1)
                new Module1Screen(lang, isAppProtected);
            if (e.getSource() == mod2)
                new Module2Screen(lang, isAppProtected);
            if (e.getSource() == mod3)
                new Module3Screen(lang, isAppProtected);
            // frame.setVisible(true);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
}
