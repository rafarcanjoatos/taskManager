package com.ipass.taskManager.controller;

import com.ipass.taskManager.service.SubtaskService;
import com.ipass.taskManager.service.TaskService;
import com.ipass.taskManager.service.UserService;

public class SubtaskController extends TaskController {
    private final SubtaskService subtaskService;

    public SubtaskController(TaskService taskService, SubtaskService subtaskService, UserService userService) {
        super(taskService);
        this.subtaskService = subtaskService;
    }
   
}