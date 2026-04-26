package elyseedev.java.swing;

import java.awt.*;
import java.awt.event.*;

public class AWTCheckbox {

    public AWTCheckbox() {
        // Create frame
        Frame frame = new Frame("AWT Checkbox Example");
        frame.setSize(400, 250);
        frame.setLayout(null);

        // Create checkboxes
        Checkbox cb1 = new Checkbox("Option 1");
        cb1.setBounds(50, 50, 100, 30);
        frame.add(cb1);

        Checkbox cb2 = new Checkbox("Option 2");
        cb2.setBounds(50, 80, 100, 30);
        frame.add(cb2);

        Checkbox cb3 = new Checkbox("Option 3");
        cb3.setBounds(50, 110, 100, 30);
        frame.add(cb3);

        // Button
        Button button = new Button("Show Selected");
        button.setBounds(50, 150, 120, 30);
        frame.add(button);

        // Label to display selected options
        Label outputLabel = new Label("");
        outputLabel.setBounds(50, 190, 300, 30);
        frame.add(outputLabel);

        // Button action
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = "";
                if (cb1.getState()) selected += "Option 1 ";
                if (cb2.getState()) selected += "Option 2 ";
                if (cb3.getState()) selected += "Option 3 ";

                if (selected.isEmpty()) {
                    outputLabel.setText("No option selected");
                } else {
                    outputLabel.setText("Selected: " + selected);
                }
            }
        });

        // Close window properly
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                frame.dispose();
            }
        });

        // Show frame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new AWTCheckbox();
    }
}