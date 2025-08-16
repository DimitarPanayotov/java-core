# Bolt - Vehicle Renting Platform - OOP and Exceptions

## Условие на задачата

Създаване на платформа за краткосрочен и дългосрочен наем на превозни средства (колела, коли, каравани) с различни ценови модели и такси.

### Основни компоненти:

#### 1. RentalService класа
Управлява процеса на наемане и връщане:
- `rentVehicle(Driver driver, Vehicle vehicle, LocalDateTime startOfRent)` - наемане на превозно средство
- `returnVehicle(Vehicle vehicle, LocalDateTime endOfRent)` - връщане и изчисляване на цена

#### 2. Driver и AgeGroup
- **Driver**: `Driver(AgeGroup group)`
- **AgeGroup** enum с стойности: `JUNIOR`, `EXPERIENCED`, `SENIOR`
- **Такси за възраст**: JUNIOR (10), EXPERIENCED (0), SENIOR (15)
- Таксите НЕ се начисляват за колела

#### 3. Vehicle йерархия
Абстрактен клас `Vehicle` с три наследника:

**Bicycle**:
- Конструктор: `Bicycle(String id, String model, double pricePerDay, double pricePerHour)`
- Може да се наема до 1 седмица
- Минимум 1 час наем

**Car**:
- Конструктор: `Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour)`
- Цена за седалка: 5
- Дневна такса за гориво според типа

**Caravan**:
- Конструктор: `Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds, double pricePerWeek, double pricePerDay, double pricePerHour)`
- Цена за седалка: 5, за легло: 10
- Минимален наем: 1 ден (24ч)
- Дневна такса за гориво според типа

#### 4. FuelType enum
Дневни такси за горива: DIESEL: 3; PETROL: 3; HYBRID: 1; ELECTRICITY: 0; HYDROGEN: 0

#### 5. Custom Exceptions
- `VehicleAlreadyRentedException` - превозното средство вече е наето
- `VehicleNotRentedException` - превозното средство не е наето
- `InvalidRentingPeriodException` - невалиден период за наем

### Структура на пакетите:
```
bg.sofia.uni.fmi.mjt.vehiclerent/
├── driver/              (Driver, AgeGroup)
├── exception/           (custom exceptions)
├── vehicle/            (Vehicle, Car, Bicycle, Caravan, FuelType)
└── RentalService.java
```

### Примери за изчисление:
```java
// Електрическа кола за 5 дни
Vehicle electricCar = new Car("1", "Tesla Model 3", FuelType.ELECTRICITY, 4, 1000, 150, 10);
// Цена: 150*5 + 0*5 + 4*5 = 770.0

// Дизелова кола за 5 дни  
Vehicle dieselCar = new Car("2", "Toyota Auris", FuelType.DIESEL, 4, 500, 80, 5);
// Цена: 80*5 + 3*5 + 4*5 = 435.0
```

## Важни забележки
- Минимален наем: 1 час (по-малко се заплаща като цял час)
- Каравани: минимум 1 ден наем
- Колела: максимум до 1 седмица наем
- Таксите за възраст не се прилагат при колела