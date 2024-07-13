import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPAInputPage extends JPanel {

    // ... (same fields as before)
    private String[] subjectOptions = {
            "Integral Calculus", "Object Oriented Programming 1", "Object Oriented Programming 2",
            "Computer Organization", "Data Structures",
            "Differential Calculus", "Ethics", "Management", "Computer Networks", "Operating Systems",
            "Economics", "Statistics", "Probability", "Discrete" };

    private JPanel inputPanel; // Now a class member
    private JButton addSubjectButton, calculateButton;

    private List<JComboBox<String>> gradeFields = new ArrayList<>();
    private List<JComboBox<String>> subjectFields = new ArrayList<>();
    private List<JTextField> creditHoursFields = new ArrayList<>(); // Optional
    private GridBagConstraints gbc = new GridBagConstraints();

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

        calculateButton.addActionListener(e -> calculateGPA());

        // Initial subject row
        addSubjectRow();
    }

    private void addSubjectRow() {
        JComboBox<String> subjectField = new JComboBox<>(subjectOptions);
        // JTextField subjectField = new JTextField(20); // Set preferred column width
        String[] gradeOptions = { "A", "B", "C", "D", "F" };
        JComboBox<String> gradeField = new JComboBox<>(gradeOptions);
        JTextField creditHoursField = new JTextField(5); // Set preferred column width for credit hours

        subjectFields.add(subjectField);
        gradeFields.add(gradeField);
        creditHoursFields.add(creditHoursField);

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

        // Optional: Add credit hours field
        if (creditHoursFields.size() > 0) {
            gbc.gridx = 3;
            inputPanel.add(new JLabel("Credits:"), gbc);
            gbc.gridx = 4;
            inputPanel.add(creditHoursField, gbc);
        }

        revalidate();
    }

    private void calculateGPA() {
        Map<String, Double> gradePoints = new HashMap<>();
        gradePoints.put("A", 4.0);
        gradePoints.put("B", 3.0);
        gradePoints.put("C", 2.0);
        gradePoints.put("D", 1.0);
        gradePoints.put("F", 0.0);

        double totalWeightedGradePoints = 0;
        int totalCreditHours = 0;

        for (int i = 0; i < subjectFields.size(); i++) {
            String subject = (String) subjectFields.get(i).getSelectedItem();
            String grade = (String) gradeFields.get(i).getSelectedItem();
            int creditHours;
            try {
                creditHours = Integer.parseInt(creditHoursFields.get(i).getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid credit hours for " + subject);
                return;
            }

            if (gradePoints.containsKey(grade)) {
                totalWeightedGradePoints += gradePoints.get(grade) * creditHours;
                totalCreditHours += creditHours;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid grade entered for " + subject);
                return;
            }
        }

        if (totalCreditHours == 0) {
            JOptionPane.showMessageDialog(this, "Please enter credit hours for at least one subject.");
            return;
        }

        double gpa = totalWeightedGradePoints / totalCreditHours;
        JOptionPane.showMessageDialog(this, String.format("Your GPA is: %.2f", gpa));
    }

    // ... (Methods to get data from fields and validate input)

    public static void main(String[] args) {
        GPAInputPage inputPage = new GPAInputPage(admissionNumber);

    }
}
