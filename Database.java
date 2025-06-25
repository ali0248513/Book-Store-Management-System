
import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    static Connection con = null;

    public static Connection connection() {
        String url = "jdbc:mysql://localhost:3306/bookstore";
        String user = "root";
        String password = "0248513";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected");
        } catch (Exception e) {
            System.out.println("Not Connected");
            e.printStackTrace();
        }
        return con;
    }
}
