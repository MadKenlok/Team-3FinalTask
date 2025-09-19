package com.tracker.integration;

import java.util.List;

/**
 * Менеджер интеграции - центральный компонент для работы с модулями команды
 * Позволяет подключать и использовать функционал от других участников
 */
public class IntegrationManager {
    private TaskProvider taskProvider;
    private FileDataProvider fileDataProvider;
    private SortProvider sortProvider;
    private SearchProvider searchProvider;
    
    // Конструктор
    public IntegrationManager() {
        // Инициализация заглушками - будут заменены реальными реализациями
        this.taskProvider = new MockTaskProvider();
        this.fileDataProvider = new MockFileDataProvider();
        this.sortProvider = new MockSortProvider();
        this.searchProvider = new MockSearchProvider();
    }
    
    // Геттеры для доступа к провайдерам
    public TaskProvider getTaskProvider() { return taskProvider; }
    public FileDataProvider getFileDataProvider() { return fileDataProvider; }
    public SortProvider getSortProvider() { return sortProvider; }
    public SearchProvider getSearchProvider() { return searchProvider; }
    
    // Методы для замены провайдеров на реальные реализации
    public void setTaskProvider(TaskProvider taskProvider) {
        this.taskProvider = taskProvider;
    }
    
    public void setFileDataProvider(FileDataProvider fileDataProvider) {
        this.fileDataProvider = fileDataProvider;
    }
    
    public void setSortProvider(SortProvider sortProvider) {
        this.sortProvider = sortProvider;
    }
    
    public void setSearchProvider(SearchProvider searchProvider) {
        this.searchProvider = searchProvider;
    }
    
    // Заглушки для тестирования
    private static class MockTaskProvider implements TaskProvider {
        @Override
        public List<Object> getAllTasks() {
            System.out.println("📝 MockTaskProvider: получение задач (заглушка)");
            return List.of();
        }
        
        @Override
        public int getTaskCount() {
            return 0;
        }
        
        @Override
        public boolean isEmpty() {
            return true;
        }
    }
    
    private static class MockFileDataProvider implements FileDataProvider {
        @Override
        public List<Object> loadFromFile(String filePath) {
            System.out.println("📁 MockFileDataProvider: загрузка из файла " + filePath + " (заглушка)");
            return List.of();
        }
        
        @Override
        public boolean saveToFile(String filePath, List<Object> tasks) {
            System.out.println("💾 MockFileDataProvider: сохранение в файл " + filePath + " (заглушка)");
            return true;
        }
        
        @Override
        public List<Object> generateRandomTasks(int count) {
            System.out.println("🎲 MockFileDataProvider: генерация " + count + " случайных задач (заглушка)");
            return List.of();
        }
    }
    
    private static class MockSortProvider implements SortProvider {
        @Override
        public List<Object> sortById(List<Object> tasks) {
            System.out.println("🔄 MockSortProvider: сортировка по ID (заглушка)");
            return tasks;
        }
        
        @Override
        public List<Object> sortByTitle(List<Object> tasks) {
            System.out.println("🔄 MockSortProvider: сортировка по названию (заглушка)");
            return tasks;
        }
        
        @Override
        public List<Object> sortByPriority(List<Object> tasks) {
            System.out.println("🔄 MockSortProvider: сортировка по приоритету (заглушка)");
            return tasks;
        }
        
        @Override
        public List<Object> sortEvenOnly(List<Object> tasks) {
            System.out.println("🔄 MockSortProvider: сортировка чётных элементов (заглушка)");
            return tasks;
        }
    }
    
    private static class MockSearchProvider implements SearchProvider {
        @Override
        public Object binarySearchById(List<Object> tasks, int id) {
            System.out.println("🔍 MockSearchProvider: бинарный поиск по ID " + id + " (заглушка)");
            return null;
        }
        
        @Override
        public List<Object> searchByTitle(List<Object> tasks, String title) {
            System.out.println("🔍 MockSearchProvider: поиск по названию '" + title + "' (заглушка)");
            return List.of();
        }
        
        @Override
        public List<Object> searchByPriority(List<Object> tasks, int priority) {
            System.out.println("🔍 MockSearchProvider: поиск по приоритету " + priority + " (заглушка)");
            return List.of();
        }
    }
}
