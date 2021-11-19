package com.repository;

import com.entyty.ToDo;
import com.entyty.User;

public interface JdbcTemplateRepository {

    void writeToDoInDataBase(ToDo toDo);

    User findUserByLogin(String text);

    void listAllUnfulfilledToDo(int userId);

    void deadlineMessageOutput();

    ToDo getToDoById(int todoId);

    void changeStatusToDoInDataBase(int todoId);

    boolean checkStatusSubtasks(int todoId);
}
