package gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ViewTimetable extends JFrame {
    private JTable timetableTable;

    public ViewTimetable() {
        setTitle("View Timetable");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Define columns for the timetable
        String[] columns = { "Day", "Time Slot", "Paper Name", "Faculty Name", "Room" };

        // Example timetable data (Replace with actual database data)
        Object[][] data = {
            {"Monday", "9:00 - 11:00", "Course XYZ", "Dr. ABC", "Room 101"},
            {"Monday", "11:15 - 1:15", "Course DEF", "Dr. XYZ", "Room 102"},
            {"Monday", "2:00 - 4:00", "Course GHI", "Dr. LMN", "Room 103"},
            {"Tuesday", "9:00 - 11:00", "Course ABC", "Dr. PQR", "Room 201"},
            {"Tuesday", "11:15 - 1:15", "Course DEF", "Dr. STU", "Room 202"},
            {"Wednesday", "9:00 - 11:00", "Course XYZ", "Dr. XYZ", "Room 301"}
        };

        // Create a table with custom styling
        timetableTable = new JTable(data, columns);
        customizeTable();

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(timetableTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Add "Back to Menu" button
        JButton backButton = new JButton("Back to Menu");
        backButton.setBackground(new Color(0, 102, 204));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            // Close the current frame and return to the previous page (Menu)
            dispose();
            // Replace MenuPage with your actual menu or previous screen class
            new MainMenu(); // Assuming MenuPage is your menu class, replace it with the actual class name
        });

        // Create a panel for the back button and add it at the bottom of the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240)); // Same background color as the frame
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void customizeTable() {
        JTableHeader header = timetableTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(30, 144, 255)); // Dodger Blue
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(header.getWidth(), 30));

        timetableTable.setFont(new Font("Arial", Font.PLAIN, 13));
        timetableTable.setRowHeight(25);
        timetableTable.setGridColor(Color.LIGHT_GRAY);
        timetableTable.setShowGrid(false);
        timetableTable.setIntercellSpacing(new Dimension(10, 5));

        // Alternate row colors
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(240, 248, 255) : Color.WHITE); // Light blue & white
                }
                return c;
            }
        };

        for (int i = 0; i < timetableTable.getColumnCount(); i++) {
            timetableTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

    public static void main(String[] args) {
        new ViewTimetable();
    }
}
