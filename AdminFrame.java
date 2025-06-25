import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AdminFrame implements ActionListener {
    private JFrame frame;
    private JPanel sidePanel, contentPanel;
    private JLabel welcome;
    private JButton dashBoard, addBook, addMember, report, search, showMembers; // Added showMembers button

    private AddBookPanel addBookPanel;
    private AddMemberPanel addMemberPanel;
    private AddSearchPanel addSearchPanel;
    private AddDashPanel addDashPanel;
    private AddReportPanel addReportPanel;
    private AddShowMemberPanel addMembersPanel; // Panel to show all members
    private int aID;
    private ImageIcon icon;

    public AdminFrame(int adminID) {
        aID=adminID;

        frame = new JFrame("Admin");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        icon = new ImageIcon("C:/Users/Alex/Documents/OOP LAB/p/images/2.png/");
        frame.setIconImage(icon.getImage());

        sidePanel = new JPanel();
        sidePanel.setBackground(Color.DARK_GRAY);
        sidePanel.setPreferredSize(new Dimension(200, frame.getHeight()));
        sidePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        dashBoard = createButton("Dashboard", 0);
        addBook = createButton("Book Management", 1);
        addMember = createButton("Add Member", 2);
        report = createButton("Report", 3);
        search = createButton("Search", 4);
        showMembers = createButton("Show Members", 5); // Create showMembers button

        welcome = new JLabel("<html><h1>WELCOME</h1></html>", JLabel.CENTER);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(welcome, BorderLayout.CENTER);

        frame.add(sidePanel, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JButton createButton(String text, int y) {
        JButton button = new JButton(text);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = y;
        sidePanel.add(button, gbc);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dashBoard) {
            showDashboard();
        } else if (e.getSource() == addBook) {
            showAddBook();
        } else if (e.getSource() == addMember) {
            showAddMember();
        } else if (e.getSource() == report) {
            showReport();
        } else if (e.getSource() == search) {
            showSearch();
        } else if (e.getSource() == showMembers) { // Handle showMembers button
            showMembers();
        }
    }

    private void showDashboard() {
        clearContentPanel();
        if (addDashPanel == null) {
            addDashPanel = new AddDashPanel();
        }
        updateContentPanel(addDashPanel);
    }

    private void showAddBook() {
        clearContentPanel();
        if (addBookPanel == null) {
            addBookPanel = new AddBookPanel(aID);
        }
        updateContentPanel(addBookPanel);
    }

    private void showAddMember() {
        clearContentPanel();
        if (addMemberPanel == null) {
            addMemberPanel = new AddMemberPanel();
        }
        updateContentPanel(addMemberPanel);
    }

    private void showReport() {
        clearContentPanel();
        if (addReportPanel == null) {
            addReportPanel = new AddReportPanel();
        }
        updateContentPanel(addReportPanel);
    }

    private void showSearch() {
        clearContentPanel();
        if (addSearchPanel == null) {
            addSearchPanel = new AddSearchPanel(this);
        }
        updateContentPanel(addSearchPanel);
    }

    private void showMembers() {
        clearContentPanel();
        if (addMembersPanel == null) {
            addMembersPanel = new AddShowMemberPanel();
        }
        updateContentPanel(addMembersPanel);
    }

    private void clearContentPanel() {
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    void updateContentPanel(JPanel panel) {
        contentPanel.add(panel, BorderLayout.CENTER);
        frame.validate();
    }

    public void updateTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.SOUTH);
        frame.validate();
    }
}
