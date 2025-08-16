package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class DLC implements StoreItem {

    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private Game game;

    private double totalSumOfRatings;
    private int ratingsCount;

    public DLC(String title, BigDecimal price, LocalDateTime releaseDate, Game game) {
        this.title = title;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
        this.releaseDate = releaseDate;
        this.game = game;

        totalSumOfRatings = 0.0;
        ratingsCount = 0;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public double getRating() {
        return ratingsCount == 0 ? 0.0 : totalSumOfRatings / ratingsCount;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void rate(double rating) {
        totalSumOfRatings += rating;
        ratingsCount++;
    }

    public Game getGame() {
        return game;
    }

}
