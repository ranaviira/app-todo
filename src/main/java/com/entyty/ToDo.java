package com.entyty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDo {
    private int todoId;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime stopDate;
    private String status;
    private int userId;
    private int parentTodoId;
}
