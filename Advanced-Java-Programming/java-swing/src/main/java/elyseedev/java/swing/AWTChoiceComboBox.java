package elyseedev.java.swing;

import java.awt.*;
import java.awt.event.*;

public class AWTChoiceComboBox {

    public AWTChoiceComboBox() {
        // Create frame
        Frame frame = new Frame("AWT Choice (ComboBox) Example");
        frame.setSize(400, 250);
        frame.setLayout(null);

        // Label
        Label label = new Label("Select your favorite programming language:");
        label.setBounds(20, 50, 300, 30);
        frame.add(label);

        // Create Choice (combo box)
        Choice languageChoice = new Choice();
        languageChoice.setBounds(20, 90, 150, 30);

        // Add items to Choice
        languageChoice.add("Java");
        languageChoice.add("Python");
        languageChoice.add("C++");
        languageChoice.add("C#");
        languageChoice.add("JavaScript");
        languageChoice.add("Ruby");
        languageChoice.add("Go");
        languageChoice.add("Kotlin");
        languageChoice.add("Swift");
        frame.add(languageChoice);

        // Label to display selected item
        Label outputLabel = new Label("Selected language: ");
        outputLabel.setBounds(20, 140, 300, 30);
        frame.add(outputLabel);

        // Add item listener
        languageChoice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String selected = languageChoice.getSelectedItem();
                outputLabel.setText("Selected language: " + selected);
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
        new AWTChoiceComboBox();
    }
}