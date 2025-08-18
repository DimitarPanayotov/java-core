package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Glovo implements GlovoApi {
    private final ControlCenter controlCenter;

    public Glovo(char[][] mapLayout) {
        this.controlCenter = new ControlCenter(mapLayout);
    }

    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws InvalidOrderException, NoAvailableDeliveryGuyException {
        validateOrder(client, restaurant, foodItem);
        return createDelivery(client, restaurant, foodItem, -1, -1, ShippingMethod.CHEAPEST);
    }

    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws InvalidOrderException, NoAvailableDeliveryGuyException {
        validateOrder(client, restaurant, foodItem);
        return createDelivery(client, restaurant, foodItem, -1, -1, ShippingMethod.FASTEST);
    }

    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant,
                                                 String foodItem, double maxPrice)
        throws InvalidOrderException, NoAvailableDeliveryGuyException {
        validateOrder(client, restaurant, foodItem);
        if (maxPrice <= 0) throw new InvalidOrderException("Max price must be positive");
        return createDelivery(client, restaurant, foodItem, maxPrice, -1, ShippingMethod.FASTEST);
    }

    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant,
                                                       String foodItem, int maxTime)
        throws InvalidOrderException, NoAvailableDeliveryGuyException {
        validateOrder(client, restaurant, foodItem);
        if (maxTime <= 0) throw new InvalidOrderException("Max time must be positive");
        return createDelivery(client, restaurant, foodItem, -1, maxTime, ShippingMethod.CHEAPEST);
    }

    private Delivery createDelivery(MapEntity client, MapEntity restaurant, String foodItem,
                                    double maxPrice, int maxTime, ShippingMethod method)
        throws NoAvailableDeliveryGuyException {
        DeliveryInfo info = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(), client.location(), maxPrice, maxTime, method);

        if (info == null) {
            throw new NoAvailableDeliveryGuyException("No available delivery for these constraints");
        }

        return new Delivery(
            client.location(),
            restaurant.location(),
            info.deliveryGuyLocation(),
            foodItem,
            info.price(),
            info.estimatedTime()
        );
    }

    private void validateOrder(MapEntity client, MapEntity restaurant, String foodItem)
        throws InvalidOrderException {
        if (client == null || restaurant == null || foodItem == null || foodItem.isBlank()) {
            throw new InvalidOrderException("Invalid order parameters");
        }

        try {
            if (!isClient(client) || !isRestaurant(restaurant)) {
                throw new InvalidOrderException("Invalid client or restaurant location");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidOrderException("Location out of map bounds");
        }
    }

    private boolean isClient(MapEntity entity) {
        return controlCenter.getLayout()[entity.location().x()][entity.location().y()].type()
            .equals(MapEntityType.CLIENT);
    }

    private boolean isRestaurant(MapEntity entity) {
        return controlCenter.getLayout()[entity.location().x()][entity.location().y()].type()
            .equals(MapEntityType.RESTAURANT);
    }
}