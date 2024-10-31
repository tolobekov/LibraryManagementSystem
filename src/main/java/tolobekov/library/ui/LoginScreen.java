package tolobekov.library.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import tolobekov.library.database.DataManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import tolobekov.library.model.User;
import tolobekov.library.util.LibraryController;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LoginScreen extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JFrame parentFrame;

    public LoginScreen(JFrame parent) {
        this.parentFrame = parent;
        setLayout(new BorderLayout());

        // Add the login panel directly to this panel
        add(createLoginPanel(), BorderLayout.CENTER);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Username:"), constraints);

        constraints.gridx = 1;
        usernameField = new JTextField(20);
        panel.add(usernameField, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        panel.add(new JLabel("Password:"), constraints);

        constraints.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, constraints);

        constraints.gridy++;
        loginButton = new JButton("Login");
        loginButton.addActionListener(this::performLogin);
        panel.add(loginButton, constraints);

        constraints.gridy++;
        registerButton = new JButton("Register");
        registerButton.addActionListener(this::openRegistrationForm);
        panel.add(registerButton, constraints);

        return panel;
    }

    private void performLogin(ActionEvent e) {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();

        User user = authenticate(username, password);

        if (user != null) {
            transitionToNextScreen(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private User authenticate(String username, char[] password) {
        try {
            // Deserialize the list of users from the file system
            List<User> users = DataManager.loadUsers();
            if (users != null) {
                for (User user : users) {
                    // Check if the input username matches any username in the list
                    if (user.getUsername().equals(username)) {

                        if (Arrays.equals(password, user.getPassword().toCharArray())) {
                            return user; // Return the user type if authentication is successful
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; // Return null if authentication fails
    }

    private void transitionToNextScreen(User user) {
        // Clear the current content pane
        parentFrame.getContentPane().removeAll();
        LibraryController libraryController = new LibraryController();
        // Determine which dashboard to display based on the user type
        JPanel newPanel;
        if ("Librarian".equals(user.getUserType())) {
            newPanel = new LibrarianDashboard();
        } else {
            // Default to UserDashboard if not a librarian
            newPanel = new UserDashboard(user, libraryController);
        }

        // Update the content pane and refresh the frame
        parentFrame.setContentPane(newPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void openRegistrationForm(ActionEvent e) {
        // Open the registration form. This could be a new dialog or a different panel
        RegistrationForm registrationForm = new RegistrationForm(parentFrame);
        registrationForm.setVisible(true);
    }

}