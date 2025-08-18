# Goodreads: Book Recommender

Java приложение за препоръчване на книги, базирано на статистически подходи от машинното обучение и dataset от 10,000 най-препоръчвани книги.

## Описание

Системата анализира и препоръчва книги чрез:
- TF-IDF анализ на описанията на книгите
- Overlap coefficient за сравнение на жанрове
- Композитни алгоритми за подобност
- Филтриране и търсене по различни критерии

## Архитектура

### Основни компоненти

- **BookRecommenderAPI** - Главен интерфейс за препоръчване на книги
- **BookFinderAPI** - Търсене и филтриране на книги
- **SimilarityCalculator** - Алгоритми за изчисляване на подобност
- **Book** - Модел на книга с всички свойства

### Пакетна структура
```
bg.sofia.uni.fmi.mjt.goodreads/
├── book/
│   └── Book.java
├── finder/
│   ├── BookFinder.java
│   ├── BookFinderAPI.java
│   └── MatchOption.java
├── recommender/
│   ├── similaritycalculator/
│   │   ├── SimilarityCalculator.java
│   │   ├── composite/
│   │   │   └── CompositeSimilarityCalculator.java
│   │   ├── descriptions/
│   │   │   └── TFIDFSimilarityCalculator.java
│   │   └── genres/
│   │       └── GenresOverlapSimilarityCalculator.java
│   ├── BookRecommender.java
│   └── BookRecommenderAPI.java
├── tokenizer/
│   └── TextTokenizer.java
└── BookLoader.java
```

## Функционалности

### Препоръчване
- **По подобност** - намира най-подобните книги на база алгоритми
- **Топ N резултата** - връща определен брой най-добри препоръки
- **Композитни критерии** - комбинира жанрове и описания с тежести

### Търсене и филтриране
- **По автор** - намира всички книги от конкретен автор
- **По жанрове** - филтрира по един или повече жанрове
- **По ключови думи** - търси в заглавия и описания
- **Опции за съвпадение** - MATCH_ALL или MATCH_ANY

## Алгоритми

### TF-IDF (Term Frequency-Inverse Document Frequency)
Статистическа метрика за оценка на важността на думи в документи:
- **TF** - честота на появяване в конкретен документ
- **IDF** - обратна честота в цялата колекция
- **TF-IDF** - произведение за определяне на важността

### Overlap Coefficient
Алгоритъм за подобност на множества:
```
Overlap = |A ∩ B| / min(|A|, |B|)
```

### Stopwords филтриране
Премахване на 174 често срещани думи без семантична стойност (the, and, of, etc.)

## Технологии

- **Java** - основен език за разработка
- **OpenCSV** - парсване на CSV dataset
- **NLP техники** - текстова обработка и анализ
- **Статистически методи** - машинно обучение

## Dataset

Работи с `goodreads_data.csv` съдържащ:
- N, Book, Author, Description, Genres, Avg_Rating, Num_Ratings, URL
- 10,000 най-препоръчвани книги за всички времена
- Богата информация за жанрове и описания
