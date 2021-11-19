package com.repository;

import com.entyty.ToDo;
import com.entyty.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SimpleJdbcTemplateRepository implements JdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SimpleJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    /**
     * Метод проверяет в базе данных пользователя с данным логином, если нет возвращает null;
     *
     * @param login
     * @return User
     */
    @Override
    public User findUserByLogin(String login) {
        String sqlQuery = "SELECT * from users where login=?;";
        return jdbcTemplate.query(sqlQuery, new Object[]{login}, new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null);
    }

    /**
     * Метод записывает задание в базу данных;
     *
     * @param toDo
     */
    @Override
    public void writeToDoInDataBase(ToDo toDo) {
        String sqlQuery = "INSERT into todo values(?, ?, ?, ?, ?, ?, ?);";
        jdbcTemplate.update(sqlQuery,
                toDo.getTodoId(),
                toDo.getDescription(),
                toDo.getStartDate(),
                toDo.getStopDate(),
                toDo.getStatus(),
                toDo.getUserId(),
                toDo.getParentTodoId());
    }

    /**
     * Метод выводит все невыполнение задачи пользователя;
     *
     * @param userId
     */
    @Override
    public void listAllUnfulfilledToDo(int userId) {
        String sqlQuery = "SELECT * from todo where status != 'Выполнено' AND user_id=? order by parent_todo_id;";
        List<ToDo> listToDo = jdbcTemplate.query(sqlQuery, new Object[]{userId}, new BeanPropertyRowMapper<>(ToDo.class));
        if (!(listToDo.isEmpty())) {
            for (ToDo toDo : listToDo) {
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Статус: " + toDo.getStatus()
                        + " || ID " + toDo.getTodoId()
                        + " || Задача: " + toDo.getDescription()
                        + " || Дедлайн: " + toDo.getStopDate()
                        + " || Родительский ID " + toDo.getParentTodoId());
            }
        } else {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("Список невыполненных задач пуст");
        }
    }

    /**
     * Метод выводит сообщение за 1 час до Дедлайна задачи;
     */
    @Override
    public void deadlineMessageOutput() {
        String sqlQuery = "SELECT * from todo where stop_date=? AND status != 'Выполнено';";

        LocalDateTime localDateTimeNow = LocalDateTime.of(LocalDateTime.now().getYear()
                , LocalDateTime.now().getMonth()
                , LocalDateTime.now().getDayOfMonth()
                , LocalDateTime.now().getHour()
                , LocalDateTime.now().getMinute());

        List<ToDo> listTodo = jdbcTemplate.query(sqlQuery, new Object[]{localDateTimeNow.plusMinutes(60)}, new BeanPropertyRowMapper<>(ToDo.class));
        if (!(listTodo.isEmpty())) {
            for (ToDo toDo : listTodo) {
                System.out.println("------------------------------------------------------------------------------");
                System.out.println("До конца срока задачи остался 1 час || ID - " + toDo.getTodoId() + " || Задача:" + toDo.getDescription());
            }
        }
    }

    /**
     * Метод возвращает объект ToDO из базы данных;
     *
     * @param todoId
     * @return ToDo or null;
     */
    @Override
    public ToDo getToDoById(int todoId) {
        String sqlQuery = "SELECT * from todo where todo_id=?;";
        return jdbcTemplate.query(sqlQuery, new Object[]{todoId}, new BeanPropertyRowMapper<>(ToDo.class)).stream().findAny().orElse(null);
    }

    /**
     * Метод изменяет статус задачи на Выполнено;
     *
     * @param todoId
     */
    @Override
    public void changeStatusToDoInDataBase(int todoId) {
        String sqlQuery = "UPDATE todo SET status='Выполнено' where todo_id=?;";
        jdbcTemplate.update(sqlQuery, todoId);
    }

    /**
     * Метод проверяет есть ли у задачи, незакрытые подзадачи;
     *
     * @param todoId
     * @return true or false
     */
    @Override
    public boolean checkStatusSubtasks(int todoId) {
        String sqlQuery = "SELECT * from todo where status != 'Выполнено' AND parent_todo_id=?;";
        List<ToDo> listTodo = jdbcTemplate.query(sqlQuery, new Object[]{todoId}, new BeanPropertyRowMapper<>(ToDo.class));
        if (!(listTodo.isEmpty())) {
            return false;
        }
        return true;
    }
}

