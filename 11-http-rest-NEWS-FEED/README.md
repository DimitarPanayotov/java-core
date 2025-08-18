# News Feed

## Описание
Java клиент библиотека за търсене на актуални новини чрез News API REST услуга с пълна свобода в дизайна и имплементацията.

## Функционалност

### Търсене по критерии
- **Ключови думи** (задължително)
- **Категория** (опционално)
- **Държава** (опционално)

⚠️ Не може търсене само по държава без ключови думи

### API endpoint
- `/v2/top-headlines` на News API
- Безплатен план: **100 заявки/ден**
- Изисква API Key за автентикация

## Ключови изисквания

### Design Freedom
- **Проектиране на собствено API** - удобно и интуитивно
- **Design patterns** - прилагане където е подходящо
- **Performance optimization** 

### Security
- **API Key management** - НЕ hardcode в кода
- Сигурно съхранение и подаване на ключа

### Pagination
- Поддръжка за страници от резултати
- Ефективно управление на множество заявки

###  Error Handling
- **Custom checked exceptions** за различни грешки
- Правилна обработка на HTTP статус кодове
- Graceful handling на network проблеми

## Тестване
- **Mocking** за изолация от външни услуги

## Технически детайли
- **HTTP клиент** за REST заявки
- **JSON parsing** за response-ите
- **Thread safety** 
- **Resource management** - правилно затваряне на connection-и

## Структура (примерна)
```
src/bg/sofia/uni/fmi/mjt/news/
├── client/
├── model/
├── exception/
├── config/
└── util/

test/bg/sofia/uni/fmi/mjt/news/
└── (comprehensive tests)
```