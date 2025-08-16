package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.FuelType;

import java.time.Duration;
import java.time.LocalDateTime;


public final class Caravan extends Vehicle {
    private FuelType fuelType;
    private int numberOfSeats;
    private int numberOfBeds;
    private double pricePerWeek;
    private double pricePerDay;
    private double pricePerHour;

    private static double pricePerSeat;
    private static double pricePerBed;

    static {
        pricePerSeat = 5.0;
        pricePerBed = 10.0;
    }

    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds,  double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.numberOfBeds = numberOfBeds;
        this.pricePerWeek = pricePerWeek;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    @Override
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        super.returnBack(rentalEnd);
        if(duration.compareTo(Duration.ofDays(1)) < 0) {
            throw new InvalidRentingPeriodException("Caravans must be rented for at least a day!");
        }
        freeCar(rentalEnd);
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException{
        if (startOfRent == null || endOfRent == null) {
            throw new IllegalArgumentException("Start or end of rent is null");
        }
        if(endOfRent.isBefore(startOfRent)) {
            throw new InvalidRentingPeriodException("The end of the rent is before the start of the rent!");
        }

        Duration duration = Duration.between(startOfRent, endOfRent);
        if(duration.compareTo(Duration.ofDays(1)) < 0) {
            throw new InvalidRentingPeriodException("Caravans must be rented for at least a day!");
        }

        long totalHours = duration.toHours();
        long remainingMinutes = duration.toMinutesPart();


        if (remainingMinutes > 0) {
            totalHours++;
        }

        long totalDays = totalHours / 24;
        long fullWeeks = totalDays / 7;
        long leftoverDays = totalDays % 7;
        long leftoverHours = totalHours % 24;

        double price = fullWeeks * pricePerWeek + leftoverDays * pricePerDay + leftoverHours * pricePerHour + numberOfSeats * pricePerSeat + numberOfBeds * pricePerBed + fuelType.getTax() * totalDays;

        return price;
    }

}
