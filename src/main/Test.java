package main;

import java.awt.*;
import javax.swing.*;

public class Test extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JRadioButton button1 = new JRadioButton();
  JRadioButton button2 = new JRadioButton();
  JRadioButton button3 = new JRadioButton();
  ButtonGroup buttons = new ButtonGroup();

  public Test() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  void jbInit() throws Exception {
    System.out.println("Message in x");
    this.setLayout(borderLayout1);
    jPanel1.setLayout(null);
    button1.setText("Button 1");
    button1.setBounds(new Rectangle(132, 75, 91, 23));
    button2.setText("Button 2");
    button2.setBounds(new Rectangle(132, 100, 91, 23));
    button3.setText("Button 3");
    button3.setBounds(new Rectangle(132, 124, 91, 23));
    this.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(button1, null);
    jPanel1.add(button2, null);
    jPanel1.add(button3, null);
    
    // this is where the radio buttons are added to the button group
    buttons.add(button1);
    buttons.add(button2);
    buttons.add(button3);
    jPanel1.setVisible(true);
  }
}