package elyseedev.java.swing;

import java.awt.*;
import java.awt.event.*;

public class AWTList {

    public AWTList() {
        // Create a frame
        Frame frame = new Frame("AWT List Example");
        frame.setSize(400, 300);
        frame.setLayout(null);

        // Create a List (single selection)
        List itemList = new List(5, false); // 5 visible rows, single selection
        itemList.setBounds(50, 50, 150, 100);
        
        // Add items
        itemList.add("Apple");
        itemList.add("Banana");
        itemList.add("Cherry");
        itemList.add("Date");
        itemList.add("Elderberry");
        frame.add(itemList);

        // Button to show selected item
        Button button = new Button("Show Selected");
        button.setBounds(50, 160, 120, 30);
        frame.add(button);

        // Label to display selection
        Label outputLabel = new Label("");
        outputLabel.setBounds(50, 200, 300, 30);
        frame.add(outputLabel);

        // Button action
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = itemList.getSelectedItem();
                if (selected != null) {
                    outputLabel.setText("Selected item: " + selected);
                } else {
                    outputLabel.setText("No item selected");
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
        new AWTList();
    }
}