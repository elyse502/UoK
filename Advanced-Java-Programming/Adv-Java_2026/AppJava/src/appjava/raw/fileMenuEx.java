package appjava.raw;
import javax.swing.*;
import java.awt.event.*;

public class fileMenuEx {

    fileMenuEx(){
        
        JFrame frame = new JFrame("File Menu Example");
        JMenuBar menuBar = new JMenuBar();
        JTextArea area = new JTextArea();
        
        area.setBounds(0, 0, 400,400);
        
        // File Menus        
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        
        // Dropdown Menus
        JMenuItem newItem1 = new JMenuItem("New");
        JMenuItem newItem2 = new JMenuItem("Open");
        JMenuItem newItem3 = new JMenuItem("Save");
        JMenuItem newItem4 = new JMenuItem("Exit");
        
        // Adding Dropdown menus
        fileMenu.add(newItem1);
        fileMenu.add(newItem2);
        fileMenu.add(newItem3);
        fileMenu.add(newItem4);
        
        // Adding menus to the bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        
        
        // Action to close frame
        newItem4.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        
        // Setting MenuBar to Frame
        frame.setJMenuBar(menuBar);
        
        frame.add(area);
        
        frame.setSize(400,400);
        frame.setVisible(true);
        frame.setLayout(null);
        
    }
    public static void main(String[] args) {
        // TODO code application logic here
        new fileMenuEx();
    }
    
}
