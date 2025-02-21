package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.service.GoalService;
import com.CareerPathway.CareerPathway.service.NotificationService;
import com.CareerPathway.CareerPathway.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    UserService userService;
    @Autowired
    GoalService goalService;
    @GetMapping("/getNotifications")
    public ResponseEntity<?> getNotifications(HttpServletRequest request) {
        long user_id = Long.parseLong(request.getAttribute("userId").toString());
        User employee = userService.userDetails(user_id);
        return ResponseEntity.ok(notificationService.getNotifications(employee));
    }

    @GetMapping("/getReminders")
    public ResponseEntity<?> getReminders(HttpServletRequest request) {
        long user_id = Long.parseLong(request.getAttribute("userId").toString());
        User employee = userService.userDetails(user_id);
        List<String> reminders = goalService.reminders(employee);
        return ResponseEntity.status(HttpStatus.OK).body(reminders);
    }
}
