package com.tracker.integration;

import java.util.List;

/**
 * Интерфейс для работы с файлами от модуля файлов (Дмитрий Г)
 * Позволяет интегрироваться с функционалом чтения/записи файлов
 */
public interface FileDataProvider {
    /**
     * Загрузить задачи из файла
     * @param filePath путь к файлу
     * @return список загруженных задач
     */
    List<Object> loadFromFile(String filePath);
    
    /**
     * Сохранить задачи в файл (append режим)
     * @param filePath путь к файлу
     * @param tasks список задач для сохранения
     * @return true если сохранение успешно
     */
    boolean saveToFile(String filePath, List<Object> tasks);
    
    /**
     * Сгенерировать случайные задачи
     * @param count количество задач
     * @return список сгенерированных задач
     */
    List<Object> generateRandomTasks(int count);
}
