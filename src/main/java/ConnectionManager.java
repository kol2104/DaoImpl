import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static Connection con;

    public static Connection getConnection () {
        if (con == null) {
            try {
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testjdbc", "postgres", "qwerty");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return con;
    }
}
