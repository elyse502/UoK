/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elyseedev.java.swing;
import java.awt.*;
import javax.swing.*;


/**
 *
 * @author elyse
 */
public class JPanelExample {

    public JPanelExample() {
        JFrame f = new JFrame("Panel Example");

        // Create panel
        JPanel panel = new JPanel();
        panel.setBounds(40, 80, 200, 200);
        panel.setBackground(Color.gray);
        panel.setLayout(null); // allows manual positioning of buttons

        // Create buttons
        JButton b1 = new JButton("Button 1");
        b1.setBounds(20, 30, 80, 30);
        b1.setBackground(Color.yellow);

        JButton b2 = new JButton("Button 2");
        b2.setBounds(100, 30, 80, 30);
        b2.setBackground(Color.green);

        panel.add(b1);
        panel.add(b2);

        // Add panel to frame
        f.add(panel);
        f.setSize(400, 400);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new JPanelExample();
    }
}