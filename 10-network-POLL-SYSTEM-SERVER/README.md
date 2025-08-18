# Poll System

## Описание
Клиент-сървър система за провеждане на анкети с многонишков сървър, който обслужва множество клиенти едновременно.

## Архитектура
- **Server** - управлява анкети и гласове, обслужва множество клиенти
- **Client** - изпраща команди към сървъра
- **Repository** - съхранява данните за анкетите

## Команди

### create-poll
```
create-poll <question> <option1> <option2> [... <optionN>]
```
- Създава анкета с въпрос и минимум 2 опции
- Въпроси/отговори БЕЗ интервали (използвай тире)

### list-polls
```
list-polls
```
- Показва всички активни анкети с резултати

### submit-vote
```
submit-vote <poll-id> <option>
```
- Гласува за конкретна опция в анкета
- Неограничен брой гласове

### disconnect
```
disconnect
```
- Прекъсва връзката с сървъра

## Основни компоненти

### Server
- **PollServer** - `PollServer(int port, PollRepository repository)`
- **PollRepository** - интерфейс за съхранение на данни
- **InMemoryPollRepository** - in-memory имплементация

### Model
- **Poll** - record с `(String question, Map<String, Integer> options)`

### Отговори
JSON формат с `status` (OK/ERROR) и `message`/`polls`

## Пример
```bash
create-poll What-is-your-favourite-xmas-movie? Home-Alone Die-Hard Elf
submit-vote 1 Home-Alone
list-polls
disconnect
```

## Пакетна структура
```
src/bg/sofia/uni/fmi/mjt/poll/
├── client/
├── server/
│   ├── model/Poll.java
│   ├── repository/
│   └── PollServer.java
└── test/ (optional)
```