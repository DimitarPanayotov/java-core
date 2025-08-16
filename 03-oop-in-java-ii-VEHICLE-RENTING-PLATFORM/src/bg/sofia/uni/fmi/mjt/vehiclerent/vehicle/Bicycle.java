package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.Duration;
import java.time.LocalDateTime;

public final class Bicycle extends Vehicle {
    private final double pricePerDay;
    private final double pricePerHour;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    @Override
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        super.returnBack(rentalEnd);
        if(duration.compareTo(Duration.ofDays(7)) > 0) {
            throw new InvalidRentingPeriodException("Bicycles cannot be rented for more than 7 days!");
        }
        freeCar(rentalEnd);
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (startOfRent == null || endOfRent == null) {
            throw new IllegalArgumentException("Start or end of rent is null");
        }
        if(endOfRent.isBefore(startOfRent)) {
            throw new InvalidRentingPeriodException("The end of the rent is before the start of the rent!");
        }

        Duration duration = Duration.between(startOfRent, endOfRent);

        if (duration.compareTo(Duration.ofDays(7)) > 0) {
            throw new InvalidRentingPeriodException("Cannot rent a bicycle for more than 7 days");
        }

        long totalHours = duration.toHours();
        long remainingMinutes = duration.toMinutesPart();

        if (remainingMinutes > 0) {
            totalHours++;
        }
        long fullDays = totalHours / 24;
        long leftoverHours = totalHours % 24;
        double price = fullDays * pricePerDay + leftoverHours * pricePerHour;
        return price;
    }
}