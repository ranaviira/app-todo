package com.repository;

import com.entyty.ToDo;

import java.time.LocalDateTime;
import java.util.List;

public interface JdbcTemplateRepository {

    void createToDo(String text, LocalDateTime diedLineTime, int parentTodoId);

    boolean findUserInDataBase(String login, String password);

    List<ToDo> listAllUnfulfilledToDo();

    List<ToDo> listDeadLineToDo();

    ToDo getToDoByTodoId(int todoId);

    void changeStatusToDoInDataBase(int todoId);

    boolean checkStatusSubtasks(int todoId);
}
