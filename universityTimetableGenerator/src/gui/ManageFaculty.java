package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

import database.DatabaseConnection;

public class ManageFaculty extends JFrame {
    private JTextField nameField;
    private JComboBox<String> departmentComboBox;
    private JButton addButton, editButton, deleteButton, backButton;

    public ManageFaculty() {
        setTitle("Manage Faculty");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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

        // Inner Panel for Components
        JPanel contentPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // Changed to 4 rows to accommodate back button
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(255, 255, 255, 200)); // Light Transparent Panel

        // Labels and Inputs
        JLabel nameLabel = new JLabel("Faculty Name:");
        nameField = new JTextField();
        JLabel departmentLabel = new JLabel("Department:");
        departmentComboBox = new JComboBox<>(getDepartmentList());

        // Buttons with Custom Styling
        addButton = createStyledButton("Add Faculty");
        editButton = createStyledButton("Edit Faculty");
        deleteButton = createStyledButton("Delete Faculty");
        backButton = createStyledButton("Back to Menu");  // Added Back to Menu button

        // Adding Actions
        addButton.addActionListener(e -> addFaculty());
        editButton.addActionListener(e -> editFaculty());
        deleteButton.addActionListener(e -> deleteFaculty());
        backButton.addActionListener(e -> goBackToMenu()); // Action for going back to menu

        // Adding Components to Panel
        contentPanel.add(nameLabel);
        contentPanel.add(nameField);
        contentPanel.add(departmentLabel);
        contentPanel.add(departmentComboBox);
        contentPanel.add(addButton);
        contentPanel.add(editButton);
        contentPanel.add(deleteButton);
        contentPanel.add(backButton);  // Add the back button to the layout

        backgroundPanel.add(contentPanel);
        add(backgroundPanel);

        setVisible(true);
    }

    // Fetch department list from the database
    private String[] getDepartmentList() {
        try (Connection conn = DatabaseConnection.connect()) {
            // Fetch all department names from the database
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM Department");
            ResultSet rs = stmt.executeQuery();
            // Collect department names in a list
            ArrayList<String> departmentList = new ArrayList<>();
            while (rs.next()) {
                departmentList.add(rs.getString("name"));
            }
            // Return department names as an array
            return departmentList.toArray(new String[0]);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new String[0]; // Return an empty array if there's an error
        }
    }

    // Go back to the main menu or previous screen
    private void goBackToMenu() {
        this.dispose(); // Close the current window
        new MainMenu();  // Assuming you have a MainMenu class to show the main screen or previous screen
    }

    // Method to add faculty
    private void addFaculty() {
        String facultyName = nameField.getText();
        String department = (String) departmentComboBox.getSelectedItem();

        if (facultyName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Faculty name cannot be empty.");
            return;
        }

        // Get the department ID
        int deptId = getDepartmentId(department);
        if (deptId == -1) {
            // Department does not exist, show an error
            JOptionPane.showMessageDialog(this, "Selected department does not exist.");
            return;
        }

        // Insert faculty if department is valid
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Faculty (faculty_name, dept_id) VALUES (?, ?)");
            stmt.setString(1, facultyName);
            stmt.setInt(2, deptId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Faculty Added Successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding faculty: " + ex.getMessage());
        }
    }

    // Get department ID from the database
    private int getDepartmentId(String department) {
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT dept_id FROM Department WHERE name = ?");
            stmt.setString(1, department);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("dept_id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Method to edit faculty
    private void editFaculty() {
        String facultyName = JOptionPane.showInputDialog(this, "Enter the faculty name to edit:");
        if (facultyName != null && !facultyName.trim().isEmpty()) {
            try (Connection conn = DatabaseConnection.connect()) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Faculty WHERE faculty_name = ?");
                stmt.setString(1, facultyName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String newFacultyName = JOptionPane.showInputDialog(this, "Enter new faculty name:", rs.getString("faculty_name"));
                    if (newFacultyName != null) {
                        PreparedStatement updateStmt = conn.prepareStatement("UPDATE Faculty SET faculty_name = ? WHERE faculty_name = ?");
                        updateStmt.setString(1, newFacultyName);
                        updateStmt.setString(2, facultyName);
                        updateStmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Faculty updated successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Faculty not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Method to delete faculty
    private void deleteFaculty() {
        String facultyName = JOptionPane.showInputDialog(this, "Enter the faculty name to delete:");
        if (facultyName != null && !facultyName.trim().isEmpty()) {
            try (Connection conn = DatabaseConnection.connect()) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM Faculty WHERE faculty_name = ?");
                stmt.setString(1, facultyName);
                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Faculty deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Faculty not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 102, 204));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover Effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(0, 82, 184));
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        new ManageFaculty();
    }
}
