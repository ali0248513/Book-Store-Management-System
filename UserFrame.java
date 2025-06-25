import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel bookPanel;
    private JPanel cartPanel;
    private JPanel sidebarPanel;
    private JPanel accountPanel;
    private JPanel historyPanel;
    private JPanel receiptPanel;

    private CardLayout cardLayout;
    private Connection connection;
    private List<JPanel> bookItems;
    private ImageIcon icon;

    private DefaultListModel<String> cartListModel;
    private JLabel totalCostLabel;
    private double totalCost = 0.0;

    private int userId ; // Replace with the actual logged-in user's ID

    UserFrame(int uID) {
        userId=uID;
        setTitle("Online Bookstore");
        setSize(1000, 600);
        icon = new ImageIcon("C:/Users/Alex/Documents/OOP LAB/p/images/2.png/");
        setIconImage(icon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        createSidebarPanel();
        connection = Database.connection(); // Initialize database connection first
        createBookPanel();
        createCartPanel();
        createAccountPanel();
        createHistoryPanel();
        createReceiptPanel();

        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(new JScrollPane(bookPanel), BorderLayout.CENTER);
        mainContentPanel.add(cartPanel, BorderLayout.EAST);

        mainPanel.add(mainContentPanel, "main");
        mainPanel.add(accountPanel, "account");
        mainPanel.add(historyPanel, "history");
        mainPanel.add(receiptPanel, "receipt");

        JPanel container = new JPanel(new BorderLayout());
        container.add(sidebarPanel, BorderLayout.WEST);
        container.add(mainPanel, BorderLayout.CENTER);

        add(container);

        setVisible(true);
    }

    private void createSidebarPanel() {
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBackground(new Color(43, 43, 43));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Online Bookstore");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        sidebarPanel.add(titleLabel);

        // Dropdown menu for categories
        JLabel categoryLabel = new JLabel("Select Category:");
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        sidebarPanel.add(categoryLabel);

        String[] categories = {"All", "Fiction", "Non-Fiction"};
        JComboBox<String> categoryDropdown = new JComboBox<>(categories);
        categoryDropdown.setMaximumSize(new Dimension(180, 30));
        categoryDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(categoryDropdown);

        // categoryDropdown.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         // Logic to filter books by selected category and display them
        //         String selectedCategory = (String) categoryDropdown.getSelectedItem();
        //         filterBooksByCategory(selectedCategory);
        //     }
        // });

        // Other Buttons
        String[] buttonLabels = {"Home", "Account", "History", "Sign Out"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setMaximumSize(new Dimension(180, 40));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBackground(new Color(60, 63, 65));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between buttons
            sidebarPanel.add(button);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleSidebarButtonClick(e.getActionCommand());
                }
            });
        }
    }

    private void handleSidebarButtonClick(String actionCommand) {
        switch (actionCommand) {
            case "Home":
                cardLayout.show(mainPanel, "main");
                break;
            case "Account":
                cardLayout.show(mainPanel, "account");
                break;
            case "History":
                cardLayout.show(mainPanel, "history");
                break;
            case "Sign Out":
                JOptionPane.showMessageDialog(UserFrame.this, "Logging out");
                dispose();
                new LoginFrame().setVisible(true);
                break;
            default:
                break;
        }
    }

    private void createBookPanel() {
        bookPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        bookPanel.setBackground(Color.WHITE);
        bookPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bookItems = new ArrayList<>();
        loadBooksFromDatabase(); // Load books from the database
    }

    private void loadBooksFromDatabase() {
        String query = "SELECT * FROM books";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                String author = rs.getString("Author");
                double price = rs.getDouble("Price");
                String dateIssued = rs.getString("date_Issued");
                String imagePath = rs.getString("image_Path");

                System.out.println("Book ID: " + id);
                System.out.println("Book Name: " + name);
                System.out.println("Book Image Path: " + imagePath); // Debug statement

                // Print image loading debug information
                ImageIcon imageIcon = new ImageIcon(imagePath);
                if (imageIcon.getImage() == null) {
                    System.out.println("Error loading image: " + imagePath);
                } else {
                    System.out.println("Image loaded successfully: " + imagePath);
                }

                JPanel bookItem = createBookItemPanel(id, name, author, price, dateIssued, imagePath);
                bookItems.add(bookItem);
                bookPanel.add(bookItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createBookItemPanel(int id, String name, String author, double price, String dateIssued, String imagePath) {
        JPanel bookItem = new JPanel(new BorderLayout());
        bookItem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        bookItem.setPreferredSize(new Dimension(150, 200));

        // Load the image
        ImageIcon imageIcon = null;
        try {
            imageIcon = new ImageIcon(imagePath);
            Image image = imageIcon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(image);
        } catch (Exception e) {
            System.out.println("Error loading image: " + imagePath);
            imageIcon = new ImageIcon(); // Placeholder or empty icon in case of error
        }

        JLabel bookImage = new JLabel(imageIcon); // Use scaled ImageIcon
        JLabel bookTitle = new JLabel(name);
        bookTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel bookPriceLabel = new JLabel("$" + price);
        bookPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBackground(new Color(128, 128, 128));
        addToCartButton.setForeground(Color.WHITE);
        JButton buyButton = new JButton("Buy");
        buyButton.setBackground(new Color(255, 69, 0));
        buyButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(addToCartButton);
        buttonPanel.add(buyButton);

        bookItem.add(bookImage, BorderLayout.CENTER);
        bookItem.add(bookTitle, BorderLayout.NORTH);
        bookItem.add(bookPriceLabel, BorderLayout.SOUTH);
        bookItem.add(buttonPanel, BorderLayout.SOUTH);

        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookTitleText = bookTitle.getText();
                String bookPriceText = bookPriceLabel.getText();
                addBookToCart(bookTitleText, bookPriceText);
            }
        });

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the purchase of the book
                handleBuyBook(id, name, price, imagePath);
            }
        });

        return bookItem;
    }

    private void createCartPanel() {
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setPreferredSize(new Dimension(250, 0));
        cartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel cartTitle = new JLabel("Shopping Cart");
        cartTitle.setFont(new Font("Arial", Font.BOLD, 16));
        cartPanel.add(cartTitle);

        cartListModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartListModel);
        cartList.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartPanel.add(new JScrollPane(cartList));

        totalCostLabel = new JLabel("Total Cost: $0.00");
        totalCostLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cartPanel.add(totalCostLabel);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBackground(new Color(255, 69, 0));
        checkoutButton.setForeground(Color.WHITE);
        cartPanel.add(checkoutButton);

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCheckout();
            }
        });
    }

    private void addBookToCart(String bookTitle, String bookPrice) {
        cartListModel.addElement(bookTitle + " - " + bookPrice);
        double price = Double.parseDouble(bookPrice.replace("$", ""));
        totalCost += price;
        totalCostLabel.setText("Total Cost: $" + String.format("%.2f", totalCost));
    }

    private void handleBuyBook(int bookId, String bookTitle, double price, String imagePath) {
        // Display the receipt panel with the book details and image
        displayReceiptPanel(bookId, bookTitle, price, imagePath);

        // Add the book to the purchase history
        addBookToPurchaseHistory(bookId, price);
    }

    private void handleCheckout() {
        // Add all items in the cart to the purchase history
        for (int i = 0; i < cartListModel.size(); i++) {
            String cartItem = cartListModel.getElementAt(i);
            String[] parts = cartItem.split(" - ");
            String bookTitle = parts[0];
            double bookPrice = Double.parseDouble(parts[1].replace("$", ""));

            // You need to get the book ID from the database based on the book title
            int bookId = getBookIdByTitle(bookTitle);

            addBookToPurchaseHistory(bookId, bookPrice);
        }

        JOptionPane.showMessageDialog(this, "Proceeding to checkout with total cost: $" + String.format("%.2f", totalCost));

        // Clear the cart after checkout
        cartListModel.clear();
        totalCost = 0.0;
        totalCostLabel.setText("Total Cost: $0.00");
    }

    private void addBookToPurchaseHistory(int bookId, double price) {
        String query = "INSERT INTO purchase_history (user_id, book_id, purchase_date) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getBookIdByTitle(String bookTitle) {
        String query = "SELECT ID FROM books WHERE Name = ?";
        int bookId = -1;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, bookTitle);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                bookId = rs.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookId;
    }

    private void displayReceiptPanel(int bookId, String bookTitle, double price, String imagePath) {
        receiptPanel.removeAll();

        JLabel titleLabel = new JLabel("Receipt");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel bookImage = new JLabel(new ImageIcon(imagePath));
        JLabel bookDetails = new JLabel("<html>Book: " + bookTitle + "<br>Price: $" + price + "</html>");
        bookDetails.setHorizontalAlignment(SwingConstants.CENTER);

        receiptPanel.setLayout(new BorderLayout());
        receiptPanel.add(titleLabel, BorderLayout.NORTH);
        receiptPanel.add(bookImage, BorderLayout.CENTER);
        receiptPanel.add(bookDetails, BorderLayout.SOUTH);

        cardLayout.show(mainPanel, "receipt");

        JOptionPane.showMessageDialog(this, "Thank you for purchasing: " + bookTitle + " for $" + price);
    }

    private void createAccountPanel() {
        accountPanel = new AccountPanel(userId); // Pass the database connection
    }

    private void createHistoryPanel() {
        historyPanel = new PurchaseHistoryPanel(userId); // Pass the database connection
    }

    private void createReceiptPanel() {
        receiptPanel = new JPanel();
        receiptPanel.setBackground(Color.WHITE);
    }
}
