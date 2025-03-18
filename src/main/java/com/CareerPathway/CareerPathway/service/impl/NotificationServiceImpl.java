package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import com.CareerPathway.CareerPathway.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee cannot be null");
        }

        try {
            List<Notification> allNotifications = notificationRepository.findByUser(employee);
            List<Notification> notifications = new ArrayList<>();

            for (Notification notification : allNotifications) {
                if (notification.getSentAt() == null) {
                    continue;
                }
                long days = ChronoUnit.DAYS.between(notification.getSentAt(), LocalDateTime.now());
                if (days <= 3) {
                    notifications.add(notification);
                }
            }
            return notifications;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving notifications", e);
        }
    }

    @Override
    public List<Notification> readNotifications(User employee) {
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee cannot be null");
        }

        try {
            List<Notification> notifications = notificationRepository.findByUserAndRead(employee, false);
            for (Notification notification : notifications) {
                notification.setRead(true);
                notificationRepository.save(notification);
            }
            return notifications;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error marking notifications as read", e);
        }
    }

    @Override
    public List<Notification> unreadNotifications(User employee) {
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee cannot be null");
        }

        try {
            return notificationRepository.findByUserAndRead(employee, false);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving unread notifications", e);
        }
    }

    @Override
    public Notification createNotification(String message, User user) {
        if (message == null || user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message and user cannot be null");
        }

        try {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMessage(message);
            notification.setSentAt(LocalDateTime.now());
            notification.setRead(false);
            return notificationRepository.save(notification);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating notification", e);
        }
    }
}
