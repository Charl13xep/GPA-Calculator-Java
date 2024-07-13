import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JPanel {

    public HomePage() {
        setLayout(new BorderLayout(20, 40)); // Increased vertical padding
        setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80)); // More padding on sides

        // Title with larger font and more spacing
        JLabel titleLabel = new JLabel("GPA Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Panel for description and image
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Description with custom font and line spacing
        JLabel descriptionLabel = new JLabel(
                "<html><div style='text-align: center;font-family: Arial, sans-serif;line-height: 1.5;'>" +
                        "Calculate and track your GPA with ease.<br/>" +
                        "Log in to get started." +
                        "</div></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(descriptionLabel, gbc);

        // Add an image (replace 'path/to/your/image.png' with your actual image path)
        ImageIcon imageIcon = new ImageIcon("path/to/your/image.png");
        Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 10, 10, 10); // Add more top margin to the image
        centerPanel.add(imageLabel, gbc);
        add(centerPanel, BorderLayout.CENTER);

        // Login button with larger size and padding
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(150, 40)); // Make button bigger
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(HomePage.this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new LoginPage());
            frame.revalidate();
            frame.repaint();
        });
        add(loginButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GPA Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new HomePage());
        frame.pack();

        // Set preferred size (adjust values as needed)
        Dimension preferredSize = new Dimension(600, 400); // Wider and taller
        frame.setPreferredSize(preferredSize);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
