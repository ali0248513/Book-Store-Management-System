import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AddShowMemberPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public AddShowMemberPanel() {
        setLayout(new BorderLayout());

        // Define the table model with column names
        model = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone", "Address", "Role"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        fetchMembersFromDatabase();
    }

    private void fetchMembersFromDatabase() {
        // SQL query to fetch members
        String sqlQuery = "SELECT * FROM members";

        try (Connection con = Database.connection()) {
            // Create statement
            Statement statement = con.createStatement();
            // Execute query
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            // Clear existing table data
            model.setRowCount(0);

            // Populate table with query results
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String role = resultSet.getString("role");

                // Add row to table model
                model.addRow(new Object[]{id, name, email, phone, address, role});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching members: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
