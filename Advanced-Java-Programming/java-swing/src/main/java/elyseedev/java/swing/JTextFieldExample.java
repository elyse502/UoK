/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package elyseedev.java.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 *
 * @author elyse
 */
public class JTextFieldExample {

    public JTextFieldExample() {
        // Create a new frame
        JFrame frame = new JFrame("JTextField Example");
        frame.setSize(400, 200);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a label
        JLabel label = new JLabel("Enter text:");
        label.setBounds(50, 30, 100, 30);
        frame.add(label);

        // Create a text field
        JTextField textField = new JTextField();
        textField.setBounds(150, 30, 150, 30);
        frame.add(textField);

        // Create a button
        JButton button = new JButton("Show Text");
        button.setBounds(150, 80, 100, 30);
        frame.add(button);

        // Label to display text
        JLabel outputLabel = new JLabel("");
        outputLabel.setBounds(50, 120, 300, 30);
        frame.add(outputLabel);

        // Add action listener to button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textField.getText(); // Get text from JTextField
                outputLabel.setText("You typed: " + input);
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new JTextFieldExample();
    }
}