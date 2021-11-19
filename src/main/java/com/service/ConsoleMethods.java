package com.service;

import com.entyty.ToDo;
import com.repository.SimpleJdbcTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleMethods {

    @Autowired
    SimpleJdbcTemplateRepository simpleJdbcTemplateRepository;

    Scanner scanner = new Scanner(System.in);

    public boolean checkAuthorization(String login, String password) {
        return simpleJdbcTemplateRepository.findUserInDataBase(login, password);
    }

    public void printAllToDoByUser() {
        List<ToDo> listToDo = simpleJdbcTemplateRepository.listAllUnfulfilledToDo();
        if (!(listToDo.isEmpty())) {
            for (ToDo toDo : listToDo) {
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Статус задачи: " + toDo.getStatus()
                        + " || ID " + toDo.getTodoId()
                        + " || Задача: " + toDo.getDescription()
                        + " || Дедлайн: " + toDo.getStopDate()
                        + " || Подзадача ID " + toDo.getParentTodoId());
            }
        } else {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Список невыполненных задач пуст");
        }
    }

    public void changeStatusToDo() {

        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Введите ID задачи, у которой хотите изменить статус на 'Выполнено'");
        String todoId = scanner.nextLine();

        int parseTodoId;
        try {
            parseTodoId = Integer.parseInt(todoId);
        } catch (NumberFormatException e) {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Задача с таким ID не найдена");
            return;
        }

        if (simpleJdbcTemplateRepository.getToDoByTodoId(parseTodoId) != null) {
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
            System.out.println("Задачи с таким ID у пользователя не найдена");
        }
    }

    public void getDataForCreateToDo(String numberMenu) {

        int parentTodoId = 0;

        if (numberMenu.equals("1")) {
            parentTodoId = 0;
        } else if (numberMenu.equals("2")) {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Введите ID задачи, для которой хотите сделать подзадачу");
            String todoId = scanner.nextLine();

            int parseTodoId;
            try {
                parseTodoId = Integer.parseInt(todoId);

            } catch (NumberFormatException e) {
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Задача с таким ID не найдена");
                return;
            }
            if (simpleJdbcTemplateRepository.getToDoByTodoId(parseTodoId) != null) {
                parentTodoId = simpleJdbcTemplateRepository.getToDoByTodoId(parseTodoId).getTodoId();
            } else {
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Задача с таким ID не найдена");
                return;
            }
        }

        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Введите описание задачи");
        String text = scanner.nextLine();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Введите Дедлайн по формату '2021-11-31 12:30'");
        System.out.println("За час до Дедлайна будет отправлено сообщение");
        String deadLineDate = scanner.nextLine();

        LocalDateTime diedLineTime;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            diedLineTime = LocalDateTime.parse(deadLineDate, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Формат даты введен не верно, введите пожалуйста дату по шаблону");
            return;
        }
        simpleJdbcTemplateRepository.createToDo(text, diedLineTime, parentTodoId);

        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Задача создана и записана в базу данных");
    }

}

