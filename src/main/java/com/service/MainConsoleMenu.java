package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MainConsoleMenu {

    @Autowired
    ConsoleMethods methodConsole;

    public void outputMenu() {

        while (true) {

            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Введите номер комманды из списка");
            System.out.println("1 - Создать задачу");
            System.out.println("2 - Создать подзадачу по ID");
            System.out.println("3 - Список всех невыполненных задач пользователя");
            System.out.println("4 - Изменить статус задачи на 'Выполнено'");

            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();

            if (command.equals("1")) {
                methodConsole.createToDo("1");
            } else if (command.equals("2")) {
                methodConsole.createToDo("2");
            } else if (command.equals("3")) {
                methodConsole.printAllToDo();
            } else if (command.equals("4")) {
                methodConsole.changeStatusToDo();
            } else {
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Такой комманды пока нет");
            }
        }
    }
}







