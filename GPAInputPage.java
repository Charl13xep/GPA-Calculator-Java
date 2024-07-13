import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GPAInputPage extends JPanel {

    // ... (same fields as before)
    private String[] subjectOptions = {
            "Mathematics", "English", "Physics", "Chemistry", "Biology",
            "History", "Geography", "Computer Science", "Art", "Music",
            "Physical Education", "Business Studies", "Foreign Language", "Other" };

    private JPanel inputPanel; // Now a class member
    private JButton addSubjectButton, calculateButton;

    private List<JComboBox<String>> gradeFields = new ArrayList<>();
    private List<JComboBox<String>> subjectFields = new ArrayList<>();
    private List<JTextField> creditHoursFields = new ArrayList<>(); // Optional
    private GridBagConstraints gbc = new GridBagConstraints();

    public GPAInputPage() {

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

    // ... (Methods to get data from fields and validate input)

    public static void main(String[] args) {
        JFrame frame = new JFrame("GPA Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new GPAInputPage());

        // Set preferred size for a wider window
        frame.setPreferredSize(new Dimension(600, 400));
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}
