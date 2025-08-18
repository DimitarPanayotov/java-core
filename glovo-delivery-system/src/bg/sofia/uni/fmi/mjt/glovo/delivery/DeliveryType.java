package bg.sofia.uni.fmi.mjt.glovo.delivery;

public enum DeliveryType {
    CAR(5, 3),
    BIKE(3, 5);

    private final double pricePerKm;
    private final int timePerKm;

    DeliveryType(int price, int time) {
        this.pricePerKm = price;
        this.timePerKm = time;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }

    public int getTimePerKm() {
        return timePerKm;
    }
}
