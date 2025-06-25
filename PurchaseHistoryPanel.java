import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseHistoryPanel extends JPanel {
    private Connection con=Database.connection();
    private int userid;

    public PurchaseHistoryPanel(int userid) {
        this.userid = userid;
        setLayout(new BorderLayout());

        JTable historyTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane, BorderLayout.CENTER);

        loadPurchaseHistory(historyTable);
    }

    private void loadPurchaseHistory(JTable historyTable) {
        String query = "SELECT ph.ID, ph.purchase_date, b.Name AS book_name " +
                       "FROM purchase_history ph " +
                       "JOIN books b ON ph.book_id = b.ID " +
                       "WHERE ph.user_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userid); 
            try (ResultSet rs = stmt.executeQuery()) {
                historyTable.setModel(buildTableModel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static javax.swing.table.TableModel buildTableModel(ResultSet rs) throws SQLException {
        java.sql.ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        java.util.Vector<String> columnNames = new java.util.Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        java.util.Vector<java.util.Vector<Object>> data = new java.util.Vector<>();
        while (rs.next()) {
            java.util.Vector<Object> vector = new java.util.Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new javax.swing.table.DefaultTableModel(data, columnNames);
    }
}
