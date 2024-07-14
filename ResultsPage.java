import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent; // Add this import statement
import java.awt.event.ActionListener; // Add this import statement
import java.sql.*;

public class ResultsPage extends JPanel {
    private JTable table;
    private JLabel gpaLabel;
    private final String link = "jdbc:mysql://localhost:3306/oop";
    private final String dUname = "root";
    private final String dPass = "693369";
    private String[] subjectOptions = {
            "Integral Calculus", "Object Oriented Programming 1", "Object Oriented Programming 2",
            "Computer Organization", "Data Structures" };

    public ResultsPage(String admissionNumber, double calculatedGPA, JFrame mainFrame) {
        setLayout(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.addActionListener(e -> {
            // Remove the ResultsPage
            SwingUtilities.getWindowAncestor(this).dispose();
            new GPAInputPage(admissionNumber);
        });

        // Panel for button alignment
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.NORTH);

        // GPA Label
        gpaLabel = new JLabel(String.format("Your GPA: %.2f", calculatedGPA));
        gpaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel gpaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gpaPanel.add(gpaLabel);
        add(gpaPanel, BorderLayout.SOUTH);

        displayResults(admissionNumber, subjectOptions);
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
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error (e.g., show error message)
        }
    }
}
