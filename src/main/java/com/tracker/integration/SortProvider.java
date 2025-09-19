package com.tracker.integration;

import java.util.List;

/**
 * Интерфейс для сортировки от модуля паттернов (Дмитрий М)
 * Позволяет интегрироваться с паттерном Стратегия для сортировки
 */
public interface SortProvider {
    /**
     * Отсортировать задачи по ID
     * @param tasks список задач
     * @return отсортированный список
     */
    List<Object> sortById(List<Object> tasks);
    
    /**
     * Отсортировать задачи по названию
     * @param tasks список задач
     * @return отсортированный список
     */
    List<Object> sortByTitle(List<Object> tasks);
    
    /**
     * Отсортировать задачи по приоритету
     * @param tasks список задач
     * @return отсортированный список
     */
    List<Object> sortByPriority(List<Object> tasks);
    
    /**
     * Сортировка с условием "чётные сортируем, нечётные оставляем"
     * @param tasks список задач
     * @return отсортированный список
     */
    List<Object> sortEvenOnly(List<Object> tasks);
}
