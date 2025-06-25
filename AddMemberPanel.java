import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddMemberPanel extends JPanel {
    private JLabel nameLabel, roleLabel, emailLabel, phoneLabel, addressLabel, passwordLabel;
    private JTextField nameText, roleText, emailText, phoneText, addressText, passwordText;
    private JButton addButton;

    public AddMemberPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        nameLabel = new JLabel("Name");
        nameText = new JTextField(20);
        addComponent(this, nameLabel, nameText, gbc);

        roleLabel = new JLabel("Role (Admin/User)");
        roleText = new JTextField(20);
        addComponent(this, roleLabel, roleText, gbc);

        emailLabel = new JLabel("Email");
        emailText = new JTextField(20);
        addComponent(this, emailLabel, emailText, gbc);

        phoneLabel = new JLabel("Phone");
        phoneText = new JTextField(20);
        addComponent(this, phoneLabel, phoneText, gbc);

        addressLabel = new JLabel("Address");
        addressText = new JTextField(20);
        addComponent(this, addressLabel, addressText, gbc);

        passwordLabel = new JLabel("Password");
        passwordText = new JTextField(20);
        addComponent(this, passwordLabel, passwordText, gbc);

        addButton = new JButton("Add Member");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMemberToDatabase();
            }
        });
        addComponent(this, new JLabel(), addButton, gbc);
    }

    private void addComponent(JPanel panel, JLabel label, JTextField textField, GridBagConstraints gbc) {
        gbc.gridx = 0;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    private void addComponent(JPanel panel, JLabel label, JButton button, GridBagConstraints gbc) {
        gbc.gridx = 0;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(button, gbc);
    }

    private void addMemberToDatabase() {
        String name = nameText.getText();
        String role = roleText.getText();
        String email = emailText.getText();
        String phone = phoneText.getText();
        String address = addressText.getText();
        String password = passwordText.getText();

        try (Connection con = Database.connection()) {
            String query = "INSERT INTO members (Name, Email, Phone, Address, Role, Password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(6, password);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);
            pstmt.setString(5, role);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Member added successfully!");
            } else {
                System.out.println("Failed to add the member.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
