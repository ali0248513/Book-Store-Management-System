import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AddDashPanel extends JPanel {
    private DefaultTableModel booksModel;
    private JTable booksTable;
    private DefaultTableModel deletedBooksModel;
    private JTable deletedBooksTable;
    private JButton updateButton;

    public AddDashPanel() {
        super(new BorderLayout());
        initializeTables();
        initializeUpdateButton();
    }

    private void initializeTables() {
        // Initialize books table
        booksModel = new DefaultTableModel();
        booksModel.addColumn("ID");
        booksModel.addColumn("Name");
        booksModel.addColumn("Author");
        booksModel.addColumn("Price");
        booksModel.addColumn("Year");

        booksTable = new JTable(booksModel);
        JScrollPane booksScrollPane = new JScrollPane(booksTable);
        add(new JLabel("Currently Available Books"), BorderLayout.NORTH);
        add(booksScrollPane, BorderLayout.CENTER);

        // Initialize deleted books table
        deletedBooksModel = new DefaultTableModel();
        deletedBooksModel.addColumn("ID");
        deletedBooksModel.addColumn("Name");
        deletedBooksModel.addColumn("Author");
        deletedBooksModel.addColumn("Price");
        deletedBooksModel.addColumn("Year");
        deletedBooksModel.addColumn("Deletion Date");

        deletedBooksTable = new JTable(deletedBooksModel);
        JScrollPane deletedBooksScrollPane = new JScrollPane(deletedBooksTable);
        add(new JLabel("Deleted Books"), BorderLayout.SOUTH);
        add(deletedBooksScrollPane, BorderLayout.SOUTH);

        refreshTables();
    }

    private void initializeUpdateButton() {
        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTables();
            }
        });

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(updateButton, gbc);

        add(buttonPanel, BorderLayout.NORTH);
    }

    public void refreshTables() {
        refreshBooksTable();
        refreshDeletedBooksTable();
    }

    private void refreshBooksTable() {
        booksModel.setRowCount(0); // Clear existing rows

        try (Connection con = Database.connection()) {
            String query = "SELECT * FROM books";
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                booksModel.addRow(new Object[]{
                    rs.getInt("ID"),
                    rs.getString("Name"),
                    rs.getString("Author"),
                    rs.getString("Price"),
                    rs.getString("date_Issued")
                });
            }

            booksTable.revalidate();
            booksTable.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void refreshDeletedBooksTable() {
        deletedBooksModel.setRowCount(0); // Clear existing rows

        try (Connection con = Database.connection()) {
            String query = "SELECT * FROM deleted_books";
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                deletedBooksModel.addRow(new Object[]{
                    rs.getInt("ID"),
                    rs.getString("Name"),
                    rs.getString("Author Name"),
                    rs.getString("Price"),
                    rs.getString("Year"),
                    rs.getTimestamp("Deletion_Date")
                });
            }

            deletedBooksTable.revalidate();
            deletedBooksTable.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
