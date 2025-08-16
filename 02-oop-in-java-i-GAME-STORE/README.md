# GameStore - OOP Fundamentals

## Условие на задачата

Създаване на онлайн магазин за игри (като Steam/Epic Games) с възможност за търсене, филтриране, промоционални кодове и оценяване.

### Основни компоненти:

#### 1. StoreItem Interface
Всички артикули (игри, DLC, пакети) имплементират `StoreItem` с методи за:
- `getTitle()`, `getPrice()`, `getRating()`, `getReleaseDate()`
- `setTitle()`, `setPrice()`, `setReleaseDate()`
- `rate(double rating)`

#### 2. Артикули в пакет `category`:
- **Game**: `Game(String title, BigDecimal price, LocalDateTime releaseDate, String genre)`
- **DLC**: `DLC(String title, BigDecimal price, LocalDateTime releaseDate, Game game)`
- **GameBundle**: `GameBundle(String title, BigDecimal price, LocalDateTime releaseDate, Game[] games)`

#### 3. Филтри в пакет `filter`:
- **PriceItemFilter**: филтрира по ценови диапазон
- **RatingItemFilter**: филтрира по минимален рейтинг
- **ReleaseDateItemFilter**: филтрира по дата на издаване
- **TitleItemFilter**: филтрира по заглавие (case sensitive/insensitive)

#### 4. GameStore клас
Имплементира `StoreAPI` с методи:
- `findItemByFilters(ItemFilter[] itemFilters)` - търсене по филтри
- `applyDiscount(String promoCode)` - прилагане на промокодове
- `rateItem(StoreItem item, int rating)` - оценяване на артикули


### Структура на пакетите:
```
bg.sofia.uni.fmi.mjt.gameplatform.store
├── item/
│   ├── category/          (Game, DLC, GameBundle)
│   ├── filter/           (всички филтри)
│   └── StoreItem.java
├── GameStore.java
└── StoreAPI.java
```

## Ограничения
- Използване само на масиви (не колекции)
- Цените с точно 2 знака след десетичната запетая
- Рейтинг в диапазон [1-5]