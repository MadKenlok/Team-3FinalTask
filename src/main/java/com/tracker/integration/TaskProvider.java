package com.tracker.integration;

import java.util.List;

/**
 * Интерфейс для получения задач от модуля коллекций (Владимир)
 * Позволяет интегрироваться с кастомной коллекцией TaskList
 */
public interface TaskProvider {
    /**
     * Получить все задачи
     * @return список всех задач
     */
    List<Object> getAllTasks();
    
    /**
     * Получить количество задач
     * @return количество задач
     */
    int getTaskCount();
    
    /**
     * Проверить, пуста ли коллекция
     * @return true если коллекция пуста
     */
    boolean isEmpty();
}
