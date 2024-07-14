import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JPanel {
    private Image backgroundImage;

    public HomePage() {
        setLayout(new BorderLayout(20, 40));
        setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        // Load and scale the background image
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\shawn\\OneDrive\\Desktop\\.vscode\\GPA-Calculator-Java\\images\\pexels-ian-panelo-5491023.jpg");
        backgroundImage = imageIcon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH); 

        // Title (white text)
        JLabel titleLabel = new JLabel("GPA Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE); 
        add(titleLabel, BorderLayout.NORTH);

        // Description (white text)
        JLabel descriptionLabel = new JLabel(
            "<html><div style='text-align: center;font-family: Arial, sans-serif;line-height: 1.5; color: white;'>" +
                    "Calculate and track your GPA with ease.<br/>" +
                    "Log in to get started." +
                    "</div></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(descriptionLabel, BorderLayout.CENTER); // Add directly to main panel

        // Login button (white text)
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(150, 40)); 
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.setForeground(Color.WHITE); 
        loginButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(HomePage.this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new LoginPage()); 
            frame.revalidate();
            frame.repaint();
        });
        add(loginButton, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image to cover the entire panel
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
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
