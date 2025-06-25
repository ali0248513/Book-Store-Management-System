import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JButton adminLoginButton;
    private JButton userLoginButton;
    private JLabel logoLabel;
    private ImageIcon icon;

    public LoginFrame() {
        setTitle("Library Management System - Select Login");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        icon = new ImageIcon("C:/Users/Alex/Documents/OOP LAB/p/images/2.png/");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setLayout(null);
        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        // Logo
        logoLabel = new JLabel(new ImageIcon("library_logo.png"));
        logoLabel.setBounds(100, 20, 200, 100);
        add(logoLabel);

        // Admin login button
        adminLoginButton = new JButton("Log in as Admin");
        adminLoginButton.setBounds(100, 150, 200, 50);
        adminLoginButton.setBackground(new Color(70, 130, 180));
        adminLoginButton.setForeground(Color.WHITE);
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginAdminFrame().setVisible(true);
                dispose();
            }
        });
        add(adminLoginButton);

        // User login button
        userLoginButton = new JButton("Log in as User");
        userLoginButton.setBounds(100, 220, 200, 50);
        userLoginButton.setBackground(new Color(70, 130, 180));
        userLoginButton.setForeground(Color.WHITE);
        userLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LogingUserFrame().setVisible(true);
                dispose();
            }
        });
        add(userLoginButton);
    }
}
