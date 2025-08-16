# MJT Olympics: Testing and Mocking

## Описание
Unit тестване на имплементация на система за Олимпийски игри. Задачата включва откриване и поправяне на бъгове чрез TDD подход.

## Основни компоненти

### Competitor
- **Competitor** - интерфейс за състезатели
- **Athlete** - основна имплементация на състезател
- **Medal** - enum с типове медали (GOLD, SILVER, BRONZE)

### Competition
- **Competition** - record за състезание с име, дисциплина и участници
- **CompetitionResultFetcher** - интерфейс за получаване на резултати

### Olympics
- **Olympics** - интерфейс за основната логика
- **MJTOlympics** - главна имплементация с функции за:
    - Обновяване на медални статистики
    - Получаване на класиране по нации
    - Подсчитаване на медали по националност
    - Управление на регистрирани състезатели

## Подход
1. **TDD цикъл**: Написан тест → Намерен бъг → Оправен бъг → Повтаряне
2. **Mocking**: Използвано е Mockito за dependency-та
3. **Покритие**: Тествани edge cases и exception handling

## Пакетна структура
```
src/bg/sofia/uni/fmi/mjt/olympics/
├── comparator/
├── competition/
├── competitor/
├── MJTOlympics.java
└── Olympics.java

test/bg/sofia/uni/fmi/mjt/olympics/
└── (тестове)
```