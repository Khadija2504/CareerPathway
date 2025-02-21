package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotifications(User employee);
    List<Notification> readNotifications(User employee);
}
