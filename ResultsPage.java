import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent; // Add this import statement
import java.awt.event.ActionListener; // Add this import statement
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class ResultsPage extends JPanel {
    private JTable table;
    private JLabel gpaLabel;
    private Image backgroundImage;
    private final String link = "jdbc:mysql://localhost:3306/oop";
    private final String dUname = "root";
    private final String dPass = "693369";
    private String[] subjectOptions = {
            "Integral Calculus", "Object Oriented Programming 1", "Object Oriented Programming 2",
            "Computer Organization", "Data Structures" };

    public ResultsPage(String admissionNumber, double calculatedGPA, JFrame mainFrame) {
        JFrame frame = new JFrame("GPA Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setPreferredSize(new Dimension(600, 400));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        setLayout(new BorderLayout());

        try {
            backgroundImage = ImageIO.read(new File(
                    "C:\\Users\\Lenovo\\Desktop\\school\\Programming\\JAVA\\VSCode\\GPA Calculator\\images\\pexels-pixabay-33130.jpg"))
                    .getScaledInstance(600, 400, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0, 0, 0, 128)); // Semi-transparent black
        backButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new GPAInputPage(admissionNumber);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        buttonPanel.setOpaque(false); // Make panel transparent
        add(buttonPanel, BorderLayout.NORTH);

        // GPA Label
        // GPA Label (white text)
        gpaLabel = new JLabel(String.format("Your GPA: %.2f", calculatedGPA));
        gpaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gpaLabel.setForeground(Color.WHITE);
        JPanel gpaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gpaPanel.setOpaque(false); // Make panel transparent
        gpaPanel.add(gpaLabel);
        add(gpaPanel, BorderLayout.SOUTH);

        displayResults(admissionNumber, subjectOptions);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void displayResults(String admissionNumber, String[] subjectOptions) {
        try (Connection conn = DriverManager.getConnection(link, dUname, dPass)) {
            DefaultTableModel tableModel = new DefaultTableModel(new String[] { "Subject", "Grade" }, 0); // Fixed
                                                                                                          // column
                                                                                                          // names
            for (String subject : subjectOptions) {
                String formattedSubject = subject.replace(" ", "_");
                String query = "SELECT " + formattedSubject + " FROM grades WHERE admission_number = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, admissionNumber);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            String grade = rs.getString(formattedSubject);
                            tableModel.addRow(new Object[] { subject, grade });
                        } else {
                            tableModel.addRow(new Object[] { subject, "N/A" });
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    tableModel.addRow(new Object[] { subject, "N/A" });
                }
            }
            table = new JTable(tableModel);
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240)); // Alternate row color
                    return c;
                }
            });
            table.setOpaque(false);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            add(scrollPane, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error (e.g., show error message)
        }
    }

    public static void main(String[] args) {
        // Create a new ResultsPage
        SwingUtilities.invokeLater(() -> new ResultsPage("1111", 3.5, new JFrame()));

    }
}
