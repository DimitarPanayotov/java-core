package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.FuelType;

import java.time.Duration;
import java.time.LocalDateTime;

public final class Car extends Vehicle {
    private FuelType fuelType;
    private int numberOfSeats;
    private double pricePerWeek;
    private double pricePerDay;
    private double pricePerHour;

    private static double pricePerSeat;

    static {
        pricePerSeat = 5.0;
    }

    public Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.pricePerWeek = pricePerWeek;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    @Override
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        super.returnBack(rentalEnd);
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

        long totalHours = duration.toHours();
        long remainingMinutes = duration.toMinutesPart();


        if (remainingMinutes > 0) {
            totalHours++;
        }

        long totalDays = totalHours / 24;
        long fullWeeks = totalDays / 7;
        long leftoverDays = totalDays % 7;
        long leftoverHours = totalHours % 24;

        double price = fullWeeks * pricePerWeek + leftoverDays * pricePerDay + leftoverHours * pricePerHour + numberOfSeats * pricePerSeat + fuelType.getTax() * totalDays;

        return price;
    }


}
