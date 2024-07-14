import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPAInputPage extends JPanel {

    // ... (same fields as before)
    private String[] subjectOptions = {
            "Integral Calculus", "Object Oriented Programming 1", "Object Oriented Programming 2",
            "Computer Organization", "Data Structures" };

    private JPanel inputPanel; // Now a class member
    private JButton addSubjectButton, calculateButton;
    private final int DEFAULT_CREDIT_HOURS = 2;

    private List<JComboBox<String>> gradeFields = new ArrayList<>();
    private List<JComboBox<String>> subjectFields = new ArrayList<>();

    private GridBagConstraints gbc = new GridBagConstraints();
    private final String link = "jdbc:mysql://localhost:3306/oop";
    private final String dUname = "root";
    private final String dPass = "693369";

    private static String admissionNumber;

    public GPAInputPage(String admissionNumber) {
        JFrame frame = new JFrame("GPA Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);

        // Set preferred size for a wider window
        frame.setPreferredSize(new Dimension(600, 400));
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
        this.admissionNumber = admissionNumber;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("GPA Calculator (4.0 Scale)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        inputPanel = new JPanel(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(inputPanel, BorderLayout.CENTER);

        addSubjectButton = new JButton("Add Subject");
        addSubjectButton.addActionListener(e -> addSubjectRow());
        addSubjectButton.setFont(new Font("Arial", Font.PLAIN, 16));

        calculateButton = new JButton("Calculate GPA");
        // Add action listener to calculateButton to process data
        calculateButton.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addSubjectButton);
        buttonPanel.add(calculateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        calculateButton.addActionListener(e -> calculateAndStoreGPA());

        // Initial subject row
        addSubjectRow();
    }

    private void addSubjectRow() {
        JComboBox<String> subjectField = new JComboBox<>(subjectOptions);
        // JTextField subjectField = new JTextField(20); // Set preferred column width
        String[] gradeOptions = { "A", "B", "C", "D", "F" };
        JComboBox<String> gradeField = new JComboBox<>(gradeOptions);

        subjectFields.add(subjectField);
        gradeFields.add(gradeField);

        // Set weights for columns to distribute space better
        gbc.weightx = 0.5; // Subject field takes up more horizontal space
        gbc.gridx = 0;
        gbc.gridy = inputPanel.getComponentCount() / 3;
        inputPanel.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(subjectField, gbc);

        gbc.weightx = 0.25; // Grade and credits fields take less space
        gbc.gridx = 2;
        inputPanel.add(gradeField, gbc);

        revalidate();
    }

    private void calculateAndStoreGPA() {
        Map<String, Double> gradePoints = new HashMap<>();
        gradePoints.put("A", 4.0);
        gradePoints.put("B", 3.0);
        gradePoints.put("C", 2.0);
        gradePoints.put("D", 1.0);
        gradePoints.put("F", 0.0);

        double totalWeightedGradePoints = 0;

        try (Connection conn = DriverManager.getConnection(link, dUname, dPass)) {
            // conn.setAutoCommit(false);
            String insertQuery = "UPDATE grades SET `Integral_Calculus`=?, `Object_Oriented_Programming_1`=? , `Object_Oriented_Programming_2`  = ?, `Computer_Organization` = ?, `Data_Structures` = ?  WHERE admission_number=? ";
            try (PreparedStatement pstmt2 = conn.prepareStatement(insertQuery)) {
                pstmt2.setString(6, admissionNumber);
                pstmt2.setString(1, gradeFields.get(0).getSelectedItem().toString());
                pstmt2.setString(2, gradeFields.get(1).getSelectedItem().toString());
                pstmt2.setString(3, gradeFields.get(2).getSelectedItem().toString());
                pstmt2.setString(4, gradeFields.get(3).getSelectedItem().toString());
                pstmt2.setString(5, gradeFields.get(4).getSelectedItem().toString());

                pstmt2.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving GPA data.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < subjectFields.size(); i++) {
            String subject = (String) subjectFields.get(i).getSelectedItem();
            String grade = (String) gradeFields.get(i).getSelectedItem();
            int creditHours = DEFAULT_CREDIT_HOURS; // Use default credit hours

            if (gradePoints.containsKey(grade)) {
                totalWeightedGradePoints += gradePoints.get(grade) * creditHours;

            } else {
                JOptionPane.showMessageDialog(this, "Invalid grade entered for subject " + (i + 1));
                return;
            }
        }
        double gpa = totalWeightedGradePoints / (DEFAULT_CREDIT_HOURS * subjectFields.size());

        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(GPAInputPage.this);
        mainFrame.getContentPane().removeAll();
        ResultsPage resultsPage = new ResultsPage(admissionNumber, gpa, mainFrame);
        mainFrame.getContentPane().add(resultsPage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    // ... (Methods to get data from fields and validate input)

    public static void main(String[] args) {
        GPAInputPage inputPage = new GPAInputPage(admissionNumber);

    }
}
