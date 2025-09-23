package com.tracker.integration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

import com.tracker.collection.CustomList;
import com.tracker.collection.Task;
import com.tracker.exception.FileProcessingException;

public class FileDataProviderImpl implements FileDataProvider {
    public static final String PATTERN = "dd-MM-yyyy";

    @Override
    public CustomList<Task> loadFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            CustomList<Task> tasks = new CustomList<>();
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                Task task = parseTaskFromCSV(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            return tasks;
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            throw new FileProcessingException(e);
        }
    }

    private Task parseTaskFromCSV(String csvLine) {
        try {
            String[] values = csvLine.split(",");
            if (values.length != 6) {
                throw new FileProcessingException("В описании задачи должно быть ровно 6 колонок");
            }

            int id = parseId(values[0]);
            String title = parseTitle(values[1]);
            String description = parseDescription(values[2]);
            Task.Priority priority = parsePriority(values[3]);
            Task.Status status = parseStatus(values[4]);
            LocalDate dueDate = parseDueDate(values[5]);

            return new Task.Builder()
                    .id(id)
                    .title(title)
                    .description(description)
                    .priority(priority)
                    .status(status)
                    .dueDate(dueDate)
                    .build();

        } catch (Exception e) {
            System.err.println("Ошибка парсинга строки: " + csvLine + " - " + e.getMessage());
            return null;
        }
    }

    private int parseId(String idValue) {
        try {
            int id = Integer.parseInt(idValue);
            if (id <= 0) {
                throw new FileProcessingException("id должен быть положительным");
            }
            return id;
        } catch (Exception e) {
            throw new FileProcessingException("Ошибка парсинга id: " + idValue, e);
        }
    }

    private String parseTitle(String title) {
        if (title == null) {
            throw new FileProcessingException("Значение колонки title не может быть пустым");
        }
        if (title.length() > 50) {
            throw new FileProcessingException("Длина title не может быть больше 50 символов");
        }
        if (title.isBlank()) {
            throw new FileProcessingException("Значение колонки title не может быть пустым");
        }
        return title.trim();
    }

    private String parseDescription(String description) {
        if (description == null) {
            return null;
        }
        if (description.length() > 500) {
            throw new FileProcessingException("Длина description не может быть больше 500 символов");
        }
        return description.trim();
    }

    private Task.Priority parsePriority(String priority) throws FileProcessingException {
        if (priority == null) {
            throw new FileProcessingException("Приоритет задачи не может быть пустым");
        }
        try {
            return Task.Priority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FileProcessingException("Неверный приоритет: '" + priority +
                    "'. Допустимые значения: " + Task.Priority.getValidValues());
        }
    }

    private Task.Status parseStatus(String status) throws FileProcessingException {
        if (status == null) {
            throw new FileProcessingException("Статус задачи не может быть пустым");
        }
        try {
            return Task.Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FileProcessingException("Неверный Статус: '" + status +
                    "'. Допустимые значения: " + Task.Status.getValidValues());
        }
    }

    private LocalDate parseDueDate(String dueDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
            return LocalDate.parse(dueDate, formatter);
        } catch (DateTimeParseException e) {
            throw new FileProcessingException("Ошибка парсинга dueDate: " + dueDate, e);
        }
    }

    @Override
    public boolean saveToFile(String filePath, CustomList<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            int written = 0;
            for (Task task : tasks) {
                writer.append(task.toString());
                writer.newLine();
                written++;
            }
            writer.flush();
            System.out.println("Успешно добавлено " + written + " задач");
            return true;
        } catch (Exception e) {
            System.err.println("Не получилось сохранить в файл: " + tasks);
            return false;
        }
    }

    @Override
    public CustomList<Task> generateRandomTasks(int count) {
        Random rnd = new Random();
        CustomList<Task> tasks = new CustomList<>();
        IntStream.range(0, count).forEach(id -> {
            tasks.add(new Task.Builder()
                    .id(id + 1)
                    .title("Задача_" + (100 + rnd.nextInt(900)))
                    .description("Описание_" + (id + 1))
                    .priority(Task.Priority.values()[rnd.nextInt(Task.Priority.values().length)])
                    .status(Task.Status.values()[rnd.nextInt(Task.Status.values().length)])
                    .dueDate((LocalDate.now().plusDays(rnd.nextInt(30))))
                    .build());
        });
        return tasks;
    }

    @Override
    public CustomList<Task> generateManualTasks(int count, Scanner sc) {
        CustomList<Task> list = new CustomList<>();

        IntStream.range(0, count).forEach(i -> {
            System.out.println("\n=== Задача #" + (i + 1) + " ===");

            // Ввод с валидацией внутри стрима
            int id = inputIdWithValidation(sc, i + 1);
            String title = inputTitleWithValidation(sc);
            String description = inputDescriptionWithValidation(sc);
            Task.Priority priority = inputPriorityWithValidation(sc);
            Task.Status status = inputStatusWithValidation(sc);
            LocalDate dueDate = inputDateWithValidation(sc);

            // Создание задачи с обработкой ошибок Builder'а
            Task task = createTaskSafely(id, title, description, priority, status, dueDate);
            if (task != null) {
                list.add(task);
                System.out.println("✓ Задача #" + (i + 1) + " успешно добавлена: " + task.getTitle());
            } else {
                System.err.println("✗ Не удалось создать задачу #" + (i + 1));
            }
        });

        return list;
    }

    // Безопасное создание задачи
    private Task createTaskSafely(int id, String title, String description,
                                  Task.Priority priority, Task.Status status, LocalDate dueDate) {
        try {
            return new Task.Builder()
                    .id(id)
                    .title(title)
                    .description(description)
                    .priority(priority)
                    .status(status)
                    .dueDate(dueDate)
                    .build();
        } catch (Exception e) {
            System.err.println("Ошибка при создании задачи: " + e.getMessage());
            return null;
        }
    }

    // Методы валидации с повторным вводом
    private int inputIdWithValidation(Scanner sc, int taskNumber) {
        while (true) {
            try {
                System.out.print("Введите ID задачи #" + taskNumber + ": ");
                String input = sc.nextLine().trim();

                if (input.isEmpty()) {
                    System.err.println("ID не может быть пустым. Попробуйте еще раз.");
                    continue;
                }

                int id = Integer.parseInt(input);
                if (id <= 0) {
                    System.err.println("ID должен быть положительным числом. Попробуйте еще раз.");
                    continue;
                }

                return id;

            } catch (NumberFormatException e) {
                System.err.println("Некорректный формат ID. Введите целое число.");
            }
        }
    }

    private String inputTitleWithValidation(Scanner sc) {
        while (true) {
            try {
                System.out.print("Название: ");
                String title = sc.nextLine();

                if (title == null || title.trim().isEmpty()) {
                    System.err.println("Название не может быть пустым. Попробуйте еще раз.");
                    continue;
                }

                if (title.length() > 50) {
                    System.err.println("Длина названия не может быть больше 50 символов. Попробуйте еще раз.");
                    continue;
                }

                return title.trim();

            } catch (Exception e) {
                System.err.println("Ошибка ввода названия. Попробуйте еще раз.");
            }
        }
    }

    private String inputDescriptionWithValidation(Scanner sc) {
        while (true) {
            try {
                System.out.print("Описание: ");
                String description = sc.nextLine();

                if (description == null || description.trim().isEmpty()) {
                    System.err.println("Описание не может быть пустым. Попробуйте еще раз.");
                    continue;
                }

                if (description.length() > 500) {
                    System.err.println("Длина описания не может быть больше 500 символов. Попробуйте еще раз.");
                    continue;
                }

                return description.trim();

            } catch (Exception e) {
                System.err.println("Ошибка ввода описания. Попробуйте еще раз.");
            }
        }
    }

    private Task.Priority inputPriorityWithValidation(Scanner sc) {
        while (true) {
            try {
                System.out.println("Приоритет (" + Task.Priority.getValidValues() + "): ");
                String priorityInput = sc.nextLine().trim();

                if (priorityInput.isEmpty()) {
                    System.err.println("Приоритет не может быть пустым. Попробуйте еще раз.");
                    continue;
                }

                return Task.Priority.valueOf(priorityInput.toUpperCase());

            } catch (IllegalArgumentException e) {
                System.err.println("Неверный приоритет. Доступные значения: " +
                        Task.Priority.getValidValues() + ". Попробуйте еще раз.");
            } catch (Exception e) {
                System.err.println("Ошибка ввода приоритета. Попробуйте еще раз.");
            }
        }
    }

    private Task.Status inputStatusWithValidation(Scanner sc) {
        while (true) {
            try {
                System.out.println("Статус (" + Task.Status.getValidValues() + "): ");
                String statusInput = sc.nextLine().trim();

                if (statusInput.isEmpty()) {
                    System.err.println("Статус не может быть пустым. Попробуйте еще раз.");
                    continue;
                }

                return Task.Status.valueOf(statusInput.toUpperCase());

            } catch (IllegalArgumentException e) {
                System.err.println("Неверный статус. Доступные значения: " +
                        Task.Status.getValidValues() + ". Попробуйте еще раз.");
            } catch (Exception e) {
                System.err.println("Ошибка ввода статуса. Попробуйте еще раз.");
            }
        }
    }

    private LocalDate inputDateWithValidation(Scanner sc) {
        while (true) {
            try {
                System.out.print("Дата выполнения (" + PATTERN + "): ");
                String dateInput = sc.nextLine().trim();

                if (dateInput.isEmpty()) {
                    System.err.println("Дата не может быть пустой. Попробуйте еще раз.");
                    continue;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
                LocalDate date = LocalDate.parse(dateInput, formatter);

                // Дополнительная проверка
                if (date.isBefore(LocalDate.now().minusYears(1))) {
                    System.err.println("Дата не может быть более года назад. Попробуйте еще раз.");
                    continue;
                }

                return date;

            } catch (DateTimeParseException e) {
                System.err.println("Неверный формат даты. Используйте формат " + PATTERN +
                        " (например: 25-12-2024). Попробуйте еще раз.");
            } catch (Exception e) {
                System.err.println("Ошибка ввода даты. Попробуйте еще раз.");
            }
        }
    }
}