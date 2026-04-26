package elyseedev.java.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextFieldSimpleCalculator {

    public TextFieldSimpleCalculator() {
        // Create frame
        JFrame frame = new JFrame("Simple Calculator");
        frame.setSize(400, 250);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // First number label and text field
        JLabel label1 = new JLabel("Number 1:");
        label1.setBounds(50, 30, 80, 30);
        frame.add(label1);

        JTextField num1Field = new JTextField();
        num1Field.setBounds(150, 30, 150, 30);
        frame.add(num1Field);

        // Second number label and text field
        JLabel label2 = new JLabel("Number 2:");
        label2.setBounds(50, 70, 80, 30);
        frame.add(label2);

        JTextField num2Field = new JTextField();
        num2Field.setBounds(150, 70, 150, 30);
        frame.add(num2Field);

        // Result label
        JLabel resultLabel = new JLabel("Result: ");
        resultLabel.setBounds(50, 150, 300, 30);
        frame.add(resultLabel);

        // Create buttons for operations
        JButton addButton = new JButton("+");
        addButton.setBounds(50, 110, 50, 30);
        frame.add(addButton);

        JButton subButton = new JButton("-");
        subButton.setBounds(110, 110, 50, 30);
        frame.add(subButton);

        JButton mulButton = new JButton("*");
        mulButton.setBounds(170, 110, 50, 30);
        frame.add(mulButton);

        JButton divButton = new JButton("/");
        divButton.setBounds(230, 110, 50, 30);
        frame.add(divButton);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double num1 = Double.parseDouble(num1Field.getText());
                double num2 = Double.parseDouble(num2Field.getText());
                double result = num1 + num2;
                resultLabel.setText("Result: " + result);
            }
        });

        subButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double num1 = Double.parseDouble(num1Field.getText());
                double num2 = Double.parseDouble(num2Field.getText());
                double result = num1 - num2;
                resultLabel.setText("Result: " + result);
            }
        });

        mulButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double num1 = Double.parseDouble(num1Field.getText());
                double num2 = Double.parseDouble(num2Field.getText());
                double result = num1 * num2;
                resultLabel.setText("Result: " + result);
            }
        });

        divButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double num1 = Double.parseDouble(num1Field.getText());
                double num2 = Double.parseDouble(num2Field.getText());
                if (num2 != 0) {
                    double result = num1 / num2;
                    resultLabel.setText("Result: " + result);
                } else {
                    resultLabel.setText("Cannot divide by zero!");
                }
            }
        });

        // Show frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new TextFieldSimpleCalculator();
    }
}