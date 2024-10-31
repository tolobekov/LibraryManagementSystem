package tolobekov.library;

import javax.swing.*;
import tolobekov.library.ui.LoginScreen;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the JFrame directly here
            JFrame frame = new JFrame("Library Management System");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); // Center the window

            // Set the content pane to the LoginScreen
            frame.setContentPane(new LoginScreen(frame));
            // Make the frame visible
            frame.setVisible(true);
        });
    }
}
