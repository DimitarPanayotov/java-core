# Sentiment Analyzer

## Описание
Многонишкова система за анализ на настроения в текстови данни, използвайки Producer-Consumer pattern за паралелна обработка на множество потоци.

## Архитектура

### Producer-Consumer Pattern
- **Producer нишки (P)** - четат текстови потоци и създават задачи
- **Consumer нишки (N)** - обработват задачите и изчисляват sentiment score
- **Shared Queue** - опашка за комуникация между producer и consumer

### Sentiment Analysis Algorithm
1. Конвертиране към lowercase
2. Премахване на stop words
3. Премахване на пунктуация
4. Mapping на думи към sentiment scores от лексикона
5. Усредняване на оценките

## Основни компоненти

### Interfaces & Records
- **SentimentAnalyzerAPI** - главен интерфейс с `analyze()` метод
- **AnalyzerInput** - record с inputID и Reader
- **SentimentScore** - enum със стойности от -5 до +5

### Implementation
- **ParallelSentimentAnalyzer** - основна имплементация
    - Constructor: `(int workersCount, Set<String> stopWords, Map<String, SentimentScore> sentimentLexicon)`
    - Връща: `Map<String, SentimentScore>` (inputID → резултат)

## Пример
```java
AnalyzerInput input1 = new AnalyzerInput("doc1", new StringReader("I love java"));
AnalyzerInput input2 = new AnalyzerInput("doc2", new StringReader("I hate bugs"));

ParallelSentimentAnalyzer analyzer = new ParallelSentimentAnalyzer(4, stopWords, lexicon);
Map<String, SentimentScore> results = analyzer.analyze(input1, input2);

// Резултат:
// {"doc1": MODERATELY_POSITIVE, "doc2": SLIGHTLY_NEGATIVE}
```

## Пакетна структура
```
src/bg/sofia/uni/fmi/mjt/sentimentanalyzer/
├── exceptions/
│   └── SentimentAnalysisException.java
├── SentimentAnalyzerAPI.java
├── ParallelSentimentAnalyzer.java
├── SentimentScore.java
├── AnalyzerInput.java
└── (helper classes)
```

## Ключови изисквания
- Producer и Consumer започват едновременно
- Consumer нишките чакат за нови задачи (не busy waiting)
- Правилно приключване на всички нишки
- Thread-safe структури за данни
- Фокус върху concurrency, не върху ефективност на алгоритъма