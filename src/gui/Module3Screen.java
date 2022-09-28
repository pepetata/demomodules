package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jdom2.JDOMException;

import api.Features;

public class Module3Screen implements ActionListener {
    ResourceBundle bundle;
    String imgURL = "favicon-16x16.png";
    JFrame frame;
    JButton goBack;
    JButton function1;
    JButton function2;
    Locale lang;
    boolean isAppProtected;
    final String moduleFeature = "1300";
    final String function1Feature = "1310";
    final String function2Feature = "1300";
    Features features = new Features();

    public Module3Screen(Locale lang, boolean isAppProtected) throws Exception {
        this.lang = lang;
        this.isAppProtected = isAppProtected;
        prepareGUI();
    }

    private void prepareGUI() throws JDOMException, IOException {
        bundle = ResourceBundle.getBundle("resources.messages", lang);

        String imgURL = "favicon-16x16.png";
        frame = new JFrame(bundle.getString("main.title"));
        frame.setIconImage(new ImageIcon(imgURL).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 200);

        JPanel p1 = new JPanel();
        BoxLayout boxlayout = new BoxLayout(p1, BoxLayout.Y_AXIS);
        p1.setLayout(boxlayout);
        p1.setBorder(new EmptyBorder(new Insets(10, 20, 0, 0)));
        Label l1 = new Label(bundle.getString("module3.title2"));
        l1.setFont(new Font("Serif", Font.BOLD + Font.PLAIN, 26));
        l1.setForeground(Color.red);
        p1.add(l1, BorderLayout.NORTH);

        boolean isFeatureAvailable = features.isFeatureAvailable(isAppProtected, moduleFeature);
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
        function1 = new JButton(bundle.getString("module3.button.function1"));
        function1.addActionListener(this);
        p3.add(function1);

        function2 = new JButton(bundle.getString("module3.button.function2"));
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
            frame.setVisible(false);
            new MainScreen(lang, isAppProtected);
        }
        try {
            if (e.getSource() == function1) {
                if (features.isFeatureAvailable(isAppProtected, function1Feature) == false)
                    JOptionPane.showMessageDialog(frame, bundle.getString("module.function.notavailable1")
                            + bundle.getString("module.function.notavailable2"));
                else if (isAppProtected)
                    JOptionPane.showMessageDialog(frame,
                            bundle.getString("module.function.available") + bundle.getString("module3.function1"));
                else
                    JOptionPane.showMessageDialog(frame, bundle.getString("module3.function1"));
            }
            if (e.getSource() == function2) {
                if (features.isFeatureAvailable(isAppProtected, function2Feature) == false)
                    JOptionPane.showMessageDialog(frame, bundle.getString("module.function.notavailable1")
                            + bundle.getString("module.function.notavailable2"));
                else if (isAppProtected)
                    JOptionPane.showMessageDialog(frame,
                            bundle.getString("module.function.available") + bundle.getString("module3.function2"));
                else
                    JOptionPane.showMessageDialog(frame, bundle.getString("module3.function1"));
            }
        } catch (HeadlessException | JDOMException | IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
