package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import com.CareerPathway.CareerPathway.service.NotificationService;
import com.CareerPathway.CareerPathway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotifications(User employee) {
        List<Notification> AllNotifications = notificationRepository.findByEmployee(employee);
        List<Notification> notifications = new ArrayList<>();
        for (Notification notification : AllNotifications) {
            long days = ChronoUnit.DAYS.between(notification.getSentAt(), LocalDateTime.now());
            if (days <= 3) {
                notifications.add(notification);
            }
        }
        return notifications;
    }
    @Override
    public List<Notification> readNotifications(User employee){
        List<Notification> notifications = notificationRepository.findByEmployeeAndRead(employee, false);
        System.out.println(notifications);
        for (Notification notification : notifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
        return notifications;
    }
}
