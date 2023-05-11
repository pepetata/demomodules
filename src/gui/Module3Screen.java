package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import api.Features;
import api.EMS;
import api.Feature;

public class Module3Screen implements ActionListener {
    ResourceBundle bundle;
    String imgURL = "favicon-16x16.png";
    JFrame frame;
    JButton goBack;
    JButton function1;
    JButton function2;
    Locale lang;
    boolean isAppProtected;
    final long moduleFeature = 1300;
    final long function1Feature = 1310;
    final long function2Feature = 1320;
    Features features = new Features(moduleFeature);
    Features featuresFuntion1 = new Features(function1Feature);
    Features featuresFuntion2 = new Features(function2Feature);
    Color bg = new Color(255, 204, 128);
    boolean custom;
    Features demoFeature;
    ArrayList<Feature> featureList = new ArrayList<>();
    Feature f1000;
    Feature f1300;
    Feature f1310;
    Feature f1320;

    public Module3Screen(Locale lang, boolean isAppProtected, boolean custom, Features demoFeature) throws Error {
        // System.out.println("--> starting Module 3");
        try {
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
            f1000 = new Feature().getByIdentifier(featureList, "1000");
            f1300 = new Feature().getByIdentifier(featureList, "1300");
            f1310 = new Feature().getByIdentifier(featureList, "1310");
            f1320 = new Feature().getByIdentifier(featureList, "1320");

            prepareGUI();
        } catch (Error e1) {
            // TODO Auto-generated catch block
            System.out.println("Module 3 deu erro  ------------------------------------");
            e1.printStackTrace();
        }
    }

    private void prepareGUI() throws Error {
        bundle = ResourceBundle.getBundle("resources.messages", lang);
        boolean isFeatureAvailable = isAppProtected ? features.login() : true;

        String imgURL = "favicon-16x16.png";
        frame = new JFrame(custom ? f1000.getFeatureName() : bundle.getString("main.title"));
        frame.setIconImage(new ImageIcon(imgURL).getImage());
        frame.setSize(750, 200);
        frame.getContentPane().setBackground(bg);
        // close module and go back to main if clicks to close
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeModule();
            }
        });

        JPanel p1 = new JPanel();
        BoxLayout boxlayout = new BoxLayout(p1, BoxLayout.Y_AXIS);
        p1.setLayout(boxlayout);
        p1.setBorder(new EmptyBorder(new Insets(10, 20, 0, 0)));
        p1.setBackground(bg);
        Label l1 = new Label(custom ? f1300.getFeatureName() : bundle.getString("module3.title2"));
        l1.setFont(new Font("Serif", Font.BOLD + Font.PLAIN, 26));
        l1.setForeground(Color.red);
        p1.add(l1, BorderLayout.NORTH);

        if (isFeatureAvailable == false) {
            Label l2 = new Label(bundle.getString("module.notavailable1"));
            l2.setFont(new Font("Serif", Font.BOLD + Font.PLAIN, 18));
            l2.setBackground(Color.yellow);
            p1.add(l2, BorderLayout.NORTH);

            Label l3 = new Label(bundle.getString("module.notavailable2"));
            l3.setFont(new Font("Serif", Font.BOLD + Font.PLAIN, 14));
            l3.setForeground(Color.red);
            l3.setBackground(Color.yellow);
            p1.add(l3, BorderLayout.NORTH);
        } else if (isAppProtected) {
            Label l2 = new Label(bundle.getString("module.available"));
            l2.setFont(new Font("Serif", Font.BOLD + Font.PLAIN, 18));
            // l2.setBackground(Color.yellow);
            p1.add(l2, BorderLayout.NORTH);

        }

        frame.getContentPane().add(p1, BorderLayout.NORTH);

        JPanel p3 = new JPanel();
        p3.setBackground(bg);
        function1 = new JButton(custom ? f1310.getFeatureName() : bundle.getString("module3.button.function1"));
        function1.addActionListener(this);
        p3.add(function1);

        function2 = new JButton(custom ? f1320.getFeatureName() : bundle.getString("module3.button.function2"));
        function2.addActionListener(this);
        p3.add(function2);

        goBack = new JButton(bundle.getString("module3.button.back"));
        goBack.addActionListener(this);
        p3.add(goBack);

        frame.getContentPane().add(p3, BorderLayout.SOUTH);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goBack) {
            closeModule();
        }
        try {
            if (e.getSource() == function1) {
                if (isAppProtected == false)
                    JOptionPane.showMessageDialog(frame,
                            custom ? f1310.getFeatureDescription() : bundle.getString("module3.function1"));
                else if (featuresFuntion1.login() == false)
                    JOptionPane.showMessageDialog(frame, bundle.getString("module.function.notavailable1")
                            + bundle.getString("module.function.notavailable2"));
                else
                    JOptionPane.showMessageDialog(frame,
                            bundle.getString("module.function.available")
                                    + (custom ? f1310.getFeatureDescription() : bundle.getString("module3.function1")));
                featuresFuntion1.logout();
            }

            if (e.getSource() == function2) {
                if (isAppProtected == false)
                    JOptionPane.showMessageDialog(frame,
                            custom ? f1320.getFeatureDescription() : bundle.getString("module3.function2"));
                else if (featuresFuntion2.login() == false)
                    JOptionPane.showMessageDialog(frame, bundle.getString("module.function.notavailable1")
                            + bundle.getString("module.function.notavailable2"));
                else
                    JOptionPane.showMessageDialog(frame,
                            bundle.getString("module.function.available")
                                    + (custom ? f1320.getFeatureDescription() : bundle.getString("module3.function2")));
                featuresFuntion2.logout();
            }
        } catch (HeadlessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void closeModule() {
        // System.out.println("fechando modulo");
        features.logout();
        featuresFuntion1.logout();
        featuresFuntion2.logout();
        frame.setVisible(false);
        new MainScreen(lang, isAppProtected, custom, demoFeature);
    }

}
