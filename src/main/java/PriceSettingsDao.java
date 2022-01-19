import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PriceSettingsDao implements Dao<PriceSettings> {

    private static final String GET_PRICE_SETTINGS_BY_ID = "select * from price_settings where price_settings_id=?";
    private static final String DELETE_PRICE_SETTINGS_BY_ID = "delete from price_settings where price_settings_id=?";
    private static final String UPDATE_PRICE_SETTINGS_BY_ID = "update price_settings set price_settings_id=?,string_matcher=? where price_settings_id=?";
    private static final String INSERT_INTO_PRICE_SETTINGS = "insert into price_settings (price_settings_id, string_matcher) values (?, ?)";
    private Connection con;

    public PriceSettingsDao() {
        con = ConnectionManager.getConnection();
    }

    @Override
    public Optional<PriceSettings> get(int id) {
        try (PreparedStatement pstnt = con.prepareStatement(GET_PRICE_SETTINGS_BY_ID)) {
            pstnt.setInt(1, id);

            ResultSet res = pstnt.executeQuery();
            while (res.next()) {
                PriceSettings priceSettings = new PriceSettings();
                priceSettings.setPriceSettingsId(res.getInt("price_settings_id"));
                priceSettings.setStringMatcher(res.getString("string_matcher"));
                return Optional.of(priceSettings);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<PriceSettings> getAll() {
        List<PriceSettings> priceSettingsList = new ArrayList<>();

        try (Statement stnt = con.createStatement()) {
            ResultSet res = stnt.executeQuery("select * from price_settings");
            while (res.next()) {
                PriceSettings priceSettings = new PriceSettings();
                priceSettings.setPriceSettingsId(res.getInt("price_settings_id"));
                priceSettings.setStringMatcher(res.getString("string_matcher"));
                priceSettingsList.add(priceSettings);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return priceSettingsList;
    }

    @Override
    public void save(PriceSettings priceSettings) {
        try (PreparedStatement pstnt = con.prepareStatement(INSERT_INTO_PRICE_SETTINGS)) {
            pstnt.setInt(1, priceSettings.getPriceSettingsId());
            pstnt.setString(2, priceSettings.getStringMatcher());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(PriceSettings priceSettings, String[] params) {
        try (PreparedStatement pstnt = con.prepareStatement(UPDATE_PRICE_SETTINGS_BY_ID)) {
            pstnt.setInt(1, Integer.parseInt(params[0]));
            pstnt.setString(2, params[1]);
            pstnt.setInt(3, priceSettings.getPriceSettingsId());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void delete(PriceSettings priceSettings) {
        try (PreparedStatement pstnt = con.prepareStatement(DELETE_PRICE_SETTINGS_BY_ID)) {
            pstnt.setInt(1, priceSettings.getPriceSettingsId());
            pstnt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
