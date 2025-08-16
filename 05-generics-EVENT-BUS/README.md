# Event Bus 🔔

## Описание
Имплементация на Event Bus архитектурен pattern за управление на комуникацията между компонентите в Java приложение чрез събития и абонати.

## Основни компоненти

### Събития (Events)
- **Event<T>** - интерфейс за събития с timestamp, priority, source и payload
- **Payload<T>** - интерфейс за съдържанието на събитията

### Абонати (Subscribers)
- **Subscriber<T>** - интерфейс за компоненти, които получават събития
- **DeferredEventSubscriber<T>** - специален абонат, който съхранява събития за по-късна обработка (подредени по приоритет и timestamp)

### Event Bus
- **EventBus** - интерфейс за централния компонент
- **EventBusImpl** - основната имплементация с възможности за:
    - Subscribe/Unsubscribe на абонати към типове събития
    - Публикуване на събития
    - Логиране и извличане на събития по време
    - Извличане на абонати за даден тип събитие

## Пакетна структура
```
src/bg/sofia/uni/fmi/mjt/eventbus/
├── events/
│   ├── Event.java
│   └── Payload.java
├── exception/
│   └── MissingSubscriptionException.java  
├── subscribers/
│   ├── Subscriber.java
│   └── DeferredEventSubscriber.java
├── EventBus.java
└── EventBusImpl.java
```

## Изисквания
- Правилно боравене с Java Generics
- Без използване на Stream API и lambdas
