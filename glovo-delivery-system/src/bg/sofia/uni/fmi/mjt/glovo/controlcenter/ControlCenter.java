package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ControlCenter implements ControlCenterApi {
    private final MapEntity[][] map;
    private final int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public ControlCenter(char[][] mapLayout) {
        this.map = createMap(mapLayout);
    }

    private MapEntity[][] createMap(char[][] layout) {
        MapEntity[][] newMap = new MapEntity[layout.length][layout[0].length];
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                newMap[i][j] = new MapEntity(new Location(i, j),
                    MapEntityType.fromSymbol(layout[i][j]));
            }
        }
        return newMap;
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurant,
                                               Location client, double maxPrice, int maxTime, ShippingMethod method) {
        validateLocations(restaurant, client);
        List<DeliveryInfo> deliveries = findValidDeliveries(restaurant, client, maxPrice, maxTime);
        return selectBestDelivery(deliveries, method);
    }

    private List<DeliveryInfo> findValidDeliveries(Location restaurant,
                                                   Location client, double maxPrice, int maxTime) {
        List<DeliveryInfo> validDeliveries = new ArrayList<>();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (isDeliveryGuy(map[i][j])) {
                    processDeliveryGuy(validDeliveries, map[i][j],
                        restaurant, client, maxPrice, maxTime);
                }
            }
        }
        return validDeliveries;
    }

    private void processDeliveryGuy(List<DeliveryInfo> deliveries,
                                    MapEntity guy, Location restaurant, Location client,
                                    double maxPrice, int maxTime) {
        DeliveryType type = getDeliveryType(guy);
        int toRestaurant = findPathLength(guy.location(), restaurant);
        int toClient = findPathLength(restaurant, client);

        if (toRestaurant != -1 && toClient != -1) {
            addValidDelivery(deliveries, guy.location(), type,
                toRestaurant + toClient, maxPrice, maxTime);
        }
    }

    private void addValidDelivery(List<DeliveryInfo> deliveries,
                                  Location guyLoc, DeliveryType type, int distance,
                                  double maxPrice, int maxTime) {
        double price = distance * type.getPricePerKm();
        int time = distance * type.getTimePerKm();

        if ((maxPrice == -1 || price <= maxPrice) &&
            (maxTime == -1 || time <= maxTime)) {
            deliveries.add(new DeliveryInfo(guyLoc, price, time, type));
        }
    }

    private int findPathLength(Location start, Location end) {
        if (start.equals(end)) return 0;

        boolean[][] visited = new boolean[map.length][map[0].length];
        Queue<PathNode> queue = new ArrayDeque<>();
        queue.add(new PathNode(start, 0));
        visited[start.x()][start.y()] = true;

        while (!queue.isEmpty()) {
            PathNode current = queue.poll();
            for (int[] dir : directions) {
                int x = current.location.x() + dir[0];
                int y = current.location.y() + dir[1];
                if (isValid(x, y)) {
                    if (x == end.x() && y == end.y()) {
                        return current.distance + 1;
                    }
                    if (!visited[x][y] && map[x][y].type() != MapEntityType.WALL) {
                        visited[x][y] = true;
                        queue.add(new PathNode(new Location(x, y), current.distance + 1));
                    }
                }
            }
        }
        return -1;
    }

    private DeliveryInfo selectBestDelivery(List<DeliveryInfo> deliveries, ShippingMethod method) {
        if (deliveries.isEmpty()) return null;

        DeliveryInfo best = deliveries.get(0);
        for (int i = 1; i < deliveries.size(); i++) {
            DeliveryInfo current = deliveries.get(i);
            if ((method == ShippingMethod.FASTEST && current.estimatedTime() < best.estimatedTime()) ||
                (method == ShippingMethod.CHEAPEST && current.price() < best.price())) {
                best = current;
            }
        }
        return best;
    }

    private boolean isDeliveryGuy(MapEntity entity) {
        return entity.type() == MapEntityType.DELIVERY_GUY_CAR ||
            entity.type() == MapEntityType.DELIVERY_GUY_BIKE;
    }

    private DeliveryType getDeliveryType(MapEntity entity) {
        return entity.type() == MapEntityType.DELIVERY_GUY_CAR ?
            DeliveryType.CAR : DeliveryType.BIKE;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }

    private void validateLocations(Location restaurant, Location client) {
        if (restaurant == null || client == null) {
            throw new IllegalArgumentException("Locations cannot be null");
        }
        if (!isValid(restaurant.x(), restaurant.y()) ||
            !isValid(client.x(), client.y())) {
            throw new IllegalArgumentException("Location out of bounds");
        }
        if (map[restaurant.x()][restaurant.y()].type() != MapEntityType.RESTAURANT ||
            map[client.x()][client.y()].type() != MapEntityType.CLIENT) {
            throw new IllegalArgumentException("Invalid location type");
        }
    }

    @Override
    public MapEntity[][] getLayout() {
        return map;
    }

    private static class PathNode {
        final Location location;
        final int distance;

        PathNode(Location location, int distance) {
            this.location = location;
            this.distance = distance;
        }
    }
}