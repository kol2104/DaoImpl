import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<User> {

    private static final String GET_USER_BY_ID = "select * from users where user_id=?";
    private static final String DELETE_USER_BY_ID = "delete from users where user_id=?";
    private static final String UPDATE_USER_BY_ID = "update users set user_id=?,user_name=?,mail=? where user_id=?";
    private static final String INSERT_INTO_USERS = "insert into users (user_id, user_name, mail) values (?, ?, ?)";
    private Connection con;

    public UserDao () {
        con = ConnectionManager.getConnection();
    }

    @Override
    public Optional<User> get(int id) {
        try (PreparedStatement pstnt = con.prepareStatement(GET_USER_BY_ID)) {
            pstnt.setInt(1, id);

            ResultSet res = pstnt.executeQuery();
            while (res.next()) {
                User user = new User();
                user.setUserId(res.getInt("user_id"));
                user.setUserName(res.getString("user_name"));
                user.setMail(res.getString("mail"));
                return Optional.of(user);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        try (Statement stnt = con.createStatement()) {
            ResultSet res = stnt.executeQuery("select * from users");
            while (res.next()) {
                User user = new User();
                user.setUserId(res.getInt("user_id"));
                user.setUserName(res.getString("user_name"));
                user.setMail(res.getString("mail"));
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    @Override
    public void save(User user) {
        try (PreparedStatement pstnt = con.prepareStatement(INSERT_INTO_USERS)) {
            pstnt.setInt(1, user.getUserId());
            pstnt.setString(2, user.getUserName());
            pstnt.setString(3, user.getMail());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(User user, String[] params) {
        try (PreparedStatement pstnt = con.prepareStatement(UPDATE_USER_BY_ID)) {
            pstnt.setInt(1, Integer.parseInt(params[0]));
            pstnt.setString(2, params[1]);
            pstnt.setString(3, params[2]);
            pstnt.setInt(4, user.getUserId());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(User user) {
        try (PreparedStatement pstnt = con.prepareStatement(DELETE_USER_BY_ID)) {
            pstnt.setInt(1, user.getUserId());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
