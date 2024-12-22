# Тестовое задание для участия в Mobile-практикуме от Silicium (Android)
Приложение-ежедневник с четырьмя экранами:  
1) **Экран выбора хранилища**. Показывается только при первом запуске. Пользователь может выбрать хранение задач в Json-файле во внутреннем хранилище или же базу данных Room. Для целей тестирования добавлен checkbox с возможностью генерации случайных заданий (по 5 задач в 8 случайных днях, близких к текущей дате).
2) **Экран с календарем и списком заданий**. Дни, в которых есть задачи, отмечаются на календаре цветом. Внизу располагается панель меню с кнопками навигации между днями с задачами, кнопка добавления новой задачи, а также кнопка, прячущая календарь.
3) **Экран добавления задачи**. При открытии экрана поле с названием задания автоматически получает фокус, отображается клавиатура. Выбор времени реализован при помощи двух элементов number picker, реагирующих на изменения значений друг друга.
4) **Экран с подробностями задачи**. Отображает название, дату, время, а также краткое описание задания.
# Критерии выполнения задания  
**1 уровень:**  
✅ Структурированный чистый код  
✅ Использование сервисного слоя для подготовки данных  
✅ Адаптивная верстка с использованием Constraint Layout или сопутствующих технологий в XML-разметках  
✅ Использование архитектурных паттернов  
✅ Поддержка версий: Android 8+  
✅ Ориентация: портретная  
✅ Код на GitHub    
  
**2 уровень:**  
✅ Добавить экран создания дела, на котором присутствует возможность указать название, выбрать дату и время, краткое описание дела текстом  
❌ Создание компонентов экрана кодом с помощью кастомных вью на Kotlin или верстка с помощью Jetpack Compose  
✅ Для локального хранения используем Room  
✅ Покрытие Unit-тестами: 1-2 тест  
# Экраны приложения
![Экраны приложения](https://i.postimg.cc/MHcz3QTy/Diary.png)  
# Краткий видеообзор приложения  
[![Название ролика](https://img.youtube.com/vi/hKzyu0ppAPE/0.jpg)](https://youtube.com/shorts/hKzyu0ppAPE)