package appjava.raw;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class listEx {

    {
        JFrame frame = new JFrame("List Example");
        Label label = new   Label("Select Program to Display");
        JButton show = new JButton("Show");
        show.setBounds(300, 170, 80, 30);
        
        label.setSize(400,50);
        label.setAlignment(Label.CENTER);
        
        List l1 = new List(5);
        l1.setBounds(20, 70, 200, 100);
        l1.add("C++");
        l1.add("Java");
        l1.add("C#");
        l1.add("Swift");
        l1.add("Python");
        
        List l2 = new List(5);
        l2.setBounds(20, 200, 200, 100);
        l2.add("John");
        l2.add("Claude");
        l2.add("Manual");
        l2.add("Help");
        l2.add("Sing");
        
        show.addActionListener((ActionEvent l) -> {
            String data = "The selected program are ";
            
            for(String selected: l1.getSelectedItems()){
                data += selected;
            }
            
            for(String selected: l2.getSelectedItems()){
                data += " for " + selected;
            }
            
            label.setText(data);
        });
        
        
        frame.add(label);
        frame.add(l1);
        frame.add(l2);
        frame.add(show);
        frame.setSize(500, 500);
        frame.setLayout(null);
        frame.setVisible(true);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
  
    public static void main(String[] args) {
        // TODO code application logic here
        new listEx();
    }
    
}
