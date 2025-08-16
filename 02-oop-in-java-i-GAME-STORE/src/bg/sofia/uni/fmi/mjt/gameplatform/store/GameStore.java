package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class GameStore implements StoreAPI {
    private StoreItem[] availableItems;

    static final String weakPromoCode = "VAN40";
    static final String strongPromoCode = "100YO";

    public GameStore(StoreItem[] availableItems) {
        this.availableItems = Arrays.copyOf(availableItems, availableItems.length);
    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        StoreItem[] matched = new StoreItem[availableItems.length];
        int pos = 0;

        for (StoreItem item : availableItems) {
            boolean matchesAll = true;
            for (ItemFilter filter : itemFilters) {
                if (!filter.matches(item)) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) {
                matched[pos++] = item;
            }
        }
        return Arrays.copyOf(matched, pos);
    }

    @Override
    public void applyDiscount(String promoCode) {
        if (promoCode.equals(weakPromoCode)) {
            for (StoreItem item : availableItems) {
                BigDecimal price = item.getPrice();
                BigDecimal discount = price.multiply(new BigDecimal("0.4"));
                item.setPrice(price.subtract(discount).setScale(2, RoundingMode.HALF_UP));
            }
        } else if (promoCode.equals(strongPromoCode)) {
            for (StoreItem item : availableItems) {
                item.setPrice(BigDecimal.ZERO.setScale(2));
            }
        }
    }

    @Override
    public boolean rateItem(StoreItem item, double rating) {
        if (rating < 1 || rating > 5) {
            return false;
        }
        for (StoreItem storeItem : availableItems) {
            if (storeItem.equals(item)) {
                storeItem.rate(rating);
                return true;
            }
        }
        return false;
    }

}
