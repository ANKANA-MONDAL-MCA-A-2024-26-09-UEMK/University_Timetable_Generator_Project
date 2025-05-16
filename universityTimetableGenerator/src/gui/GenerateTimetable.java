package gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import database.DatabaseConnection;

public class GenerateTimetable extends JFrame {
    private JTable timetableTable;

    public GenerateTimetable() {
        setTitle("Generated Timetable");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set background color for the entire frame
        getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

        // Table columns
        String[] columns = { "Day", "Time Slot", "Paper Name", "Faculty Name", "Room" };
        Object[][] data = generateTimetableData();

        // Set up table
        timetableTable = new JTable(data, columns);
        timetableTable.setFillsViewportHeight(true);

        // Customize table appearance
        timetableTable.setBackground(new Color(255, 255, 255)); // White background for table
        timetableTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Set table font
        timetableTable.setRowHeight(25); // Set row height
        timetableTable.setGridColor(new Color(220, 220, 220)); // Light grid color
        timetableTable.setSelectionBackground(new Color(0, 122, 255)); // Blue selection background
        timetableTable.setSelectionForeground(Color.WHITE); // White text when selected

        // Set header style
        JTableHeader header = timetableTable.getTableHeader();
        header.setBackground(new Color(0, 122, 255)); // Header background color
        header.setForeground(Color.WHITE); // Header text color
        header.setFont(new Font("Arial", Font.BOLD, 16)); // Header font style

        // Add the table to the center of the frame
        add(new JScrollPane(timetableTable), BorderLayout.CENTER);

        // Create the "Back to Menu" button
        JButton backButton = new JButton("Back to Menu");
        backButton.setBackground(new Color(0, 102, 204));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            // Close the current frame and return to the previous page (Menu)
            dispose();
            // Assuming you have a MenuPage class, otherwise replace this with your actual menu screen
            new MainMenu(); 
        });

        // Create a panel for the back button and add it at the bottom of the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240)); // Same background color as the frame
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private Object[][] generateTimetableData() {
        List<Object[]> timetable = new ArrayList<>();
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        String[] timeSlots = { "9:00 - 11:00", "11:15 - 1:15", "2:00 - 4:00" };

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT paper_name, faculty_id FROM Paper")) {

            List<String[]> paperFacultyList = new ArrayList<>();
            while (rs.next()) {
                paperFacultyList.add(new String[]{rs.getString("paper_name"), getFacultyName(rs.getInt("faculty_id"), conn)});
            }

            if (paperFacultyList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No papers available to generate timetable!", "Error", JOptionPane.ERROR_MESSAGE);
                return new Object[0][0];
            }

            int index = 0;
            for (String day : days) {
                for (int i = 0; i < timeSlots.length; i++) {
                    if (index >= paperFacultyList.size()) index = 0;

                    String paperName = paperFacultyList.get(index)[0];
                    String facultyName = paperFacultyList.get(index)[1];
                    String room = getAvailableRoom(conn);

                    timetable.add(new Object[]{ day, timeSlots[i], paperName, facultyName, room });

                    if (i == 0) {
                        timetable.add(new Object[]{ day, "11:00 - 11:15", "Break", "", "" });
                    } else if (i == 1) {
                        timetable.add(new Object[]{ day, "1:15 - 2:00", "Lunch Break", "", "" });
                    }
                    index++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating timetable!", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return timetable.toArray(new Object[0][0]);
    }

    private String getFacultyName(int facultyId, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT faculty_name FROM Faculty WHERE faculty_id = ?");
        stmt.setInt(1, facultyId);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getString("faculty_name") : "Unknown Faculty";
    }

    private String getAvailableRoom(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT room_name FROM Rooms LIMIT 1");
        return rs.next() ? rs.getString("room_name") : "Room Not Assigned";
    }

    public static void main(String[] args) {
        new GenerateTimetable();
    }
}
