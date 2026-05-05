import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mindcare_db";
        String user = "root";
        String pass = "root123"; // same as your UserDatabase

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connection SUCCESSFUL!");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Connection FAILED!");
            e.printStackTrace();
        }
    }
}