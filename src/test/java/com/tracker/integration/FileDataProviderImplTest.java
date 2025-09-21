package com.tracker.integration;

import com.tracker.collection.Task;
import com.tracker.exception.FileProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileDataProviderImplTest {

    private FileDataProviderImpl provider = new FileDataProviderImpl();

    @Test
    @DisplayName("should Read 3 Valid Tasks")
    void test1() {
        //Given
        String filePath = "src/test/resources/Tasks1.csv";
        //When
        List<Task> tasks = provider.loadFromFile(filePath);
        //Then
        assertEquals(3, tasks.size());
    }

    @Test
    @DisplayName("Should read lines only with valid IDs")
    void test2() {
        //Given
        String filePath = "src/test/resources/Tasks2.csv";
        //When
        List<Task> tasks = provider.loadFromFile(filePath);
        //Then
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("Should read lines only with valid Titles")
    void test3() {
        //Given
        String filePath = "src/test/resources/Tasks3.csv";
        //When
        List<Task> tasks = provider.loadFromFile(filePath);
        //Then
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("Should read lines only with valid Description")
    void test4() {
        //Given
        String filePath = "src/test/resources/Tasks4.csv";
        //When
        List<Task> tasks = provider.loadFromFile(filePath);
        //Then
        assertEquals(3, tasks.size());
    }

    @Test
    @DisplayName("Should read lines only with valid Priority")
    void test5() {
        //Given
        String filePath = "src/test/resources/Tasks5.csv";
        //When
        List<Task> tasks = provider.loadFromFile(filePath);
        //Then
        assertEquals(2, tasks.size());
    }

    @Test
    @DisplayName("Should read lines only with valid Status")
    void test6() {
        //Given
        String filePath = "src/test/resources/Tasks6.csv";
        //When
        List<Task> tasks = provider.loadFromFile(filePath);
        //Then
        assertEquals(2, tasks.size());
    }

    @Test
    @DisplayName("Should read lines only with valid dueDate")
    void test7() {
        //Given
        String filePath = "src/test/resources/Tasks7.csv";
        //When
        List<Task> tasks = provider.loadFromFile(filePath);
        //Then
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("Should return true when list is empty")
    void test8() {
        //Given
        String filePath = "src/test/resources/Tasks8.csv";
        createEmptyFile(filePath);
        //When
        boolean result = provider.saveToFile(filePath, List.of());
        //Then
        assertTrue(result);
        deleteFile(filePath);
    }

    @Test
    @DisplayName("Should return true when list has one item")
    void test9() {
        //Given
        String filePath = "src/test/resources/Tasks8.csv";
        createEmptyFile(filePath);
        Task task = new Task(1,"Test Title", "Test Descr", Task.Priority.HIGH, Task.Status.NEW, LocalDate.now());
        //When
        boolean result = provider.saveToFile(filePath, List.of(task));
        //Then
        assertTrue(result);
        deleteFile(filePath);
    }

    @Test
    @DisplayName("Should return true when list has multiple items")
    void test10() {
        //Given
        String filePath = "src/test/resources/Tasks8.csv";
        createEmptyFile(filePath);
        Task task1 = new Task(1,"Test Title", "Test Descr", Task.Priority.HIGH, Task.Status.NEW, LocalDate.now());
        Task task2 = new Task(1,"Test Title", "Test Descr", Task.Priority.HIGH, Task.Status.NEW, LocalDate.now());
        Task task3 = new Task(1,"Test Title", "Test Descr", Task.Priority.HIGH, Task.Status.NEW, LocalDate.now());
        //When
        boolean result = provider.saveToFile(filePath, List.of(task1, task2, task3));
        //Then
        assertTrue(result);
        deleteFile(filePath);
    }

    @Test
    @DisplayName("Should return false")
    void test11() {
        //Given
        String filePath = "src/test/resources/";
        Task task = new Task(1,"Test Title", "Test Descr", Task.Priority.HIGH, Task.Status.NEW, LocalDate.now());
        //When
        boolean result = provider.saveToFile(filePath, List.of(task));
        //Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should throw FileProcessingException when file doesn't exist")
    void test12() {
        assertThrows(FileProcessingException.class, new MyExecutable() );
    }

    class MyExecutable implements Executable {
        @Override
        public void execute() throws Throwable {
            //Given
            String filePath = "src/test/resources/Tasks404.csv";
            //When
            provider.loadFromFile(filePath);
        }
    }


    private void deleteFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

    private void createEmptyFile(String filePath) {
        try {
            File file = new File(filePath);
            file.createNewFile();
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
