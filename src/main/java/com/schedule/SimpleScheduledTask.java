package com.schedule;

import com.entyty.ToDo;
import com.repository.SimpleJdbcTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleScheduledTask {

    @Autowired
    SimpleJdbcTemplateRepository simpleJdbcTemplateRepository;

    private boolean started = false;

    /**
     * Метод запускает планировщик, после авторизации пользователя;
     */
    public void startOutputDiedLineMessage() {
        started = true;
    }

    /**
     * Метод останавливает планировщик, после выхода пользователя;
     */
    public void stopOutputDiedLineMessage() {
        started = false;
    }

    /**
     * Метод выводит на консоль список задач, Дедлайн которых через 1 час;
     */
    @Scheduled(fixedRate = 60000)
    public void outputDiedLineMessage() {
        if (started) {
            List<ToDo> listTodo = simpleJdbcTemplateRepository.listDeadLineToDo();
            if (!(listTodo.isEmpty())) {
                for (ToDo toDo : listTodo) {
                    System.out.println("------------------------------------------------------------------------------");
                    System.out.println("До Дедлайна задачи остался 1 час || ID - "
                            + toDo.getTodoId() + " || Задача:"
                            + toDo.getDescription());
                }
            }
        }
    }
}
