package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import database.DatabaseConnection;

public class ManageDepartment extends JFrame {
    private JTextField departmentField;
    private JComboBox<String> departmentComboBox;
    private JButton addButton, editButton, deleteButton, backButton;

    public ManageDepartment() {
        setTitle("Manage Departments");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel with Gradient
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

        JPanel contentPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // Increase the row count for the new button
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(255, 255, 255, 200)); // Light Transparent Background

        // Labels and Inputs
        JLabel selectLabel = new JLabel("Select Department:");
        departmentComboBox = new JComboBox<>(getDepartmentList()); // Getting the department list here
        JLabel nameLabel = new JLabel("Department Name:");
        departmentField = new JTextField();

        // Buttons with Styling
        addButton = createStyledButton("Add Department");
        editButton = createStyledButton("Edit Department");
        deleteButton = createStyledButton("Delete Department");
        backButton = createStyledButton("Back to Menu"); // The Back Button

        // Adding Actions
        addButton.addActionListener(e -> addDepartment());
        editButton.addActionListener(e -> editDepartment());
        deleteButton.addActionListener(e -> deleteDepartment());
        departmentComboBox.addActionListener(e -> loadSelectedDepartment());
        backButton.addActionListener(e -> goBackToMenu()); // Back button action

        // Adding Components
        contentPanel.add(selectLabel);
        contentPanel.add(departmentComboBox);
        contentPanel.add(nameLabel);
        contentPanel.add(departmentField);
        contentPanel.add(addButton);
        contentPanel.add(editButton);
        contentPanel.add(deleteButton);
        contentPanel.add(backButton); // Add the back button to the content panel

        backgroundPanel.add(contentPanel);
        add(backgroundPanel);

        setVisible(true);
    }

    private void loadSelectedDepartment() {
        String selectedDept = (String) departmentComboBox.getSelectedItem();
        if (selectedDept != null) {
            departmentField.setText(selectedDept);
        }
    }

    private int getDepartmentId(String department) {
        int deptId = -1; // Default value if department not found
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT dept_id FROM Department WHERE name = ?");
            stmt.setString(1, department);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                deptId = rs.getInt("dept_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return deptId;
    }

    private void addDepartment() {
        String deptName = departmentField.getText();
        if (deptName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Department name cannot be empty.");
            return;
        }
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Department (name) VALUES (?)");
            stmt.setString(1, deptName);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Department Added Successfully!");
            refreshDepartmentList();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void editDepartment() {
        String selectedDept = (String) departmentComboBox.getSelectedItem();
        String newDeptName = departmentField.getText();
        if (selectedDept == null || newDeptName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select and enter a new department name.");
            return;
        }
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE Department SET name = ? WHERE name = ?");
            stmt.setString(1, newDeptName);
            stmt.setString(2, selectedDept);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Department Updated Successfully!");
                refreshDepartmentList();
            } else {
                JOptionPane.showMessageDialog(this, "Department not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteDepartment() {
        String selectedDept = (String) departmentComboBox.getSelectedItem();
        if (selectedDept == null) {
            JOptionPane.showMessageDialog(this, "Please select a department to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this department?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Department WHERE name = ?");
            stmt.setString(1, selectedDept);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Department Deleted Successfully!");
                refreshDepartmentList();
            } else {
                JOptionPane.showMessageDialog(this, "Department not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void refreshDepartmentList() {
        departmentComboBox.removeAllItems();
        for (String dept : getDepartmentList()) {
            departmentComboBox.addItem(dept);
        }
        departmentField.setText("");
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

    // Method to fetch the list of departments from the database
    private String[] getDepartmentList() {
        ArrayList<String> departmentList = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.connect()) {
            // Query to fetch all department names
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM Department");
            ResultSet rs = stmt.executeQuery();
            
            // Add each department name to the list
            while (rs.next()) {
                departmentList.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Convert ArrayList to String[] and return
        return departmentList.toArray(new String[0]);
    }

    // Go back to the previous screen (Menu)
    private void goBackToMenu() {
        this.dispose(); // Close the current window
        new MainMenu(); // Assuming you have a MainMenu class, you can replace it with your actual main menu class
    }

    public static void main(String[] args) {
        new ManageDepartment();
    }
}
