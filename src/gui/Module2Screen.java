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

public class Module2Screen implements ActionListener {
    String imgURL = "favicon-16x16.png";
    JFrame frame;
    JButton goBack;
    Locale lang;
    boolean isAppProtected;
    final long moduleFeature = 1200;
    Features features = new Features(moduleFeature);
    Color bg = new Color(153, 153, 255);
    boolean custom;
    Features demoFeature;
    ArrayList<Feature> featureList = new ArrayList<>();

    public Module2Screen(Locale lang, boolean isAppProtected, boolean custom, Features demoFeature) throws Error {
        // System.out.println("--> starting Module 2");
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

            prepareGUI();
        } catch (Error e1) {
            // TODO Auto-generated catch block
            System.out.println("Module 2 deu erro  ------------------------------------");
            e1.printStackTrace();
        }
    }

    private void prepareGUI() throws Error {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.messages", lang);
        boolean isFeatureAvailable = isAppProtected ? features.login() : true;

        Feature f1000 = new Feature().getByIdentifier(featureList, "1000");
        Feature f1200 = new Feature().getByIdentifier(featureList, "1200");

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
        Label l1 = new Label(custom ? f1200.getFeatureName() : bundle.getString("module2.title2"));
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
        goBack = new JButton(bundle.getString("module2.button.back"));
        goBack.addActionListener(this);
        p3.add(goBack);
        frame.getContentPane().add(p3, BorderLayout.SOUTH);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        closeModule();
    }

    public void closeModule() {
        // System.out.println("fechando modulo");
        features.logout();
        frame.setVisible(false);
        new MainScreen(lang, isAppProtected, custom, demoFeature);
    }

}
