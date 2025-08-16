# Social Network Platform - Collections

## Условие на задачата

Създаване на проста социална мрежа, която моделира потребители, мрежи от приятелства, постове и реакции.

### Основни компоненти:

#### 1. SocialNetworkImpl класа
Основен клас за управление на социалната мрежа:
- `registerUser(UserProfile userProfile)` - регистриране на потребител
- `getAllUsers()` - връща всички регистрирани потребители
- `post(UserProfile userProfile, String content)` - създаване на пост
- `getPosts()` - връща всички постове
- `getReachedUsers(Post post)` - потребители, които могат да видят даден пост
- `getMutualFriends(UserProfile user1, UserProfile user2)` - общи приятели
- `getAllProfilesSortedByFriendsCount()` - потребители подредени по брой приятели

#### 2. DefaultUserProfile класа
Моделира потребителите на социалната мрежа:
- Конструктор: `DefaultUserProfile(String username)`
- Управление на интереси: `addInterest()`, `removeInterest()`, `getInterests()`
- Управление на приятели: `addFriend()`, `unfriend()`, `getFriends()`, `isFriend()`
- ⚠️ Приятелството е симетрична релация

#### 3. Interest enum
Интереси на потребителите:
- `SPORTS`, `BOOKS`, `TRAVEL`, `MUSIC`, `MOVIES`, `GAMES`, `FOOD`

#### 4. SocialFeedPost класа
Моделира постовете в социалната мрежа:
- Конструктор: `SocialFeedPost(UserProfile author, String content)`
- `getUniqueId()`, `getAuthor()`, `getPublishedOn()`, `getContent()`
- Управление на реакции: `addReaction()`, `removeReaction()`, `getAllReactions()`
- Статистики за реакции: `getReactionCount()`, `totalReactionsCount()`

#### 5. ReactionType enum
Типове реакции към постове:
- `LIKE`, `LOVE`, `ANGRY`, `LAUGH`, `SAD`

#### 6. UserRegistrationException
Custom exception за грешки при регистрация на потребители.

### Правила за видимост на постове:
Потребител може да види пост, ако са изпълнени **И ДВЕТЕ** условия:
1. **Има поне един общ интерес** с автора на поста
2. **Има автора в мрежата си от приятели** (директно или чрез верига от приятели)

### Структура на пакетите:
```
bg.sofia.uni.fmi.mjt.socialnetwork/
├── exception/           (UserRegistrationException)
├── post/               (Post, SocialFeedPost, ReactionType)
├── profile/            (UserProfile, DefaultUserProfile, Interest)
├── SocialNetwork.java
└── SocialNetworkImpl.java
```

### Ключови особености:
- Всеки потребител има уникално потребителско име
- Всеки пост има уникален ID и време на публикуване
- Реакции могат да бъдат актуализирани (ако потребител реагира отново)
- Автор може да реагира на собствен пост
- Приятелство работи в двете посоки автоматично
- Не се използват Java Stream API или lambdas
