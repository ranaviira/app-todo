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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private int userId;
    private int todoId = 1;

    /**
     * Метод проверяет, по login и password, имеет ли пользователь доступ;
     *
     * @param login
     * @param password
     * @return true or false;
     */
    @Override
    public boolean findUserInDataBase(String login, String password) {
        String sqlQuery = "SELECT * from users where login=? and password=?;";
        User user = jdbcTemplate.query(sqlQuery, new Object[]{login, password}, new BeanPropertyRowMapper<>(User.class)).stream().findAny().orElse(null);
        if (user != null) {
            userId = user.getUserId();
            return true;
        }
        return false;
    }

    /**
     * Приватный метод возвращает текущую дату и время, в нужном формате;
     *
     * @return LocalDateTime;
     */
    private LocalDateTime getDateTimeNow() {
        return LocalDateTime.of(LocalDateTime.now().getYear()
                , LocalDateTime.now().getMonth()
                , LocalDateTime.now().getDayOfMonth()
                , LocalDateTime.now().getHour()
                , LocalDateTime.now().getMinute());
    }

    /**
     * Метод создает задачу и записывает ее в базу данных;
     *
     * @param text
     * @param diedLineTime
     * @param parentTodoId
     */
    @Override
    public void createToDo(String text, LocalDateTime diedLineTime, int parentTodoId) {
        ToDo toDo = new ToDo(todoId, text, getDateTimeNow(), diedLineTime, "Создано", userId, parentTodoId);
        todoId++;
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
     * Метод возвращает объект ToDo, по todoId;
     *
     * @param todoId
     * @return ToDo
     */
    @Override
    public ToDo getToDoByTodoId(int todoId) {
        String sqlQuery = "SELECT * from todo where todo_id=? AND user_id=?;";
        return jdbcTemplate.query(sqlQuery, new Object[]{todoId, userId}, new BeanPropertyRowMapper<>(ToDo.class)).stream().findAny().orElse(null);
    }

    /**
     * Метод возвращает список всех объектов пользователя, статус задания которых не выполнен;
     *
     * @return List<ToDo>
     */
    @Override
    public List<ToDo> listAllUnfulfilledToDo() {
        String sqlQuery = "SELECT * from todo where status != 'Выполнено' AND user_id=? order by parent_todo_id;";
        return jdbcTemplate.query(sqlQuery, new Object[]{userId}, new BeanPropertyRowMapper<>(ToDo.class));
    }

    /**
     * Метод изменяет статус задачи на 'Выполнено';
     *
     * @param todoId
     */
    @Override
    public void changeStatusToDoInDataBase(int todoId) {
        String sqlQuery = "UPDATE todo SET status='Выполнено' where todo_id=? AND user_id=?;";
        jdbcTemplate.update(sqlQuery, todoId, userId);
    }

    /**
     * Метод проверяет, есть ли у задачи, невыполненные подзадачи;
     *
     * @param todoId
     * @return true or false;
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

    /**
     * Метод возвращает список задач, Дедлайн которых через 1 час;
     *
     * @return List<ToDo>
     */
    @Override
    public List<ToDo> listDeadLineToDo() {
        String sqlQuery = "SELECT * from todo where stop_date=? AND status != 'Выполнено' AND user_id=?;";
        return jdbcTemplate.query(sqlQuery, new Object[]{getDateTimeNow().plusMinutes(60), userId}, new BeanPropertyRowMapper<>(ToDo.class));
    }
}

