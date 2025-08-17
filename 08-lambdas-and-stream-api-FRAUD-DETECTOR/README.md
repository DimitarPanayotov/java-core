# Fraud Detector

Java приложение за откриване на измамни финансови транзакции чрез анализ на модели и аномалии в транзакционни данни.

Fraud Detector анализира dataset с финансови транзакции (2,500+ записа) и изчислява рискови оценки за акаунти въз основа на конфигурируеми правила. Идентифицира подозрителни транзакции като отклонения, които се различават значително от нормалните модели.

## Функционалности

- **Анализ на транзакции**: Обработва CSV данни с полета: TransactionID, AccountID, TransactionAmount, TransactionDate, Location, Channel
- **Оценка на риска**: Изчислява рискови оценки (0.0-1.0) за акаунти с помощта на претеглени правила
- **Множество правила за откриване**:
    - **FrequencyRule**: Висока честота на транзакции във времеви прозорци
    - **LocationsRule**: Транзакции от много различни локации
    - **SmallTransactionsRule**: Прекомерни транзакции с малки суми
    - **ZScoreRule**: Статистическо откриване на отклонения с Z-score

## Бърз старт

```java
Reader reader = new FileReader("dataset.csv");
List<Rule> rules = List.of(
    new ZScoreRule(1.5, 0.3),
    new LocationsRule(3, 0.4),
    new FrequencyRule(4, Period.ofWeeks(4), 0.25),
    new SmallTransactionsRule(1, 10.20, 0.05)
);

TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, rules);
System.out.println(analyzer.accountsRisk()); // Получаване на рискови оценки
```

## Ключови компоненти

- **TransactionAnalyzerImpl**: Основен анализиращ клас
- **Transaction**: Record представящ финансова транзакция
- **Channel**: Enum (ATM, ONLINE, BRANCH)
- **Rule**: Интерфейс за правила за откриване на измами

## Изисквания

- Теглата на правилата трябва да са в сума 1.0
- CSV файлът трябва да има заглавен ред (автоматично се пропуска)
- Всички операции с акаунти валидират за null/празни ID-та

## Структура на пакетите

```
src
└── bg.sofia.uni.fmi.mjt.frauddetector
    ├── analyzer
    │     ├── TransactionAnalyzer.java
    │     ├── TransactionAnalyzerImpl.java  
    │     └── (...)
    ├── rule
    │     ├── FrequencyRule.java
    │     ├── LocationsRule.java
    │     ├── Rule.java
    │     ├── SmallTransactionsRule.java
    │     ├── ZScoreRule.java
    │     └── (...)
    ├── transaction
    │     ├── Channel.java
    │     ├── Transaction.java
    │     └── (...)
    └── (...)

test
└── bg.sofia.uni.fmi.mjt.frauddetector
     └── (...)
```