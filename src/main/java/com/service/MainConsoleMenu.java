package com.service;

import com.schedule.SimpleScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MainConsoleMenu {

    @Autowired
    ConsoleMethods methodConsole;

    @Autowired
    SimpleScheduledTask simpleScheduledTask;

    public void outputMenu() {

        while (true) {

            Scanner scanner = new Scanner(System.in);

            System.out.println("------------------------------------------------------------------------------");
            System.out.print("Введите login: ");
            String inputLogin = scanner.nextLine();
            System.out.println("------------------------------------------------------------------------------");
            System.out.print("Введите password: ");
            String inputPassword = scanner.nextLine();

            if (methodConsole.checkAuthorization(inputLogin, inputPassword)) {

                simpleScheduledTask.startOutputDiedLineMessage();

                while (true) {

                    System.out.println("------------------------------------------------------------------------------");
                    System.out.println("Введите номер комманды из списка");
                    System.out.println("1 - Создать задачу");
                    System.out.println("2 - Создать подзадачу по ID");
                    System.out.println("3 - Вывести список всех невыполненных задач пользователя");
                    System.out.println("4 - Изменить статус задачи на 'Выполнено'");
                    System.out.println("5 - Выход (сменить пользователя)");


                    String command = scanner.nextLine();

                    if (command.equals("1")) {
                        methodConsole.getDataForCreateToDo("1");
                    } else if (command.equals("2")) {
                        methodConsole.getDataForCreateToDo("2");
                    } else if (command.equals("3")) {
                        methodConsole.printAllToDoByUser();
                    } else if (command.equals("4")) {
                        methodConsole.changeStatusToDo();
                    } else if (command.equals("5")) {
                        System.out.println("Выход выполнен");
                        simpleScheduledTask.stopOutputDiedLineMessage();
                        break;
                    } else {
                        System.out.println("------------------------------------------------------------------------------");
                        System.out.println("Такой комманды пока нет");
                    }
                }

            } else {
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Не правильно введен login или password, обратитесь к администратору для добавления Вас в список авторизованных пользователей!");
            }
        }
    }
}







