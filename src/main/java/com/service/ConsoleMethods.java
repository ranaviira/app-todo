package com.service;

import com.entyty.ToDo;
import com.repository.SimpleJdbcTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@Component
public class ConsoleMethods {

    @Autowired
    SimpleJdbcTemplateRepository simpleJdbcTemplateRepository;

    private int todoId = 1;
    private int userId;

    Scanner scanner = new Scanner(System.in);

    private boolean checkAuthorization() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Введите login пользователя");
        String enteredLogin = scanner.nextLine();

        if (simpleJdbcTemplateRepository.findUserByLogin(enteredLogin) != null) {
            userId = simpleJdbcTemplateRepository.findUserByLogin(enteredLogin).getUserId();
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Авторизация прошла успешно");
            return true;
        } else {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Пользователь не найден, обратитесь к администратору для добавления Вас в список авторизованных пользователей!");
            return false;
        }
    }

    public void printAllToDo() {
        if (checkAuthorization()) {
            simpleJdbcTemplateRepository.listAllUnfulfilledToDo(userId);
        }
    }

    public void changeStatusToDo() {
        if (checkAuthorization()) {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Введите ID задачи, у которой хотите изменить статус на 'Выполнено'");
            String todoId = scanner.nextLine();

            int parseTodoId;
            try {
                parseTodoId = Integer.parseInt(todoId);
            }catch (NumberFormatException e){
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Задача с таким ID не найдена");
                return;
            }

            if (simpleJdbcTemplateRepository.getToDoById(parseTodoId) != null) {
                if (simpleJdbcTemplateRepository.checkStatusSubtasks(parseTodoId)) {
                    simpleJdbcTemplateRepository.changeStatusToDoInDataBase(parseTodoId);
                    System.out.println("------------------------------------------------------------------------------");
                    System.out.println("Статус задачи изменен на 'Выполнено'");
                } else {
                    System.out.println("------------------------------------------------------------------------------");
                    System.out.println("Статус поменять невозможно, у задачи есть незакрытые подзадачи");
                    return;
                }
            } else {
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Задача с таким ID не найдена");
            }
        }
    }

    private LocalDateTime getDateTimeNow() {
        return LocalDateTime.of(LocalDateTime.now().getYear()
                , LocalDateTime.now().getMonth()
                , LocalDateTime.now().getDayOfMonth()
                , LocalDateTime.now().getHour()
                , LocalDateTime.now().getMinute());
    }

    public void createToDo(String numberFromMenu) {
        if (checkAuthorization()) {
            int parentTodoId = 0;

            if (numberFromMenu.equals("1")) {
                parentTodoId = 0;
            } else if (numberFromMenu.equals("2")) {
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Введите ID задачи, для которой хотите сделать подзадачу");
                String todoId = scanner.nextLine();

                int parseTodoId;
                try {
                    parseTodoId = Integer.parseInt(todoId);
                }catch (NumberFormatException e){
                    System.out.println("------------------------------------------------------------------------------");
                    System.out.println("Задача с таким ID не найдена");
                    return;
                }
                if (simpleJdbcTemplateRepository.getToDoById(parseTodoId) != null) {
                    parentTodoId = simpleJdbcTemplateRepository.getToDoById(parseTodoId).getTodoId();
                } else {
                    System.out.println("------------------------------------------------------------------------------");
                    System.out.println("Задача с таким ID не найдена");
                    return;
                }
            }
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Введите задачу");
            String text = scanner.nextLine();
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Введите дедлайн по формату '2021-11-31 12:30'");
            System.out.println("За час до дедлайна будет отправлено сообщение");
            String deadLineDate = scanner.nextLine();

            LocalDateTime dateTime;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                dateTime = LocalDateTime.parse(deadLineDate, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Формат даты введен не верно, введите пожалуйста дату по шаблону");
                return;
            }
            ToDo toDo = new ToDo(todoId, text, getDateTimeNow(), dateTime, "Создано", userId, parentTodoId);
            simpleJdbcTemplateRepository.writeToDoInDataBase(toDo);
            todoId++;
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Задача записана в базу данных");
        }
    }
}

