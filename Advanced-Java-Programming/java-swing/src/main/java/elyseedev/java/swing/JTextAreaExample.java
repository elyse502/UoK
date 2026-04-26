package elyseedev.java.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JTextAreaExample {

    public JTextAreaExample() {
        // Create frame
        JFrame frame = new JFrame("JTextArea Example");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Label
        JLabel label = new JLabel("Enter your text:");
        label.setBounds(50, 20, 120, 30);
        frame.add(label);

        // Text area
        JTextArea textArea = new JTextArea();
        textArea.setBounds(50, 60, 300, 100);
        textArea.setLineWrap(true);   // Wrap text when it reaches the end of the area
        textArea.setWrapStyleWord(true); // Wrap at word boundaries
        frame.add(textArea);

        // Button
        JButton button = new JButton("Show Text");
        button.setBounds(150, 180, 100, 30);
        frame.add(button);

        // Output label
        JLabel outputLabel = new JLabel("");
        outputLabel.setBounds(50, 220, 300, 30);
        frame.add(outputLabel);

        // Button action
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                outputLabel.setText("You typed: " + text);
            }
        });

        // Show frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new JTextAreaExample();
    }
}