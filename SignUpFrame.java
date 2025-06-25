import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignUpFrame extends JFrame {
    private JTextField nameField, emailField, phoneField, addressField, usernameField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JLabel messageLabel;
    private ImageIcon icon;

    public SignUpFrame() {
        setTitle("User Sign Up");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        icon = new ImageIcon("C:/Users/Alex/Documents/OOP LAB/p/images/2.png/");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setLayout(null);
        initializeComponents();
    }

    private void initializeComponents() {
        // Name label and field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 50, 80, 25);
        add(nameLabel);
        nameField = new JTextField(20);
        nameField.setBounds(140, 50, 200, 25);
        add(nameField);

        // Email label and field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 90, 80, 25);
        add(emailLabel);
        emailField = new JTextField(20);
        emailField.setBounds(140, 90, 200, 25);
        add(emailField);

        // Phone label and field
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 130, 80, 25);
        add(phoneLabel);
        phoneField = new JTextField(20);
        phoneField.setBounds(140, 130, 200, 25);
        add(phoneField);

        // Address label and field
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(50, 170, 80, 25);
        add(addressLabel);
        addressField = new JTextField(20);
        addressField.setBounds(140, 170, 200, 25);
        add(addressField);

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 210, 80, 25);
        add(usernameLabel);
        usernameField = new JTextField(20);
        usernameField.setBounds(140, 210, 200, 25);
        add(usernameField);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 250, 80, 25);
        add(passwordLabel);
        passwordField = new JPasswordField(20);
        passwordField.setBounds(140, 250, 200, 25);
        add(passwordField);

        // Sign-up button
        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(140, 290, 200, 35);
        signUpButton.setBackground(new Color(70, 130, 180));
        signUpButton.setForeground(Color.WHITE);
        add(signUpButton);

        // Message label
        messageLabel = new JLabel();
        messageLabel.setBounds(50, 340, 300, 25);
        messageLabel.setForeground(Color.RED);
        add(messageLabel);

        // Adding action listener for the sign-up button
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });
    }

    private void handleSignUp() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection con = Database.connection()) {
            String query = "INSERT INTO members (Name, Email, Phone, Address, UserName, Password, Role) VALUES (?, ?, ?, ?, ?, ?, 'User')";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);
            pstmt.setString(5, username);
            pstmt.setString(6, password);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                messageLabel.setText("Sign up successful! Please log in.");
                new LogingUserFrame().setVisible(true);
                dispose();
            } else {
                messageLabel.setText("Failed to sign up. Please try again.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText("An error occurred. Please try again.");
        }
    }

}
