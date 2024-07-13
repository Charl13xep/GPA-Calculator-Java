import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Pattern;

public class RegistrationPage extends JPanel {
    private JTextField nameField, emailField, admissionNumberField, institutionField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JLabel errorLabel;

    public RegistrationPage() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // More padding around components

        // Title (with increased bottom padding)
        JLabel titleLabel = new JLabel("Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10); // Increased bottom padding
        add(titleLabel, gbc);

        // Input Fields and Labels
        createInputRow("Name:", nameField = new JTextField(20), 1);
        gbc.insets = new Insets(20, 10, 10, 10); // Increased top padding for the next row
        createInputRow("Email:", emailField = new JTextField(20), 2);
        createInputRow("Admission Number:", admissionNumberField = new JTextField(20), 3);
        createInputRow("Password:", passwordField = new JPasswordField(20), 4);
        createInputRow("Institution:", institutionField = new JTextField(20), 5);

        // Error Label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(errorLabel, gbc);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(registerButton, gbc);
    }

    // Helper method to create a row of label and input field
    private void createInputRow(String labelText, JComponent inputField, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 20); // Increased right padding for label
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 10); // Reset right padding for input field
        add(inputField, gbc);
    }

    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String admissionNumber = admissionNumberField.getText();
        String password = new String(passwordField.getPassword());
        String institution = institutionField.getText();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:your_database.db")) {
            String query = "SELECT COUNT(*) FROM users WHERE admission_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, admissionNumber);
                try (ResultSet rs = pstmt.executeQuery()) {
                    rs.next(); // Move to the first (and only) row of the result set
                    int count = rs.getInt(1);
                    if (count > 0) {
                        errorLabel.setText("User with this admission number already exists.");
                        return; // Don't proceed with registration
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }

        // Input Validation
        if (name.isEmpty() || email.isEmpty() || admissionNumber.isEmpty() || password.isEmpty()
                || institution.isEmpty()) {
            errorLabel.setText("All fields are required.");
        } else if (!isValidEmail(email)) {
            errorLabel.setText("Invalid email format.");
        } else if (!isValidPassword(password)) {
            errorLabel.setText("Password must be at least 8 characters long and contain letters and numbers.");
        } else {

            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:your_database.db")) {
                String query = "INSERT INTO users (name, email, admissionNumber, password, institution) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, name);
                    pstmt.setString(2, email);
                    pstmt.setString(3, admissionNumber);
                    pstmt.setString(4, password); // Store password in plain text (NOT RECOMMENDED)
                    pstmt.setString(5, institution);
                    pstmt.executeUpdate();

                    // Registration successful
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    // ... (Go back to login page or another action)
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database error (e.g., duplicate username)
            }
        }

    }

    // Email validation using regular expression
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Password validation (at least 8 characters, letters, and numbers)
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Registration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new RegistrationPage());
        frame.setPreferredSize(new Dimension(600, 400));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
