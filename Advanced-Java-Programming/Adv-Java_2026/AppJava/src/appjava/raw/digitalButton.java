package appjava.raw;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class digitalButton {

    {
        String[] button = {"9", "8", "7", "6", "5", "4", "3", "2", "1", "0"};
        
        JFrame frame = new JFrame("Calculator Test");
        JPanel panel = new JPanel();
        panel.setBounds(20, 20, 100, 200);
        
        JButton digit;
        digit = new JButton();
        
        for(int i = 0; i < button.length; i++){
            digit = new JButton(button[i]);
            digit.setSize(20,20);
            panel.add(digit);
        }
        
        digit.addActionListener((ActionEvent e) -> {
            System.out.println("Pressed");
        });
        
        panel.setLayout(new GridLayout(5, 2, 5, 5));
        
        frame.add(panel);
        frame.setSize(500,500);
        frame.setLayout(null);
        frame.setVisible(true);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        // TODO code application logic here
        new digitalButton();
    }
    
}
