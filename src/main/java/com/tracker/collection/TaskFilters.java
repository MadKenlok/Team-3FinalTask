package com.tracker.collection;

import java.util.Comparator;

public final class TaskFilters {

    private TaskFilters() {
    }

    /**
     * Фильтрация задач по статусу
     */
    public static CustomList<Task> filterByStatus(CustomList<Task> taskList, Task.Status status) {
        CustomList<Task> result = new CustomList<>();
        taskList.stream()
                .filter(t -> t.getStatus() == status)
                .forEach(result::add);
        return result;
    }


    public static CustomList<Task> getUniqueElements(CustomList<Task> taskList) {
        CustomList<Task> result = new CustomList<>();
        taskList.stream()
                .distinct() // Автоматически сохраняет порядок первых вхождений
                .forEach(result::add);
        return result;
    }

    /**
     * Ограничение на N задач
     */
    public static CustomList<Task> limitTasks(CustomList<Task> taskList, int limit) {
        CustomList<Task> result = new CustomList<>();
        taskList.stream()
                .limit(limit)
                .forEach(result::add);
        return result;
    }

    /**
     * Поиск задачи с минимальным приоритетом
     */
    public static void findMinPriority(CustomList<Task> taskList) {
        taskList.stream()
                .min(Comparator.comparingInt(Task::getPriority))
                .ifPresentOrElse(System.out::println,
                        () -> System.out.println("Список пуст — минимальный приоритет не найден")
                );
    }

    /**
     * Поиск задачи с максимальным приоритетом
     */
    public static void findMaxPriority(CustomList<Task> taskList) {
        taskList.stream()
                .max(Comparator.comparingInt(Task::getPriority))
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Список пуст — максимальный приоритет не найден")
                );
    }
}
