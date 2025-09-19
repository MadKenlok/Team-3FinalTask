package com.tracker.integration;

import java.util.List;

/**
 * Интерфейс для поиска от модуля паттернов (Дмитрий М)
 * Позволяет интегрироваться с бинарным поиском и другими методами поиска
 */
public interface SearchProvider {
    /**
     * Бинарный поиск по ID
     * @param tasks отсортированный список задач
     * @param id ID для поиска
     * @return найденная задача или null
     */
    Object binarySearchById(List<Object> tasks, int id);
    
    /**
     * Поиск по названию
     * @param tasks список задач
     * @param title название для поиска
     * @return список найденных задач
     */
    List<Object> searchByTitle(List<Object> tasks, String title);
    
    /**
     * Поиск по приоритету
     * @param tasks список задач
     * @param priority приоритет для поиска
     * @return список найденных задач
     */
    List<Object> searchByPriority(List<Object> tasks, int priority);
}
