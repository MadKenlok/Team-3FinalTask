package com.tracker.collection;

import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import com.tracker.collection.Task.Status;


public class TaskGenerator {
    public static CustomList<Task> generateRandom(int count) {
        Random rnd = new Random();
        CustomList<Task> list = new CustomList<>();
        IntStream.range(0, count).forEach(id -> {
            String title = "Task_" + (100 + rnd.nextInt(900));
            int priority = 1 + rnd.nextInt(10);
            Status status = Status.values()[rnd.nextInt(Status.values().length)];
            list.add(new Task(id, title, priority, status));
        });
        return list;
    }

    public static CustomList<Task> generateManual(int count, Scanner sc) {
        CustomList<Task> list = new CustomList<>();
        IntStream.range(0, count).forEach(i -> {
            System.out.println("Введите задачу #" + (i + 1));

            int id = readInt(sc, "ID: ");
            System.out.print("Название: ");
            String title = sc.nextLine();

            int priority = readInt(sc, "Приоритет (1–10): ");
            Task.Status status = readStatus(sc);

            list.add(new Task(id, title, priority, status));
        });

        return list;
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Неверный ввод, попробуйте снова.");
            }
        }
    }

    private static Status readStatus(Scanner sc) {
        while (true) {
            System.out.print("Статус (TODO, IN_PROGRESS, DONE): ");
            try {
                return Status.valueOf(sc.nextLine().trim().toUpperCase());
            } catch (Exception e) {
                System.out.println("Неверный статус.");
            }
        }
    }
}
