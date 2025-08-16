package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.Game;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.PriceItemFilter;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.TitleItemFilter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GameStoreTest {
    public static void main(String[] args) {
        Game cyberpunk = new Game(
            "Cyberpunk 2077",
            new BigDecimal("59.99"),
            LocalDateTime.of(2020, 12, 10, 0, 0),
            "RPG"
        );

        cyberpunk.rate(4.5);
        cyberpunk.rate(3.5);
        System.out.println("Average Rating: " + cyberpunk.getRating());

        cyberpunk.setPrice(new BigDecimal("59.999"));
        System.out.println("Price: " + cyberpunk.getPrice());

        StoreItem[] games = {
            new Game("Cyberpunk 2077", new BigDecimal("59.99"), LocalDateTime.now(), "RPG"),
            new Game("The Witcher 3", new BigDecimal("29.99"), LocalDateTime.now(), "RPG"),
            new Game("Minecraft", new BigDecimal("19.99"), LocalDateTime.now(), "Sandbox")
        };

        GameStore store = new GameStore(games);

        System.out.println("--- Items under $30 ---");
        ItemFilter priceFilter = new PriceItemFilter(BigDecimal.ZERO, new BigDecimal("30.00"));
        StoreItem[] cheapItems = store.findItemByFilters(new ItemFilter[] {priceFilter});
        for (StoreItem item : cheapItems) {
            System.out.println(item.getTitle() + " | Price: " + item.getPrice());
        }

        System.out.println("\n--- After 40% Discount (VAN40) ---");
        store.applyDiscount("VAN40");
        for (StoreItem item : games) {
            System.out.println(item.getTitle() + " | New Price: " + item.getPrice());
        }

        System.out.println("\n--- Rating 'The Witcher 3' ---");
        boolean ratingSuccess = store.rateItem(games[1], 5);
        System.out.println("Rating successful? " + ratingSuccess);
        System.out.println("New rating: " + games[1].getRating());

        System.out.println("\n--- Searching for 'witcher' (Case-Insensitive) ---");
        ItemFilter titleFilter = new TitleItemFilter("witcher", false);
        StoreItem[] witcherGames = store.findItemByFilters(new ItemFilter[] {titleFilter});
        for (StoreItem item : witcherGames) {
            System.out.println(item.getTitle() + " | Price: " + item.getPrice());
        }
    }

}
