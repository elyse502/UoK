package elyseedev.java.swing;

import java.awt.*;
import java.awt.event.*;

public class AWTScrollableList {

    public AWTScrollableList() {
        // Create the main frame
        Frame frame = new Frame("AWT Scrollable List Example");
        frame.setSize(400, 300);
        frame.setLayout(null);

        // Label
        Label label = new Label("Select a programming language:");
        label.setBounds(20, 20, 250, 30);
        frame.add(label);

        // Create a scrollable list (5 visible rows, single selection)
        List languageList = new List(5, false); // false = single selection
        languageList.setBounds(20, 60, 150, 100); // position and size
        frame.add(languageList);

        // Add programming languages to the list
        String[] languages = {"Java", "Python", "C++", "C#", "JavaScript",
                              "Ruby", "Go", "Kotlin", "Swift", "PHP"};
        for (String lang : languages) {
            languageList.add(lang);
        }

        // Label to display the selected language
        Label outputLabel = new Label("Selected language: ");
        outputLabel.setBounds(20, 180, 350, 30);
        frame.add(outputLabel);

        // Add an event listener for item selection
        languageList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String selected = languageList.getSelectedItem();
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
        new AWTScrollableList();
    }
}