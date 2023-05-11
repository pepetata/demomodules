package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ArrayList;
import javax.swing.*;

import api.EMS;
import api.Feature;
import api.Features;

public class MainScreen implements ActionListener {
    String imgURL = "favicon-16x16.png";
    JFrame frame;
    JButton mod1;
    JButton mod2;
    JButton mod3;
    Locale lang;
    boolean isAppProtected;
    boolean custom;
    Features demoFeature;
    ArrayList<Feature> featureList = new ArrayList<>();

    public MainScreen(Locale lang, boolean isAppProtected, boolean custom, Features demoFeature) {
        this.lang = lang;
        this.isAppProtected = isAppProtected;
        this.custom = custom;
        this.demoFeature = demoFeature;
        // get the list of Feature
        EMS ems = new EMS();
        try {
            ems.loadProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
        featureList = ems.getFeatureList();

        prepareGUI();
    }

    private void prepareGUI() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.messages", lang);

        String imgURL = "favicon-16x16.png";

        Feature f1000 = new Feature().getByIdentifier(featureList, "1000");
        Feature f1100 = new Feature().getByIdentifier(featureList, "1100");
        Feature f1200 = new Feature().getByIdentifier(featureList, "1200");
        Feature f1300 = new Feature().getByIdentifier(featureList, "1300");
        // System.out.println();
        // System.out.println(f1000.getFeatureIdentifier());
        // System.out.println(f1000.getFeatureName());
        // System.out.println(f1000.getFeatureDescription());
        // System.out.println(f1000.getRefId1());
        // System.out.println(f1000.getRefId2());
        // System.out.println("custom "+custom);

        frame = new JFrame(custom ? f1000.getFeatureName() : bundle.getString("main.title"));
        frame.setIconImage(new ImageIcon(imgURL).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 200);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeModule();
            }
        });

        JPanel p1 = new JPanel();
        Label l1 = new Label(custom ? f1000.getFeatureDescription() : bundle.getString("main.title2"));
        Label l2 = new Label(custom ? f1000.getRefId1() : bundle.getString("main.modules"));
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
        mod1 = new JButton(custom ? f1100.getFeatureName() : bundle.getString("main.button.module1"));
        mod2 = new JButton(custom ? f1200.getFeatureName() : bundle.getString("main.button.module2"));
        mod3 = new JButton(custom ? f1300.getFeatureName() : bundle.getString("main.button.module3"));
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
                new Module1Screen(lang, isAppProtected, custom, demoFeature);
            if (e.getSource() == mod2)
                new Module2Screen(lang, isAppProtected, custom, demoFeature);
            if (e.getSource() == mod3)
                new Module3Screen(lang, isAppProtected, custom, demoFeature);
            frame.setVisible(false);
        } catch (Error e1) {
            // TODO Auto-generated catch block
            System.out.println("MainScreen --> actionPerformed ==============================");
            e1.printStackTrace();
            new MainScreen(lang, isAppProtected, custom, demoFeature);
        }
    }

    public void closeModule() {
        // System.out.println("fechando modulo");
        demoFeature.logout();
        // frame.setVisible(false);
        // new MainScreen(lang, isAppProtected, custom, demoFeature);
    }

}
