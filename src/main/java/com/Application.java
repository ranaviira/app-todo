package com;

import com.service.MainConsoleMenu;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@ComponentScan
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        context.getBean(MainConsoleMenu.class).outputMenu();
    }
}