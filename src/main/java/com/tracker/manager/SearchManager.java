package com.tracker.manager;

import com.tracker.collection.CustomList;
import com.tracker.collection.Task;
import com.tracker.patterns.BinarySearchUtil;
import com.tracker.patterns.TaskComparators;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchManager {
    private final ExecutorService executorService;
    CustomList<Task> result = new CustomList<>();

    public SearchManager() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public CustomList<Task> searchByTitle(CustomList<Task> allTasks, String title) {
        if (allTasks == null || title == null) {
            return new CustomList<>();
        }
        result.clear();
        allTasks.stream()
                .filter(task -> task.getTitle().toLowerCase().contains(title.toLowerCase()))
                .forEach(result::add);
        return result;
    }

    public CustomList<Task> searchByPriority(CustomList<Task> allTasks, Task.Priority priority) {
        if (allTasks == null || priority == null) {
            return new CustomList<>();
        }
        result.clear();
        allTasks.stream()
                .filter(task -> task.getPriority() == priority)
                .forEach(result::add);
        return result;
    }

    public Task binarySearchById(CustomList<Task> allTasks, int id) {
        if (allTasks == null || allTasks.isEmpty()) {
            return null;
        }
        result.clear();
        // Создаем копию и сортируем по ID для бинарного поиска
        CustomList<Task> sortedTasks = new CustomList<>(allTasks);
        sortedTasks.sort(TaskComparators.byId());

        // Создаем задачу для поиска
        Task searchTask = new Task.Builder()
                .id(id)
                .title("Search")
                .description("Search")
                .priority(Task.Priority.LOW)
                .status(Task.Status.NEW)
                .dueDate(LocalDate.now())
                .build();

        int index = BinarySearchUtil.binarySearch(sortedTasks, searchTask, TaskComparators.byId());
        return index >= 0 ? sortedTasks.get(index) : null;
    }

    public CustomList<Task> searchUsingStreams(CustomList<Task> allTasks, String term) {
        if (allTasks == null || term == null) {
            return new CustomList<>();
        }
        result.clear();
        allTasks.stream()
                .filter(task ->
                        task.getTitle().toLowerCase().contains(term.toLowerCase()) ||
                                (task.getDescription() != null && task.getDescription().toLowerCase().contains(term.toLowerCase()))
                )
                .forEach(result::add);
        return result;
    }

    public Map<String, Long> countOccurrencesMultiThreaded(CustomList<Task> allTasks, String term) {
        result.clear();
        if (allTasks == null || term == null) {
            return new ConcurrentHashMap<>();
        }

        Map<String, Long> result = new ConcurrentHashMap<>();

        try {
            // Разделяем задачи на части для многопоточного поиска
            int chunkSize = Math.max(1, allTasks.size() / 4);
            CustomList<Future<Map<String, Long>>> futures = new CustomList<>();

            for (int i = 0; i < allTasks.size(); i += chunkSize) {
                int endIndex = Math.min(i + chunkSize, allTasks.size());
                CustomList<Task> chunk = allTasks.subList(i, endIndex);

                Future<Map<String, Long>> future = executorService.submit(() ->
                        countOccurrencesInChunk(chunk, term)
                );
                futures.add(future);
            }

            // Собираем результаты
            for (Future<Map<String, Long>> future : futures) {
                Map<String, Long> chunkResult = future.get();
                chunkResult.forEach((key, value) ->
                        result.merge(key, value, Long::sum)
                );
            }

        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            System.err.println("Ошибка многопоточного поиска: " + e.getMessage());
            // Fallback на обычный поиск
            return countOccurrencesInChunk(allTasks, term);
        }

        return result;
    }

    private Map<String, Long> countOccurrencesInChunk(CustomList<Task> tasks, String term) {
        Map<String, Long> counts = new ConcurrentHashMap<>();
        String lowerTerm = term.toLowerCase();

        for (Task task : tasks) {
            // Подсчет в названии
            String title = task.getTitle().toLowerCase();
            long titleCount = title.split(lowerTerm, -1).length - 1;
            if (titleCount > 0) {
                counts.merge("title", titleCount, Long::sum);
            }

            // Подсчет в описании
            if (task.getDescription() != null) {
                String description = task.getDescription().toLowerCase();
                long descCount = description.split(lowerTerm, -1).length - 1;
                if (descCount > 0) {
                    counts.merge("description", descCount, Long::sum);
                }
            }
        }

        return counts;
    }

    public void shutdown() {
        executorService.shutdown();
        System.out.println("SearchManager завершен");
    }
}
