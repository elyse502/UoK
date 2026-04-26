package appjava.raw;

import javax.swing.*;
import java.awt.event.*;

public class FileMenuWork {

    FileMenuWork() {
        JFrame f = new JFrame("File Menu Example");
        JMenuBar nav = new JMenuBar();
        JTextArea a = new JTextArea();
        
        a.setBounds(0, 0, 500, 500);
        
        JMenu file = new JMenu("File");
        
        // Making the MenuItem for File
        JMenuItem ne = new JMenuItem("New");
        JMenuItem op = new JMenuItem("Open");
        JMenuItem sa = new JMenuItem("Save");
        JMenuItem ex = new JMenuItem("Exit");
        
        // Adding the MenuItem to the Menu File
        file.add(ne);
        file.add(op);
        file.add(sa);
        file.add(ex);
        
        // Adding action listener to the button
        ex.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        
        // Adding the Menu to the Menubar        
        nav.add(file);
        
        // Setting the MenuBar to the frame
        f.setJMenuBar(nav);
        
        // Adding textarea to the frame
        f.add(a);
        
        f.setSize(500, 500);
        f.setVisible(true);
        f.setLayout(null);
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        new FileMenuWork();
    }
}
