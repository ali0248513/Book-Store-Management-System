

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class AddSearchPanel extends JPanel {
    private JTextField searchText;
    private AdminFrame adminFrame; // Store reference to AdminFrame

    public AddSearchPanel(AdminFrame adminFrame) {
        this.adminFrame = adminFrame; // Initialize AdminFrame reference
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        JLabel searchLabel = new JLabel("Search");
        searchText = new JTextField(20);
        addComponent(searchLabel, searchText, gbc);

        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchBookInDatabase();
                }
            }
        });
    }

    private void addComponent(JLabel label, JTextField textField, GridBagConstraints gbc) {
        gbc.gridx = 0;
        add(label, gbc);
        gbc.gridx = 1;
        add(textField, gbc);
    }

    private void searchBookInDatabase() {
        String searchQuery = searchText.getText();

        try (Connection con = Database.connection()) {
            String query = "SELECT * FROM books WHERE Name LIKE ? OR Author LIKE ? OR Date_Issued LIKE ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "%" + searchQuery + "%");
            pstmt.setString(2, "%" + searchQuery + "%");
            pstmt.setString(3, "%" + searchQuery + "%");
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Name");
            model.addColumn("Author");
            model.addColumn("Price");
            model.addColumn("Year");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("Author"),
                        rs.getFloat("Price"),
                        rs.getString("Date_Issued")
                });
            }

            updateContentPanel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateContentPanel(DefaultTableModel model) {
        adminFrame.updateTable(model); // Use AdminFrame reference to update the table
    }
}
