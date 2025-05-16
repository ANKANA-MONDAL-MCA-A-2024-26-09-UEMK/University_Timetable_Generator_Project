package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("University Timetable Generator");
        setSize(500, 450); // Increased size to fit logout button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient Background Panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 51, 102), getWidth(), getHeight(), new Color(0, 102, 204));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // Main Panel for Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // Increased row count by 1 for logout button
        buttonPanel.setBackground(new Color(255, 255, 255, 200)); // White with slight transparency
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Buttons
        JButton manageDept = createStyledButton("Manage Departments");
        JButton manageFaculty = createStyledButton("Manage Faculty");
        JButton managePaper = createStyledButton("Manage Papers");
        JButton viewTimetable = createStyledButton("View Timetable");
        JButton generateTimetable = createStyledButton("Generate Timetable");
        JButton logoutButton = createStyledButton("Logout");

        // Button Actions
        manageDept.addActionListener(e -> new ManageDepartment());
        manageFaculty.addActionListener(e -> new ManageFaculty());
        managePaper.addActionListener(e -> new ManagePaper());
        viewTimetable.addActionListener(e -> new ViewTimetable());
        generateTimetable.addActionListener(e -> new GenerateTimetable());
        logoutButton.addActionListener(e -> logout());

        // Adding buttons to panel
        buttonPanel.add(manageDept);
        buttonPanel.add(manageFaculty);
        buttonPanel.add(managePaper);
        buttonPanel.add(viewTimetable);
        buttonPanel.add(generateTimetable);
        buttonPanel.add(logoutButton); // Add the logout button to the panel

        // Adding components to frame
        backgroundPanel.add(buttonPanel);
        add(backgroundPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 102, 204));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 82, 184));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));
            }
        });

        return button;
    }

    private void logout() {
        // Close the current frame
        this.dispose();
        
        // Show the login page (assuming you have a Login class to navigate to)
        new LoginPage(); // Replace with your actual Login class if necessary
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}
