package appjava.raw;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

public class CalcEx {
    JFrame f = new JFrame("Calc Example");
        
    JLabel l1 = new JLabel("Num1");
    JLabel l2 = new JLabel("Num2");
    JLabel l3 = new JLabel("Result");
    
    JTextField t1 = new JTextField();
    JTextField t2 = new JTextField();
    JTextField t3 = new JTextField();
        
    JButton b1 = new JButton("Add");
    JButton b2 = new JButton("Subtract");
        
    CalcEx(){   
        b1.setBounds(20, 180, 80, 30);
        b2.setBounds(120, 180, 100, 30);
        
        l1.setBounds(20, 30, 60, 30);
        l2.setBounds(20, 80, 60, 30);
        l3.setBounds(20, 130, 60, 30);
        
        t3.setEditable(false);
        t1.setBounds(100, 30, 120, 30);
        t2.setBounds(100, 80, 120, 30);
        t3.setBounds(100, 130, 120, 30);
        
        b1.addActionListener((ActionEvent e) -> sum());
        b2.addActionListener((ActionEvent e) -> subtract());
        
        f.add(l1); f.add(l2); f.add(l3); f.add(t1); f.add(t2); f.add(t3);
        f.add(b1); f.add(b2);
        
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void sum() {
        int num1 = Integer.parseInt(t1.getText());
        int num2 = Integer.parseInt(t2.getText()); 
        int res = num1 + num2;
        t3.setText(""+ res);
    }
    
    public void subtract(){
        int num1 = Integer.parseInt(t1.getText());
        int num2 = Integer.parseInt(t2.getText()); 
        int res = num1 - num2;
        t3.setText(""+ res);
    }
    
    public static void main(String[] args) {
        new CalcEx();
    }
    
}
