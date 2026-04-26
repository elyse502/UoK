package appjava.raw;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ChoiceEx {
    JFrame f = new JFrame("Frame Example");
    JButton b1 = new JButton("Show");
    JLabel l = new JLabel();
    Choice c = new Choice();

    ChoiceEx(){
        l.setBounds(50, 120, 200, 50);
        c.setBounds(50, 50, 120, 50);
        c.add("C"); c.add("C#"); c.add("C++"); c.add("PHP");
        b1.setBounds(190, 50, 80, 30);
        
        b1.addActionListener((ActionEvent l) -> selected());
        
        f.add(c); f.add(b1); f.add(l);
        
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void selected(){
        String data = "The choice made is " + c.getSelectedItem();
        l.setText(data);
    }
    public static void main(String[] args) {
        new ChoiceEx();
    }
    
}
