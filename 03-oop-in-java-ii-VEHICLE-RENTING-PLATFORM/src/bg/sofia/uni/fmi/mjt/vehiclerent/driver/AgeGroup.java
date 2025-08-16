package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public enum AgeGroup {
    JUNIOR(10.0),
    EXPERIENCED(0.0),
    SENIOR(15.0);

    private final double tax;

    AgeGroup(double tax) {
        this.tax = tax;
    }

    public double getTax() { return tax; }
}
