package tolobekov.library.ui;

import javax.swing.*;
import java.util.List;
import tolobekov.library.database.DataManager;
import tolobekov.library.model.User;
import java.io.IOException;

import java.lang.ClassNotFoundException;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegistrationForm extends JDialog {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeComboBox;
    private JButton registerButton;

    public RegistrationForm(Frame owner) {
        super(owner, "Registration", true);
        setSize(300, 200);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(new JLabel("Username:"), constraints);

        constraints.gridx = 1;
        usernameField = new JTextField(20);
        add(usernameField, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        add(new JLabel("Password:"), constraints);

        constraints.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, constraints);

        constraints.gridy++;
        add(new JLabel("User Type:"), constraints);

        constraints.gridx = 1;
        userTypeComboBox = new JComboBox<>(new String[] { "Member", "Librarian" });
        add(userTypeComboBox, constraints);

        constraints.gridy++;
        constraints.gridwidth = 2;
        registerButton = new JButton("Register");
        registerButton.addActionListener(this::performRegistration);
        add(registerButton, constraints);
    }

    private void performRegistration(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeComboBox.getSelectedItem();

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // User creation
        User newUser = new User(username, password, userType);

        try {
            List<User> users = DataManager.loadUsers(); // Load existing users
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    JOptionPane.showMessageDialog(this, "This username is already taken.", "Registration Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            users.add(newUser); // Add new user to list
            DataManager.saveUsers(users); // Save updated user list
            JOptionPane.showMessageDialog(this, "Registration successful!", "Registration",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the registration form
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to register. Please try again.", "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}