import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JPanel {
    private JTextField admissionNumberField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;

    public LoginPage() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        add(titleLabel, gbc);

        JLabel admissionNumberLabel = new JLabel("admissionNumber:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(admissionNumberLabel, gbc);

        admissionNumberField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(admissionNumberField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(errorLabel, gbc);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> attemptLogin());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make button fill width
        add(loginButton, gbc);
    }

    private void attemptLogin() {
        String admissionNumber = admissionNumberField.getText();
        String password = new String(passwordField.getPassword()); // Get password (plain text)

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:your_database.db")) {
            String query = "SELECT password FROM users WHERE admissionNumber = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, admissionNumber);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        if (password.equals(storedPassword)) {
                            // Successful login (passwords match)
                            // Open GPAInputPage or another window
                            // ...
                        } else {
                            errorLabel.setText("Incorrect password.");
                        }
                    } else {
                        errorLabel.setText("User not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }

    public String getAdmissionNumber() {
        return admissionNumberField.getText();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new LoginPage());
        frame.setPreferredSize(new Dimension(600, 400));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
