import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;

public class LoginPage extends JPanel {
    private JTextField admissionNumberField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;
    private JButton registerButton;

    public LoginPage() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);


        // Left Panel (Image)
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage image = ImageIO.read(new File(
                            "C:\\Users\\shawn\\OneDrive\\Desktop\\.vscode\\GPA-Calculator-Java\\images\\640074.png"));
                    // Scale the image to fit the panel while maintaining aspect ratio
                    int imgWidth = image.getWidth();
                    int imgHeight = image.getHeight();
                    int panelWidth = getWidth();
                    int panelHeight = getHeight();

                    double scaleFactor = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
                    int newImgWidth = (int) (imgWidth * scaleFactor);
                    int newImgHeight = (int) (imgHeight * scaleFactor);

                    g.drawImage(image, (panelWidth - newImgWidth) / 2, (panelHeight - newImgHeight) / 2, newImgWidth,
                            newImgHeight, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 200); // Larger preferred size for the image
            }

            @Override
            public Dimension getMinimumSize() {
                return new Dimension(150, 150); // Minimum size to prevent shrinking too much
            }
        };
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5; // 50% width for the image (adjust as needed)
        gbc.weighty = 1.0;
        add(leftPanel, gbc);

        // Right Panel (Form)
        JPanel formPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        add(formPanel, gbc);

        // Form Elements
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(5, 5, 5, 5);
        formGbc.anchor = GridBagConstraints.WEST; // Left alignment


        // Title Label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formGbc.gridwidth = 2;
        formPanel.add(titleLabel, formGbc);

        // Admission Number Label
        JLabel admissionNumberLabel = new JLabel("Admission Number:");
        formGbc.gridx = 0;
        formGbc.gridy = 1;
        formGbc.gridwidth = 2; // Label spans both columns
        formPanel.add(admissionNumberLabel, formGbc);

        // Admission Number Field (move to next row)
        admissionNumberField = new JTextField(20);
        formGbc.gridx = 0; // Start in the first column
        formGbc.gridy = 2; // Move to the next row
        formGbc.gridwidth = 2; // Span both columns
        formGbc.fill = GridBagConstraints.HORIZONTAL; 
        formPanel.add(admissionNumberField, formGbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        formGbc.gridx = 0;
        formGbc.gridy = 3; // Move to the next row
        formGbc.gridwidth = 2; // Label spans both columns
        formGbc.fill = GridBagConstraints.NONE; 
        formPanel.add(passwordLabel, formGbc);

        // Password Field (move to next row)
        passwordField = new JPasswordField(20);
        formGbc.gridx = 0; // Start in the first column
        formGbc.gridy = 4; // Move to the next row
        formGbc.gridwidth = 2; // Span both columns
        formGbc.fill = GridBagConstraints.HORIZONTAL; 
        formPanel.add(passwordField, formGbc);

        // Button Panel (to hold both buttons side by side)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        formGbc.gridx = 0;
        formGbc.gridy = 5;
        formGbc.gridwidth = 2;
        formPanel.add(buttonPanel, formGbc);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));
        loginButton.addActionListener(e -> attemptLogin());
        buttonPanel.add(loginButton);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 30));
        registerButton.addActionListener(e -> {
            // Code to switch to the registration page here
            // For example:
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginPage.this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new RegistrationPage());
            frame.revalidate();
            frame.repaint();
        });
        buttonPanel.add(registerButton);

        // Error Label (below the buttons)
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        formGbc.gridy = 6;
        formPanel.add(errorLabel, formGbc);
    

        
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
