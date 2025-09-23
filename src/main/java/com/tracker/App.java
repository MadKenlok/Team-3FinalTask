package com.tracker;

import java.util.Scanner;

import com.tracker.collection.CustomList;
import com.tracker.collection.Task;
import com.tracker.manager.DataSourceManager;
import com.tracker.manager.SearchManager;
import com.tracker.manager.SortManager;
import com.tracker.manager.TaskManager;



public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager taskManager = new TaskManager();
    private static final DataSourceManager dataSourceManager = new DataSourceManager();
    private static final SortManager sortManager = new SortManager();
    private static final SearchManager searchManager = new SearchManager();

    public static void main(String[] args) {
        System.out.println("🚀 Task Tracker - Система управления задачами");
        System.out.println("===============================================");
        
        boolean running = true;

        while (running) {
            showMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> loadDataFromSource();
                case "2" -> showAllTasks();
                case "3" -> performSorting();
                case "4" -> performSearch();
                case "5" -> addTaskManually();
                case "6" -> performAdvancedSearch();
                case "7" -> saveTasksToFile();
                case "0" -> {
                    running = false;
                    System.out.println("👋 Завершение работы...");
                    shutdown();
                }
                default -> System.out.println("❌ Некорректный ввод! Попробуйте снова.");
            }
        }

        scanner.close();
    }
    
    private static void showMainMenu() {
        System.out.println("\n📋 ОСНОВНОЕ МЕНЮ:");
        System.out.println("1. 📁 Загрузить данные из источника");
        System.out.println("2. 📝 Показать все задачи");
        System.out.println("3. 🔄 Сортировка задач");
        System.out.println("4. 🔍 Поиск задач");
        System.out.println("5. ➕ Добавить задачу вручную");
        System.out.println("6. 🔬 Расширенный поиск и подсчет");
        System.out.println("7. 💾 Сохранить текущие задачи в файл (append)");
        System.out.println("0. ❌ Выход");
        System.out.print("\nВыберите пункт: ");
    }
    
    private static void loadDataFromSource() {
        System.out.println("\n📁 ИСТОЧНИКИ ДАННЫХ:");
        System.out.println("1. 📄 Загрузить из файла");
        System.out.println("2. 🎲 Сгенерировать случайные задачи");
        System.out.println("3. ✏️  Ручной ввод");
        System.out.print("Выберите источник: ");
        
        String source = scanner.nextLine();

        CustomList<Task> tasks = switch (source) {
            case "1" -> {
                System.out.print("Введите путь к файлу: ");
                String filePath = scanner.nextLine();
                yield dataSourceManager.loadFromFile(filePath);
            }
            case "2" -> {
                System.out.print("Сколько задач сгенерировать? ");
                int count = Integer.parseInt(scanner.nextLine());
                yield dataSourceManager.generateRandomTasks(count);
            }
            case "3" -> dataSourceManager.loadFromManualInput();
            default -> {
                System.out.println("❌ Некорректный выбор источника!");
                yield null;
            }
        };
        
        if (tasks != null && !tasks.isEmpty()) {
            taskManager.addTasks(tasks);
            System.out.println("✅ Загружено " + tasks.size() + " задач");
        } else {
            System.out.println("❌ Не удалось загрузить задачи");
        }
    }
    
    private static void showAllTasks() {
        CustomList<Task> tasks = taskManager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("📝 Список задач пуст");
            return;
        }
        
        System.out.println("\n📝 ВСЕ ЗАДАЧИ (" + tasks.size() + "):");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }
    
    private static void performSorting() {
        System.out.println("\n🔄 ТИПЫ СОРТИРОВКИ:");
        System.out.println("1. По приоритету");
        System.out.println("2. По дате выполнения");
        System.out.println("3. По названию");
        System.out.println("4. Быстрая сортировка");
        System.out.println("5. Сортировка пузырьком");
        System.out.println("6. Сортировка слиянием");
        System.out.println("7. Сортировка по четности id (четные сортируются)");
        System.out.print("Выберите тип сортировки: ");
        
        String sortType = scanner.nextLine();
        CustomList<Task> sortedTasks = sortManager.sortTasks(taskManager.getAllTasks(), sortType);
        
        if (sortedTasks != null) {
            taskManager.updateTasks(sortedTasks);
            System.out.println("✅ Сортировка выполнена!");
            showAllTasks();
        } else {
            System.out.println("❌ Ошибка при сортировке");
        }
    }
    
    private static void performSearch() {
        System.out.println("\n🔍 ПОИСК ЗАДАЧ:");
        System.out.println("1. Поиск по названию");
        System.out.println("2. Поиск по приоритету");
        System.out.println("3. Бинарный поиск по ID");
        System.out.println("4. Поиск с использованием стримов");
        System.out.print("Выберите тип поиска: ");
        
        String searchType = scanner.nextLine();
        
        switch (searchType) {
            case "1" -> {
                System.out.print("Введите название для поиска: ");
                String title = scanner.nextLine();
                CustomList<Task> results = searchManager.searchByTitle(taskManager.getAllTasks(), title);
                displaySearchResults(results);
            }
            case "2" -> {
                System.out.println("Выберите приоритет: 1-LOW, 2-MEDIUM, 3-HIGH, 4-URGENT");
                String priorityInput = scanner.nextLine();
                Task.Priority priority = Task.Priority.valueOf(
                    switch(priorityInput) {
                        case "1" -> "LOW";
                        case "2" -> "MEDIUM";
                        case "3" -> "HIGH";
                        case "4" -> "URGENT";
                        default -> "LOW";
                    }
                );
                CustomList<Task> results = searchManager.searchByPriority(taskManager.getAllTasks(), priority);
                displaySearchResults(results);
            }
            case "3" -> {
                System.out.print("Введите ID для поиска: ");
                String raw = scanner.nextLine();
                try {
                    int id = Integer.parseInt(raw.trim());
                    Task result = searchManager.binarySearchById(taskManager.getAllTasks(), id);
                    if (result != null) {
                        System.out.println("✅ Найдена задача: " + result);
                    } else {
                        System.out.println("❌ Задача с ID " + id + " не найдена");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Некорректный ID. Введите целое число.");
                }
            }
            case "4" -> {
                System.out.print("Введите термин для поиска: ");
                String term = scanner.nextLine();
                CustomList<Task> results = searchManager.searchUsingStreams(taskManager.getAllTasks(), term);
                displaySearchResults(results);
            }
            default -> System.out.println("❌ Некорректный тип поиска!");
        }
    }
    
    private static void performAdvancedSearch() {
        System.out.println("\n🔬 РАСШИРЕННЫЙ ПОИСК И ПОДСЧЕТ:");
        System.out.println("1. Многопоточный подсчет вхождений");
        System.out.println("2. Поиск с использованием стримов");
        System.out.print("Выберите тип: ");
        
        String searchType = scanner.nextLine();
        
        switch (searchType) {
            case "1" -> {
                System.out.print("Введите термин для подсчета вхождений: ");
                String term = scanner.nextLine();
                var counts = searchManager.countOccurrencesMultiThreaded(taskManager.getAllTasks(), term);
                displayCountResults(counts, term);
            }
            case "2" -> {
                System.out.print("Введите термин для поиска: ");
                String term = scanner.nextLine();
                CustomList<Task> results = searchManager.searchUsingStreams(taskManager.getAllTasks(), term);
                displaySearchResults(results);
            }
            default -> System.out.println("❌ Некорректный тип поиска!");
        }
    }
    
    private static void displaySearchResults(CustomList<Task> results) {
        if (results.isEmpty()) {
            System.out.println("❌ Совпадений не найдено");
        } else {
            System.out.println("✅ Найдено " + results.size() + " задач:");
            for (Task task : results) {
                System.out.println("- " + task);
            }
        }
    }
    
    private static void displayCountResults(java.util.Map<String, Long> counts, String term) {
        if (counts.isEmpty()) {
            System.out.println("❌ Вхождения не найдены");
        } else {
            System.out.println("📊 Результаты подсчета вхождений '" + term + "':");
            counts.forEach((key, value) -> 
                System.out.println("  " + key + ": " + value + " раз"));
        }
    }
    
    private static void addTaskManually() {
        CustomList<Task> newTasks = dataSourceManager.loadFromManualInput();
        if (newTasks != null && !newTasks.isEmpty()) {
            taskManager.addTasks(newTasks);
            System.out.println("✅ Добавлено задач: " + newTasks.size());
            showAllTasks();
        } else {
            System.out.println("❌ Задачи не были добавлены");
        }
    }

    private static void saveTasksToFile() {
        System.out.print("Введите путь к файлу для сохранения: ");
        String filePath = scanner.nextLine();
        CustomList<Task> tasks = taskManager.getAllTasks();
        boolean ok = dataSourceManager.saveTasksAppend(filePath, tasks);
        System.out.println(ok ? "✅ Сохранено успешно (append)" : "❌ Не удалось сохранить");
    }
    
    private static void shutdown() {
        System.out.println("🔄 Завершение работы менеджеров...");
        sortManager.shutdown();
        searchManager.shutdown();
        System.out.println("✅ Все менеджеры завершены");
    }
}
