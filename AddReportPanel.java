import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AddReportPanel extends JPanel {
    private DefaultTableModel activityModel;
    private JTable activityTable;
    private JButton refreshButton;

    public AddReportPanel() {
        setLayout(new BorderLayout());
        initializeTable();
        initializeRefreshButton();
    }

    private void initializeTable() {
        activityModel = new DefaultTableModel();
        activityModel.addColumn("Admin ID");
        activityModel.addColumn("Activity");
        activityModel.addColumn("Timestamp");

        activityTable = new JTable(activityModel);
        JScrollPane scrollPane = new JScrollPane(activityTable);
        add(scrollPane, BorderLayout.CENTER);

        refreshActivityTable();
    }

    private void initializeRefreshButton() {
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshActivityTable());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(refreshButton, gbc);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void refreshActivityTable() {
        activityModel.setRowCount(0); // Clear existing rows

        try (Connection con = Database.connection()) {
            String query = "SELECT * FROM admin_activities";
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                activityModel.addRow(new Object[]{
                        rs.getInt("admin_id"),
                        rs.getString("activity"),
                        rs.getTimestamp("timestamp")
                });
            }

            activityTable.revalidate();
            activityTable.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
