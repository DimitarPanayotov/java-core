package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

public enum FuelType {
    DIESEL(3.0),
    PETROL(3.0),
    HYBRID(1.0),
    ELECTRICITY(0.0),
    HYDROGEN(0.0);

    private final double tax;

    FuelType(double tax) {
        this.tax = tax;
    }

    public double getTax() { return tax; }
}
