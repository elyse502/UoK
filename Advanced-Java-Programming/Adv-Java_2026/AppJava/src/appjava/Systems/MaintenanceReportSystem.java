package appjava.Systems;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MaintenanceReportSystem {
    
    JFrame f = new JFrame("Maintenance Report System");
    JPanel p1 = new JPanel(); 
    {
        p1.setBounds(20, 80, 580, 600);
        p1.setBackground(Color.white);
        
        f.add(p1);
        f.setSize(900, 800);
        f.setLayout(null);
        f.setVisible(true);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        new MaintenanceReportSystem();
    }
    
}
