package elyseedev.java.swing;

/**
 *
 * @author Elysee NIYIBIZI
 */

import javax.swing.*;
import java.awt.*;

public class LayoutManagement {

    public LayoutManagement() {
        // Create the frame
        JFrame frame = new JFrame("Layout Management Example");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set layout to BorderLayout
        frame.setLayout(new BorderLayout());

        // Add buttons to each region
        JButton northButton = new JButton("North");
        frame.add(northButton, BorderLayout.NORTH);

        JButton southButton = new JButton("South");
        frame.add(southButton, BorderLayout.SOUTH);

        JButton eastButton = new JButton("East");
        frame.add(eastButton, BorderLayout.EAST);

        JButton westButton = new JButton("West");
        frame.add(westButton, BorderLayout.WEST);

        JButton centerButton = new JButton("Center");
        frame.add(centerButton, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new LayoutManagement();
    }
}