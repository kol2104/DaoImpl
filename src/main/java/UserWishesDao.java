import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserWishesDao implements Dao<UserWishes> {

    private static final String GET_USER_WISHES_BY_ID = "select * from user_wishes where user_wishes_id=?";
    private static final String DELETE_USER_WISHES_BY_ID = "delete from user_wishes where user_wishes_id=?";
    private static final String UPDATE_USER_WISHES_BY_ID = "update user_wishes set user_wishes_id=?, user_id=?,search_string=?,price_settings_id=? where user_wishes_id=?";
    private static final String INSERT_INTO_USER_WISHES = "insert into user_wishes (user_wishes_id, user_id, search_string, price_settings_id) values (?, ?, ?, ?)";
    private Connection con;
    private UserDao userDao;
    private PriceSettingsDao priceSettingsDao;

    public UserWishesDao () {
        con = ConnectionManager.getConnection();
        userDao = new UserDao();
        priceSettingsDao = new PriceSettingsDao();
    }

    @Override
    public Optional<UserWishes> get(int id) {
        try (PreparedStatement pstnt = con.prepareStatement(GET_USER_WISHES_BY_ID)) {
            pstnt.setInt(1, id);

            ResultSet res = pstnt.executeQuery();
            while (res.next()) {
                UserWishes userWishes = new UserWishes();
                userWishes.setUserWishesId(res.getInt("user_wishes_id"));
                userWishes.setUser( userDao.get(res.getInt("user_id")).orElse(null) );
                userWishes.setSearchString(res.getString("search_string"));
                userWishes.setPriceSettings( priceSettingsDao.get(res.getInt("price_settings_id")).orElse(null) );
                return Optional.of(userWishes);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<UserWishes> getAll() {
        List<UserWishes> userWishesList = new ArrayList<>();

        try (Statement stnt = con.createStatement()) {
            ResultSet res = stnt.executeQuery("select * from user_wishes");
            while (res.next()) {
                UserWishes userWishes = new UserWishes();
                userWishes.setUserWishesId(res.getInt("user_wishes_id"));
                userWishes.setUser( userDao.get(res.getInt("user_id")).orElse(null) );
                userWishes.setSearchString(res.getString("search_string"));
                userWishes.setPriceSettings( priceSettingsDao.get(res.getInt("price_settings_id")).orElse(null) );
                userWishesList.add(userWishes);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userWishesList;
    }

    @Override
    public void save(UserWishes userWishes) {
        try (PreparedStatement pstnt = con.prepareStatement(INSERT_INTO_USER_WISHES)) {
            pstnt.setInt(1, userWishes.getUserWishesId());
            pstnt.setInt(2, userWishes.getUser().getUserId());
            pstnt.setString(3, userWishes.getSearchString());
            pstnt.setInt(4, userWishes.getPriceSettings().getPriceSettingsId());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(UserWishes userWishes, String[] params) {
        try (PreparedStatement pstnt = con.prepareStatement(UPDATE_USER_WISHES_BY_ID)) {
            pstnt.setInt(1, Integer.parseInt(params[0]));
            pstnt.setInt(2, Integer.parseInt(params[1]));
            pstnt.setString(3, params[2]);
            pstnt.setInt(4, Integer.parseInt(params[3]));
            pstnt.setInt(5, userWishes.getUserWishesId());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(UserWishes userWishes) {
        try (PreparedStatement pstnt = con.prepareStatement(DELETE_USER_WISHES_BY_ID)) {
            pstnt.setInt(1, userWishes.getUserWishesId());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
