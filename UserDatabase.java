package mindcare;
import java.sql.*;

public class UserDatabase {
   private static final String URL =
"jdbc:mysql://localhost:3306/mindcare_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root123";

    public static boolean addUser(String firstName, String lastName, String age, String gender, String dob, String username, String password) {
        String query = "INSERT INTO users (first_name, last_name, age, gender, dob, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);

            // ✅ FIX: convert age to INT (matches DB schema)
            pstmt.setInt(3, Integer.parseInt(age));

            pstmt.setString(4, gender);
            pstmt.setString(5, dob);
            pstmt.setString(6, username);
            pstmt.setString(7, password);

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username already exists!");
            return false;

        } catch (Exception e) {
            // ✅ FIX: print FULL error (very important)
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String[] getUserDetails(String username) {
        String[] details = new String[5];
        String query = "SELECT first_name, last_name, age, gender, username FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                details[0] = rs.getString("first_name");
                details[1] = rs.getString("last_name");
                details[2] = String.valueOf(rs.getInt("age")); // ✅ proper retrieval
                details[3] = rs.getString("gender");
                details[4] = rs.getString("username");
                return details;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}