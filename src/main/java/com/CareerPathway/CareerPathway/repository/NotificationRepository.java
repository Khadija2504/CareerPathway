package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Employee;
import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEmployee(User employee);
    List<Notification> findByEmployeeAndMessage(User employee, String message);
    List<Notification> findByEmployeeAndRead(User employee, boolean read);
}
