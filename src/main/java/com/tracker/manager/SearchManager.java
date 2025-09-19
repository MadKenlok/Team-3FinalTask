package com.tracker.manager;

import com.tracker.collection.Task;
import java.util.List;

public class SearchManager {
    public List<Task> searchByTitle(List<Task> allTasks, String title) {
        throw new RuntimeException("Not yet implemented");
    }

    public List<Task> searchByPriority(List<Task> allTasks, Task.Priority priority) {
        throw new RuntimeException("Not yet implemented");
    }

    public Task binarySearchById(List<Task> allTasks, int id) {
        throw new RuntimeException("Not yet implemented");
    }

    public List<Task> searchUsingStreams(List<Task> allTasks, String term) {
        throw new RuntimeException("Not yet implemented");
    }

    public java.util.Map<String, Long> countOccurrencesMultiThreaded(List<Task> allTasks, String term) {
        throw new RuntimeException("Not yet implemented");
    }

    public void shutdown() {
        throw new RuntimeException("Not yet implemented");
    }
}
