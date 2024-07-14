import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

public class RegistrationPage extends JPanel {
    private BufferedImage backgroundImage;
    private JTextField nameField, emailField, admissionNumberField, facultyField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JLabel errorLabel;

    private final String link = "jdbc:mysql://localhost:3306/oop";
    private final String dUname = "root";
    private final String dPass = "693369";

    public RegistrationPage() {
        setLayout(new BorderLayout()); 

        // Load and create a BufferedImage for scaling
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\shawn\\OneDrive\\Desktop\\.vscode\\GPA-Calculator-Java\\images\\pexels-markus-winkler-1430818-4097118.jpg");
        backgroundImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = backgroundImage.createGraphics();
        g2.drawImage(imageIcon.getImage(), 0, 0, null);
        g2.dispose();

        // Form Panel (set transparent background)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        addFormElements(formPanel);
        add(formPanel, BorderLayout.CENTER); // Center the form on the image background
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Scale and draw the background image dynamically
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    

    private void addFormElements(JPanel formPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        // Title (with increased bottom padding)
        JLabel titleLabel = new JLabel("Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        formPanel.add(titleLabel, gbc);

        // Input Fields and Labels
        createInputRow(formPanel, gbc, "Name:", nameField = new JTextField(20), 1);
        gbc.insets = new Insets(20, 10, 10, 10); 
        createInputRow(formPanel, gbc, "Email:", emailField = new JTextField(20), 2);
        createInputRow(formPanel, gbc, "Admission Number:", admissionNumberField = new JTextField(20), 3);
        createInputRow(formPanel, gbc, "Password:", passwordField = new JPasswordField(20), 4);
        createInputRow(formPanel, gbc, "Faculty:", facultyField = new JTextField(20), 5);

        // Error Label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(errorLabel, gbc);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> registerUser());
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(registerButton, gbc);

        // Back Button (top right)
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK); // Set background color to black
        backButton.setForeground(Color.WHITE); // Set text color to white

        // Add action listener to the back button
        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(RegistrationPage.this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new HomePage()); // Replace with your HomePage panel
            frame.revalidate();
            frame.repaint();
        });

        // Panel to hold the back button (aligned to the top right)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Make the panel transparent
        topPanel.add(backButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH); // Add the topPanel to the main panel

        
    }


    // Helper method to create a row of label and input field (modified to take formPanel)
    private void createInputRow(JPanel formPanel, GridBagConstraints gbc, String labelText, JComponent inputField, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 20);
        JLabel label = new JLabel(labelText); 
        label.setForeground(Color.WHITE);
        formPanel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 10);
        inputField.setForeground(Color.WHITE); // Set text color to white
        inputField.setBackground(Color.DARK_GRAY); // Adjust background for visibility
        formPanel.add(inputField, gbc);
    }
    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String admissionNumber = admissionNumberField.getText();
        String password = new String(passwordField.getPassword());
        String faculty = facultyField.getText();

        try (Connection conn = DriverManager.getConnection(link, dUname, dPass)) {
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
                || faculty.isEmpty()) {
            errorLabel.setText("All fields are required.");
        } else if (!isValidEmail(email)) {
            errorLabel.setText("Invalid email format.");
        } else if (!isValidPassword(password)) {
            errorLabel.setText("Password must be at least 8 characters long and contain letters and numbers.");
        } else {

            try (Connection conn = DriverManager.getConnection(link, dUname, dPass)) {
                String query = "INSERT INTO users (name1, email, admission_number, password1, faculty) VALUES (?, ?, ?, ?, ?)";
                String query2 = "INSERT INTO grades (admission_number) VALUES (?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, name);
                    pstmt.setString(2, email);
                    pstmt.setString(3, admissionNumber);
                    pstmt.setString(4, password); // Store password in plain text (NOT RECOMMENDED)
                    pstmt.setString(5, faculty);
                    pstmt.executeUpdate();

                    // Registration successful
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    SwingUtilities.getWindowAncestor(this).dispose();
                    new LoginPage();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database error (e.g., duplicate username)
            }

            try {
                Connection conn = DriverManager.getConnection(link, dUname, dPass);
                String query2 = "INSERT INTO grades (admission_number) VALUES (?)";
                try (PreparedStatement pstmt2 = conn.prepareStatement(query2)) {
                    pstmt2.setString(1, admissionNumber);
                    pstmt2.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
