import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginAdminFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private ImageIcon icon;

    public LoginAdminFrame() {
        setTitle("Admin Login");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        icon = new ImageIcon("C:/Users/Alex/Documents/OOP LAB/p/images/2.png/");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setLayout(null);
        initializeComponents();
    }

    private void initializeComponents() {
        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 140, 80, 25);
        add(usernameLabel);

        // Username field
        usernameField = new JTextField(20);
        usernameField.setBounds(140, 140, 200, 25);
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128), 1));
        add(usernameField);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 180, 80, 25);
        add(passwordLabel);

        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setBounds(140, 180, 200, 25);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128), 1));
        add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setBounds(140, 220, 200, 35);
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        add(loginButton);

        // Message label
        messageLabel = new JLabel();
        messageLabel.setBounds(50, 270, 300, 25);
        messageLabel.setForeground(Color.RED);
        add(messageLabel);

        // Adding action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection con = Database.connection()) {
            String query = "SELECT * FROM members WHERE Name=? AND Password=? AND Role='Admin'";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int adminID = rs.getInt("ID");
                new AdminFrame(adminID);
                dispose();
            } else {
                messageLabel.setText("Invalid username or password");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText("An error occurred. Please try again.");
        }
    }
}
