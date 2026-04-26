/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package elyseedev.java.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author elyse
 */

public class JavaSwing {

    public static void main(String[] args) {
        // Create a new frame (window)
        JFrame frame = new JFrame("Swing Example");
        frame.setSize(400, 200); // width, height
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null); // we'll position components manually

        // Create a label
        JLabel label = new JLabel("Hello, Java Swing!");
        label.setBounds(130, 30, 200, 30); // x, y, width, height
        frame.add(label);

        // Create a button
        JButton button = new JButton("Click Me!");
        button.setBounds(150, 80, 100, 30);
        frame.add(button);

        // Add action listener to button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("Button Clicked!");
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }
}
