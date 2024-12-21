package com.example.diary.data

import java.time.Instant
import java.time.ZoneId
import java.util.LinkedList
import kotlin.random.Random

object RandomTasksGenerator {

    private const val MINUTE_IN_SECONDS = 60
    private const val HOUR_IN_SECONDS = MINUTE_IN_SECONDS * 60
    private const val DAY_IN_SECONDS = HOUR_IN_SECONDS * 24

    fun generateTasksJsonDTO(): List<TaskJsonDTO> {
        val tasksJsonDTOlist = mutableListOf<TaskJsonDTO>()
        val randomDays = getRandomDays()
        var id = 0
        for (i in 0..7) {
            val today =
                Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault())
            val startOfTheDay =
                today.toEpochSecond() - today.hour * HOUR_IN_SECONDS - (today.minute - 1) * MINUTE_IN_SECONDS
            val randomDayInSeconds = randomDays.poll() * DAY_IN_SECONDS
            val randomTasks = getRandomTasks()
            for (j in 0..4) {
                val randomStartHour = (Random.nextInt(8) + 8) * HOUR_IN_SECONDS
                val start = startOfTheDay + randomDayInSeconds + randomStartHour
                val finish = start + (Random.nextInt(4) + 3) * HOUR_IN_SECONDS
                val taskNameAndDescription = randomTasks.poll()
                val taskJsonDTO = TaskJsonDTO(id, start, finish, taskNameAndDescription.first, taskNameAndDescription.second)
                tasksJsonDTOlist.add(taskJsonDTO)
                id++
            }
        }
        return tasksJsonDTOlist
    }

    fun generateTasksRoomDTO(): List<TaskRoomDTO> {
        val randomDays = getRandomDays()
        val tasksRoomDTOlist = mutableListOf<TaskRoomDTO>()
        var id = 0
        for (i in 0..7) {
            val today =
                Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault())
            val startOfTheDay =
                today.toEpochSecond() - today.hour * HOUR_IN_SECONDS - (today.minute - 1) * MINUTE_IN_SECONDS
            val randomDayInSeconds = randomDays.poll() * DAY_IN_SECONDS
            val randomTasks = getRandomTasks()
            for (j in 0..4) {
                val randomStartHour = (Random.nextInt(8) + 8) * HOUR_IN_SECONDS
                val start = startOfTheDay + randomDayInSeconds + randomStartHour
                val finish = start + (Random.nextInt(4) + 3) * HOUR_IN_SECONDS
                val taskNameAndDescription = randomTasks.poll()
                val taskRoomDTO = TaskRoomDTO(id, start, finish, taskNameAndDescription.first, taskNameAndDescription.second)
                tasksRoomDTOlist.add(taskRoomDTO)
                id++
            }
        }
        return tasksRoomDTOlist
    }

    private fun getRandomDays(): LinkedList<Int> {
        val randomDaysList: MutableList<Int> = mutableListOf()
        val randomDaysLinkedList = LinkedList<Int>()
        for (i in -20..20){
            randomDaysList.add(i)
        }
        randomDaysList.shuffled().forEach {
            randomDaysLinkedList.add(it)
        }
        return randomDaysLinkedList
    }

    private fun getRandomTasks(): LinkedList<Pair<String, String>> {
        val randomTasksList = mutableListOf<Pair<String, String>>()
        val randomTasksLinkedList = LinkedList<Pair<String, String>>()
        randomTasksList.add(Pair("Создать экран авторизации", "Реализуйте экран авторизации, где пользователь может ввести логин и пароль. Проверьте правильность ввода (например, длину пароля) и отобразите соответствующие ошибки. После успешного ввода перенаправьте пользователя на главный экран."))
        randomTasksList.add(Pair("Реализовать экран списка товаров", "Создайте экран, отображающий список товаров в виде RecyclerView. Для каждого элемента отобразите название, цену и миниатюру изображения. Добавьте обработчик клика на элемент для перехода на экран с подробной информацией."))
        randomTasksList.add(Pair("Создать кастомный виджет", "Разработайте виджет в виде кастомной кнопки с градиентным фоном и закругленными углами. Кнопка должна менять цвет при нажатии и быть переиспользуемой."))
        randomTasksList.add(Pair("Реализовать экран с картой", "Добавьте в приложение карту с помощью Google Maps API. Отметьте несколько точек (маркеров) на карте и отобразите информацию о каждом месте при клике на маркер."))
        randomTasksList.add(Pair("Добавить темную тему", "Внедрите поддержку темной темы в приложение. Обеспечьте правильное отображение всех элементов интерфейса при переключении между светлой и темной темами."))
        randomTasksList.add(Pair("Сделать приложение мультиязычным", "Добавьте поддержку нескольких языков (например, английского, русского и испанского). Переведите все строки приложения и настройте автоматический выбор языка на основе настроек устройства."))
        randomTasksList.add(Pair("Реализовать уведомления", "Настройте отправку уведомлений пользователю. Например, отправьте напоминание через 10 секунд после открытия приложения."))
        randomTasksList.add(Pair("Реализовать работу с камерой", "Добавьте функциональность для захвата фото через камеру устройства. Отобразите сделанное фото на экране с возможностью его сохранения в галерею."))
        randomTasksList.add(Pair("Сделать экран с анимацией загрузки", "Реализуйте экран с анимацией, который отображается во время выполнения длительных операций. Используйте Lottie или стандартные средства анимации Android."))
        randomTasksList.add(Pair("Добавить функционал поиска", "Реализуйте поиск по списку элементов с помощью SearchView. Обеспечьте фильтрацию данных в режиме реального времени."))
        randomTasksList.add(Pair("Интеграция с REST API", "Подключитесь к публичному REST API (например, OpenWeather или GitHub API). Выведите данные на экран с использованием Retrofit или другого HTTP-клиента."))
        randomTasksList.add(Pair("Создать экран с ViewPager", "Разработайте экран с ViewPager для отображения нескольких вкладок. В каждой вкладке отобразите уникальный контент (например, список статей, фото или видео)."))
        randomTasksList.add(Pair("Добавить свайп для удаления", "Настройте RecyclerView для поддержки удаления элемента при свайпе. Добавьте всплывающее сообщение с кнопкой \"Отмена\" для восстановления удаленного элемента."))
        randomTasksList.add(Pair("Создать базу данных SQLite", "Реализуйте локальное хранение данных с использованием SQLite. Например, сохраните список заметок и обеспечьте возможность их добавления, редактирования и удаления."))
        randomTasksList.add(Pair("Внедрить Room для хранения данных", "Используйте библиотеку Room для упрощения работы с базой данных. Реализуйте функционал для сохранения и отображения информации о пользователях."))
        randomTasksList.add(Pair("Реализовать Drag & Drop", "Настройте возможность перетаскивания элементов внутри RecyclerView. Сохраните новый порядок элементов после завершения операции."))
        randomTasksList.add(Pair("Добавить загрузку данных по частям", "Реализуйте функционал пагинации для подгрузки данных по мере прокрутки списка. Например, используйте Paging Library или ручную реализацию."))
        randomTasksList.add(Pair("Создать экран с графиками", "Добавьте в приложение экран для отображения графиков (например, изменения цен или статистики). Используйте библиотеку MPAndroidChart или аналог."))
        randomTasksList.add(Pair("Реализовать Splash Screen", "Создайте экран загрузки, который отображается при запуске приложения. Добавьте анимацию логотипа или другой элемент для улучшения пользовательского опыта."))
        randomTasksList.add(Pair("Добавить авторизацию через Google", "Внедрите авторизацию через Google с помощью Google Sign-In API. После успешной авторизации выведите имя пользователя и его фотографию на экране профиля."))
        randomTasksList.add(Pair("Добавить функцию загрузки файлов", "Реализуйте функционал загрузки файлов в приложение. Позвольте пользователю выбирать файл из устройства и сохранять его в локальное хранилище. Добавьте индикатор прогресса для процесса загрузки."))
        randomTasksList.add(Pair("Создать экран профиля", "Реализуйте экран с информацией о пользователе, включая аватар, имя, электронную почту и дату регистрации. Позвольте редактировать данные и загружать новый аватар с помощью камеры или галереи."))
        randomTasksList.add(Pair("Реализовать функцию голосового ввода", "Добавьте возможность ввода текста голосом. Используйте Android Speech API для преобразования речи в текст и отображайте результат в TextView или поле ввода."))
        randomTasksList.add(Pair("Реализовать поддержку динамических ссылок", "Добавьте возможность ввода текста голосом. Используйте Android Speech API для преобразования речи в текст и отображайте результат в TextView или поле ввода."))
        randomTasksList.add(Pair("Реализовать поддержку динамических ссылок", "Настройте приложение для открытия через динамические ссылки (Deep Links). Например, сделайте так, чтобы ссылка на конкретный товар открывала соответствующий экран с деталями в приложении."))
        randomTasksList.add(Pair("Добавить поддержку биометрической аутентификации", "Реализуйте авторизацию через биометрию (сканер отпечатка пальца или распознавание лица). Используйте BiometricPrompt API и обеспечьте альтернативный способ входа (например, PIN-код)."))
        randomTasksList.add(Pair("Создать модульный проект", "Разделите приложение на модули (например, модуль для авторизации, модуль для главного экрана и т. д.). Настройте зависимость между модулями и оптимизируйте сборку."))
        randomTasksList.add(Pair("Реализовать функцию оффлайн-режима", "Позвольте пользователю работать с приложением без интернета. Сохраняйте данные локально с использованием Room или SharedPreferences и синхронизируйте их с сервером при подключении к сети."))
        randomTasksList.add(Pair("Добавить поддержку WorkManager", "Настройте фоновую задачу для выполнения определенных операций (например, синхронизации данных или загрузки новых файлов). Используйте WorkManager для обеспечения надежности выполнения задач."))
        randomTasksList.add(Pair("Реализовать кастомную анимацию перехода между экранами", "Настройте уникальные анимации перехода между экранами (например, слайд, вращение или масштабирование). Используйте Transition API или View Animation для создания эффектов."))
        randomTasksList.add(Pair("Создать виджет для главного экрана", "Реализуйте виджет, который пользователь может добавить на главный экран устройства. Например, создайте виджет с часами, погодой или быстрым доступом к функциям приложения."))
        randomTasksList.shuffled().forEach {
            randomTasksLinkedList.add(it)
        }
        return randomTasksLinkedList
    }

}