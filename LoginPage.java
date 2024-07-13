import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;

public class LoginPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;

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
                    BufferedImage image = ImageIO.read(new File("C:\\Users\\shawn\\OneDrive\\Desktop\\.vscode\\GPA-Calculator-Java\\images\\640074.png"));
                    // Scale the image to fit the panel while maintaining aspect ratio
                    int imgWidth = image.getWidth();
                    int imgHeight = image.getHeight();
                    int panelWidth = getWidth();
                    int panelHeight = getHeight();

                    double scaleFactor = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
                    int newImgWidth = (int) (imgWidth * scaleFactor);
                    int newImgHeight = (int) (imgHeight * scaleFactor);

                    g.drawImage(image, (panelWidth - newImgWidth) / 2, (panelHeight - newImgHeight) / 2, newImgWidth, newImgHeight, this);
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
        gbc.weightx = 0.5;  // 50% width for the image (adjust as needed)
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

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        formGbc.gridx = 0;
        formGbc.gridy = 1;
        formGbc.gridwidth = 1; // Reset to one column
        formPanel.add(usernameLabel, formGbc);

        // Username Field
        usernameField = new JTextField(20);
        formGbc.gridx = 1;
        formGbc.gridy = 1;
        formGbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space
        formPanel.add(usernameField, formGbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        formGbc.gridx = 0;
        formGbc.gridy = 2;
        formGbc.fill = GridBagConstraints.NONE; // Reset fill
        formPanel.add(passwordLabel, formGbc);

        // Password Field
        passwordField = new JPasswordField(20);
        formGbc.gridx = 1;
        formGbc.gridy = 2;
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, formGbc);

        // Error Label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        formGbc.gridx = 0;
        formGbc.gridy = 3;  // Moved to row 3
        formGbc.gridwidth = 2;
        formPanel.add(errorLabel, formGbc);

        // Login Button (Smaller and Aligned)
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30)); // Adjust size as needed
        loginButton.addActionListener(e -> attemptLogin());
        formGbc.gridx = 0;
        formGbc.gridy = 4;  // Moved to row 4
        formGbc.gridwidth = 2;
        formGbc.fill = GridBagConstraints.NONE;    // Don't fill horizontal space
        formGbc.anchor = GridBagConstraints.CENTER; // Center the button
        formPanel.add(loginButton, formGbc);

        try {
            BufferedImage image = ImageIO.read(new File("C:\\Users\\shawn\\OneDrive\\Desktop\\.vscode\\GPA-Calculator-Java\\images\\640074.png"));
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            gbc.gridx = 0; // Adjust the position as needed
            gbc.gridy = 0; 
            gbc.gridwidth = 2; // Span two columns if needed
            add(imageLabel, gbc);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the case where the image cannot be loaded
        } 
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword()); // Get password (plain text)

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:your_database.db")) {
            String query = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);
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
