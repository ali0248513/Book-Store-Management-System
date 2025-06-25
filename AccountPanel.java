import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountPanel extends JPanel {
    private Connection con = Database.connection();
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JButton updateButton;
    private JButton editButton;
    private int userId;

    public AccountPanel(int userId) {
        this.userId = userId;

        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel addressLabel = new JLabel("Address:");

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        addressField = new JTextField(20);

        nameField.setEditable(false);
        emailField.setEditable(false);
        phoneField.setEditable(false);
        addressField.setEditable(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        infoPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(addressField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        editButton = new JButton("Edit Info");
        updateButton = new JButton("Update Info");

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableEditing(true);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserDetails(nameField.getText(), emailField.getText(), phoneField.getText(), addressField.getText());
                enableEditing(false);
            }
        });

        buttonPanel.add(editButton);
        buttonPanel.add(updateButton);

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUserDetails();
    }

    private void enableEditing(boolean enable) {
        nameField.setEditable(enable);
        emailField.setEditable(enable);
        phoneField.setEditable(enable);
        addressField.setEditable(enable);
    }

    private void loadUserDetails() {
        String query = "SELECT * FROM members WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    emailField.setText(rs.getString("email"));
                    phoneField.setText(rs.getString("phone"));
                    addressField.setText(rs.getString("address"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUserDetails(String name, String email, String phone, String address) {
        String updateQuery = "UPDATE members SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(updateQuery)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setInt(5, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
