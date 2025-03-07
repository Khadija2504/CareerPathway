package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.GoalStatus;
import com.CareerPathway.CareerPathway.repository.GoalRepository;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import com.CareerPathway.CareerPathway.repository.UserRepository;
import com.CareerPathway.CareerPathway.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class GoalServiceImpl implements GoalService {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public EmployeeGoal addGoal(EmployeeGoal goal) {
        if (goal.getEmployee() == null || goal.getEmployee().getId() == null) {
            throw new IllegalArgumentException("Goal must be associated with a valid user");
        }
        return goalRepository.save(goal);
    }

    @Override
    public List<EmployeeGoal> getGoals(Long id) {
        return goalRepository.findEmployeeGoalByEmployeeId(id);
    }

    @Override
    public EmployeeGoal updateGoalStatus(Long goalId, String status) {
        Optional<EmployeeGoal> goalOpt = goalRepository.findEmployeeGoalById(goalId);
        if (goalOpt.isPresent()) {
            EmployeeGoal goal = goalOpt.get();
            if (status.equals("COMPLETED")) {
                goal.setStatus(GoalStatus.COMPLETED);
            } else {
                goal.setStatus(GoalStatus.IN_PROGRESS);
            }
            return goalRepository.save(goal);
        } else {
            throw new IllegalArgumentException("Goal is not associated with a valid user");
        }
    }

    @Override
    public boolean deleteGoal(Long goalId) {
        Optional<EmployeeGoal> goalOpt = goalRepository.findEmployeeGoalById(goalId);
        if (goalOpt.isPresent()) {
            EmployeeGoal goal = goalOpt.get();
            goalRepository.delete(goal);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateGoal(Long goalId, EmployeeGoal employeeGoal) {
        Optional<EmployeeGoal> goalOpt = goalRepository.findEmployeeGoalById(goalId);
        if (goalOpt.isPresent()) {
            EmployeeGoal goal = goalOpt.get();
            goal.setEmployee(employeeGoal.getEmployee());
            goal.setGoalDescription(employeeGoal.getGoalDescription());
            goal.setStatus(employeeGoal.getStatus());
            goal.setType(employeeGoal.getType());
            goal.setTargetDate(employeeGoal.getTargetDate());
            goalRepository.save(goal);
            return true;
        }
        return false;
    }

    @Override
    public EmployeeGoal getGoal(Long id) {
        Optional<EmployeeGoal> goalOpt = goalRepository.findEmployeeGoalById(id);
        if (goalOpt.isPresent()) {
            return goalOpt.get();
        } else {
            throw new IllegalArgumentException("Goal not found");
        }
    }

    public List<String> reminders(User user) {
        List<EmployeeGoal> goals = goalRepository.findEmployeeGoalByEmployeeIdAndStatusIn(
                user.getId(),
                Arrays.asList(GoalStatus.NOT_STARTED, GoalStatus.IN_PROGRESS)
        );

        List<String> reminders = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (EmployeeGoal goal : goals) {
            LocalDate targetDate = goal.getTargetDate();
            long daysPassed = ChronoUnit.DAYS.between(currentDate, targetDate);
            System.out.println("hello inside the loop : " + daysPassed + " " + goal.getGoalDescription());
            if (daysPassed > 7) {
                String reminderMessage = String.format(
                        "Reminder: Your goal '%s' is due. Keep up the good work and continue improving!",
                        goal.getGoalDescription()
                );

                List<Notification> notifs = notificationRepository.findByUserAndMessage(user, reminderMessage);
                System.out.println(notifs.size());
                if (notifs.size() < 2) {
                    Notification notification = new Notification();
                    notification.setUser(user);
                    notification.setMessage(reminderMessage);
                    notification.setSentAt(LocalDateTime.now());
                    notification.setRead(false);
                    notificationRepository.save(notification);
                    reminders.add(reminderMessage);
                }
            }
        }
        return reminders;
    }

    @Override
    public EmployeeGoal updateEmployeeGoalSupported(boolean supported, long goalId) {
        EmployeeGoal goal = getGoal(goalId);
        goal.setSupported(supported);
        return goalRepository.save(goal);
    }

    @Override
    public List<EmployeeGoal> getAllGoals(){
        return goalRepository.findAll();
    }
}
