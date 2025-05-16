package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import database.DatabaseConnection;

public class ManagePaper extends JFrame {
    private JTextField paperNameField;
    private JComboBox<String> facultyComboBox, paperComboBox;
    private JComboBox<Integer> semesterComboBox;
    private JButton addButton, editButton, deleteButton, backButton;

    public ManagePaper() {
        setTitle("Manage Papers");
        setSize(550, 400); // Adjust size to accommodate the new button
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
        JPanel contentPanel = new JPanel(new GridLayout(6, 2, 10, 10)); // Adjusted GridLayout to add a new button
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(255, 255, 255, 200)); // Light Transparent Panel

        // Labels and Inputs
        JLabel selectPaperLabel = new JLabel("Select Paper:");
        paperComboBox = new JComboBox<>(getPaperList());
        paperComboBox.addActionListener(e -> loadPaperDetails());

        JLabel nameLabel = new JLabel("Paper Name:");
        paperNameField = new JTextField();

        JLabel facultyLabel = new JLabel("Faculty:");
        facultyComboBox = new JComboBox<>(getFacultyList());

        JLabel semesterLabel = new JLabel("Semester:");
        semesterComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});

        // Buttons with Custom Styling
        addButton = createStyledButton("Add Paper");
        editButton = createStyledButton("Edit Paper");
        deleteButton = createStyledButton("Delete Paper");
        backButton = createStyledButton("Back to Menu");  // New button for going back to the menu

        // Adding Actions
        addButton.addActionListener(e -> addPaper());
        editButton.addActionListener(e -> editPaper());
        deleteButton.addActionListener(e -> deletePaper());
        backButton.addActionListener(e -> goBackToMenu()); // Action to go back to menu

        // Adding Components to Panel
        contentPanel.add(selectPaperLabel);
        contentPanel.add(paperComboBox);
        contentPanel.add(nameLabel);
        contentPanel.add(paperNameField);
        contentPanel.add(facultyLabel);
        contentPanel.add(facultyComboBox);
        contentPanel.add(semesterLabel);
        contentPanel.add(semesterComboBox);
        contentPanel.add(addButton);
        contentPanel.add(editButton);
        contentPanel.add(deleteButton);
        contentPanel.add(backButton); // Adding the new button to the panel

        backgroundPanel.add(contentPanel);
        add(backgroundPanel);

        setVisible(true);
    }

    private String[] getFacultyList() {
        Vector<String> facultyList = new Vector<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT faculty_name FROM Faculty")) {
            while (rs.next()) {
                facultyList.add(rs.getString("faculty_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facultyList.toArray(new String[0]);
    }

    private String[] getPaperList() {
        Vector<String> papers = new Vector<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT paper_name FROM Paper")) {
            while (rs.next()) {
                papers.add(rs.getString("paper_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return papers.toArray(new String[0]);
    }

    private void loadPaperDetails() {
        String selectedPaper = (String) paperComboBox.getSelectedItem();
        if (selectedPaper == null) return;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Paper WHERE paper_name = ?")) {
            stmt.setString(1, selectedPaper);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                paperNameField.setText(rs.getString("paper_name"));
                facultyComboBox.setSelectedItem(getFacultyName(rs.getInt("faculty_id")));
                semesterComboBox.setSelectedItem(rs.getInt("semester"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addPaper() {
        String paperName = paperNameField.getText();
        String faculty = (String) facultyComboBox.getSelectedItem();
        Integer semester = (Integer) semesterComboBox.getSelectedItem();

        if (paperName.isEmpty() || faculty == null || semester == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            int facultyId = getFacultyId(faculty);
            if (facultyId == -1) {
                JOptionPane.showMessageDialog(this, "Faculty does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Paper (paper_name, faculty_id, semester) VALUES (?, ?, ?)");
            stmt.setString(1, paperName);
            stmt.setInt(2, facultyId);
            stmt.setInt(3, semester);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Paper Added Successfully!");
            paperComboBox.addItem(paperName);
            paperNameField.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding paper!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getFacultyId(String facultyName) {
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT faculty_id FROM Faculty WHERE faculty_name = ?");
            stmt.setString(1, facultyName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("faculty_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String getFacultyName(int facultyId) {
        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT faculty_name FROM Faculty WHERE faculty_id = ?");
            stmt.setInt(1, facultyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("faculty_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Faculty";
    }

    private void editPaper() {
        String selectedPaper = (String) paperComboBox.getSelectedItem();
        if (selectedPaper == null) {
            JOptionPane.showMessageDialog(this, "Select a paper to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newPaperName = paperNameField.getText();
        String faculty = (String) facultyComboBox.getSelectedItem();
        Integer semester = (Integer) semesterComboBox.getSelectedItem();

        if (newPaperName.isEmpty() || faculty == null || semester == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            int facultyId = getFacultyId(faculty);
            if (facultyId == -1) {
                JOptionPane.showMessageDialog(this, "Faculty does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PreparedStatement stmt = conn.prepareStatement("UPDATE Paper SET paper_name = ?, faculty_id = ?, semester = ? WHERE paper_name = ?");
            stmt.setString(1, newPaperName);
            stmt.setInt(2, facultyId);
            stmt.setInt(3, semester);
            stmt.setString(4, selectedPaper);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Paper Updated Successfully!");
            paperComboBox.removeItem(selectedPaper);
            paperComboBox.addItem(newPaperName);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating paper!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePaper() {
        String selectedPaper = (String) paperComboBox.getSelectedItem();
        if (selectedPaper == null) {
            JOptionPane.showMessageDialog(this, "Select a paper to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + selectedPaper + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DatabaseConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Paper WHERE paper_name = ?");
            stmt.setString(1, selectedPaper);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Paper Deleted Successfully!");
            paperComboBox.removeItem(selectedPaper);
            paperNameField.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting paper!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBackToMenu() {
        // Close the current window
        this.dispose();
        
        // Create and show the previous menu or window (e.g., the main menu)
        new MainMenu(); // Replace with the appropriate class for the main menu
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    public static void main(String[] args) {
        new ManagePaper();
    }
}
