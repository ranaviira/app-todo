package com.schedule;

import com.repository.SimpleJdbcTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SimpleScheduledTask {

    @Autowired
    SimpleJdbcTemplateRepository simpleJdbcTemplateRepository;

    /**
     * Метод делает запрос в базу данных каждые 30 секунд;
     */
    @Scheduled(fixedRate = 30000)
    public void scheduledRequest() {
        simpleJdbcTemplateRepository.deadlineMessageOutput();
    }
}
