### Задача 1: Course Scheduler
Напишете функция `maxNonOverlappingCourses(int[][] courses)` в клас `CourseScheduler`, която намира максималния брой незастъпващи се курсове. Всеки курс е представен с начален и краен час.

**Примери:**
- `{{9, 11}, {10, 12}, {11, 13}, {15, 16}}` → 3
- `{{19, 22}, {17, 19}, {9, 12}, {9, 11}, {15, 17}, {15, 17}}` → 4

### Задача 2: Text Justifier
Напишете функция `justifyText(String[] words, int maxWidth)` в клас `TextJustifier`, която подравнява текст двустранно с равномерно разпределение на интервалите.

**Пример:**
При думи `{"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog."}` и ширина 11:
```
{"The   quick", 
"brown   fox",
"jumps  over",
"the    lazy",
"dog.       "}
```