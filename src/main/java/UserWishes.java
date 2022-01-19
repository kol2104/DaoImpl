import lombok.Data;

@Data
public class UserWishes {
    Integer userWishesId;
    User user;
    String searchString;
    PriceSettings priceSettings;
}
