import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBookPanel extends JPanel {
    private JLabel id, name, author, price, year, imagePath;
    private JTextField idText, nameText, authorText, priceText, yearText, imagePathText;
    private JButton addBookButton, deleteBookButton, editBookButton;
    private int adminId;

    public AddBookPanel(int adminId) {
        this.adminId = adminId;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        addBookButton = createButton("Add Book", gbc);
        deleteBookButton = createButton("Delete Book", gbc);
        editBookButton = createButton("Edit Book", gbc);
    }

    private JButton createButton(String text, GridBagConstraints gbc) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addBookButton) {
                    showAddBookDialog();
                } else if (e.getSource() == deleteBookButton) {
                    showDeleteBookDialog();
                } else if (e.getSource() == editBookButton) {
                    showEditBookDialog();
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(button, gbc);
        return button;
    }

    private void showAddBookDialog() {
        JFrame addFrame = new JFrame("Add Book");
        addFrame.setSize(300, 400);

        JPanel addPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        id = new JLabel("ID");
        idText = new JTextField(20);
        addComponent(addPanel, id, idText, gbc);

        name = new JLabel("Name");
        nameText = new JTextField(20);
        addComponent(addPanel, name, nameText, gbc);

        author = new JLabel("Author");
        authorText = new JTextField(20);
        addComponent(addPanel, author, authorText, gbc);

        price = new JLabel("Price");
        priceText = new JTextField(20);
        addComponent(addPanel, price, priceText, gbc);

        year = new JLabel("Date Issued");
        yearText = new JTextField(20);
        addComponent(addPanel, year, yearText, gbc);

        imagePath = new JLabel("Image Path");
        imagePathText = new JTextField(20);
        addComponent(addPanel, imagePath, imagePathText, gbc);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookToDatabase();
            }
        });
        addComponent(addPanel, new JLabel(), addButton, gbc);

        addFrame.add(addPanel);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setVisible(true);
    }

    private void showDeleteBookDialog() {
        JFrame deleteFrame = new JFrame("Delete Book");
        deleteFrame.setSize(300, 200);

        JPanel deletePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        id = new JLabel("ID");
        idText = new JTextField(20);
        addComponent(deletePanel, id, idText, gbc);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBookFromDatabase();
            }
        });
        addComponent(deletePanel, new JLabel(), deleteButton, gbc);

        deleteFrame.add(deletePanel);
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteFrame.setVisible(true);
    }

    private void showEditBookDialog() {
        JFrame editFrame = new JFrame("Edit Book");
        editFrame.setSize(300, 400);

        JPanel editPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;

        id = new JLabel("ID");
        idText = new JTextField(20);
        addComponent(editPanel, id, idText, gbc);

        name = new JLabel("Name");
        nameText = new JTextField(20);
        addComponent(editPanel, name, nameText, gbc);

        author = new JLabel("Author");
        authorText = new JTextField(20);
        addComponent(editPanel, author, authorText, gbc);

        price = new JLabel("Price");
        priceText = new JTextField(20);
        addComponent(editPanel, price, priceText, gbc);

        year = new JLabel("Date Issued");
        yearText = new JTextField(20);
        addComponent(editPanel, year, yearText, gbc);

        imagePath = new JLabel("Image Path");
        imagePathText = new JTextField(20);
        addComponent(editPanel, imagePath, imagePathText, gbc);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editBookInDatabase();
            }
        });
        addComponent(editPanel, new JLabel(), editButton, gbc);

        editFrame.add(editPanel);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setVisible(true);
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

    private void addBookToDatabase() {
        String bookId = idText.getText();
        String bookName = nameText.getText();
        String bookAuthor = authorText.getText();
        String bookPrice = priceText.getText();
        String bookYear = yearText.getText();
        String bookImagePath = imagePathText.getText();

        try (Connection con = Database.connection()) {
            String query = "INSERT INTO books (ID, Name, Author, Price, Date_Issued, Image_Path) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, bookId);
                ps.setString(2, bookName);
                ps.setString(3, bookAuthor);
                ps.setString(4, bookPrice);
                ps.setString(5, bookYear); // Changed to Date_Issued in the new schema
                ps.setString(6, bookImagePath);
                ps.executeUpdate();
                logAdminActivity(adminId, "Added book with ID " + bookId);
                JOptionPane.showMessageDialog(this, "Book added successfully.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage());
        }
    }

    private void deleteBookFromDatabase() {
        String bookId = idText.getText();

        try (Connection con = Database.connection()) {
            // Copy book to deleted_books table before deleting
            String copyQuery = "INSERT INTO deleted_books (ID, Name, Author, Price, Date_Issued, image_Path) SELECT ID, Name, Author, Price, Date_Issued, image_Path FROM books WHERE ID = ?";
            try (PreparedStatement psCopy = con.prepareStatement(copyQuery)) {
                psCopy.setString(1, bookId);
                psCopy.executeUpdate();
            }

            String query = "DELETE FROM books WHERE ID = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, bookId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    logAdminActivity(adminId, "Deleted book with ID " + bookId);
                    JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Book ID not found.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting book: " + ex.getMessage());
        }
    }

    private void editBookInDatabase() {
        String bookId = idText.getText();
        String bookName = nameText.getText();
        String bookAuthor = authorText.getText();
        String bookPrice = priceText.getText();
        String bookYear = yearText.getText();
        String bookImagePath  = imagePathText.getText();

        try (Connection con = Database.connection()) {
            String query = "UPDATE books SET Name = ?, Author = ?, Price = ?, Date_Issued = ?, Image_Path = ? WHERE ID = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, bookName);
                ps.setString(2, bookAuthor);
                ps.setString(3, bookPrice);
                ps.setString(4, bookYear); // Changed to Date_Issued in the new schema
                ps.setString(7, bookImagePath);
                ps.setString(8, bookId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    logAdminActivity(adminId, "Edited book with ID " + bookId);
                    JOptionPane.showMessageDialog(this, "Book updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Book ID not found.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + ex.getMessage());
        }
    }

    private void logAdminActivity(int adminId, String activity) {
        try (Connection con = Database.connection()) {
            String query = "INSERT INTO admin_activities (admin_id, activity) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, adminId);
                ps.setString(2, activity);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
