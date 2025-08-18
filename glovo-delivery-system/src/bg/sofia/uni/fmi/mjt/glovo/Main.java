package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Main {
    @SuppressWarnings("checkstyle:MethodLength")
    public static void main(String[] args) {
        char[][] mapLayout = {
            {'#', '#', '#', '.', '#'},
            {'#', '.', 'B', 'R', '.'},
            {'.', '.', '#', '.', '#'},
            {'#', 'C', '.', 'A', '.'},
            {'#', '.', '#', '#', '#'}
        };

        Glovo glovo = new Glovo(mapLayout);

        // Define locations
        MapEntity restaurant = new MapEntity(new Location(1, 3), MapEntityType.RESTAURANT);
        MapEntity client = new MapEntity(new Location(3, 1), MapEntityType.CLIENT);
        String foodItem = "Pizza Margherita";

        try {
            // Test cheapest delivery
            System.out.println("Testing cheapest delivery:");
            Delivery cheapDelivery = glovo.getCheapestDelivery(client, restaurant, foodItem);
            printDeliveryInfo(cheapDelivery);

            // Test fastest delivery
            System.out.println("\nTesting fastest delivery:");
            Delivery fastDelivery = glovo.getFastestDelivery(client, restaurant, foodItem);
            printDeliveryInfo(fastDelivery);

            // Test fastest under price
            System.out.println("\nTesting fastest delivery under 20:");
            Delivery fastUnder20 = glovo.getFastestDeliveryUnderPrice(client, restaurant, foodItem, 20);
            printDeliveryInfo(fastUnder20);

            // Test cheapest within time limit
            System.out.println("\nTesting cheapest delivery within 15 minutes:");
            Delivery cheapWithin15 = glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, foodItem, 15);
            printDeliveryInfo(cheapWithin15);

        } catch (InvalidOrderException | NoAvailableDeliveryGuyException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void printDeliveryInfo(Delivery delivery) {
        System.out.println("Food: " + delivery.foodItem());
        System.out.println("From restaurant at: " + delivery.restaurant());
        System.out.println("To client at: " + delivery.client());
        System.out.println("Delivery guy at: " + delivery.deliveryGuy());
        System.out.printf("Price: %.2f\n", delivery.price());
        System.out.println("Estimated time: " + delivery.estimatedTime() + " minutes");
    }
}