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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Goal must be associated with a valid user");
        }
        try {
            return goalRepository.save(goal);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding goal", e);
        }
    }

    @Override
    public List<EmployeeGoal> getGoals(Long id) {
        try {
            return goalRepository.findEmployeeGoalByEmployeeId(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving goals", e);
        }
    }

    @Override
    public EmployeeGoal updateGoalStatus(Long goalId, String status) {
        try {
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
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found with ID: " + goalId);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating goal status", e);
        }
    }

    @Override
    public boolean deleteGoal(Long goalId) {
        try {
            Optional<EmployeeGoal> goalOpt = goalRepository.findEmployeeGoalById(goalId);
            if (goalOpt.isPresent()) {
                goalRepository.delete(goalOpt.get());
                return true;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found with ID: " + goalId);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting goal", e);
        }
    }

    @Override
    public boolean updateGoal(Long goalId, EmployeeGoal employeeGoal) {
        try {
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
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found with ID: " + goalId);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating goal", e);
        }
    }

    @Override
    public EmployeeGoal getGoal(Long id) {
        try {
            Optional<EmployeeGoal> goalOpt = goalRepository.findEmployeeGoalById(id);
            if (goalOpt.isPresent()) {
                return goalOpt.get();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found with ID: " + id);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving goal", e);
        }
    }

    @Override
    public List<String> reminders(User user) {
        try {
            List<EmployeeGoal> goals = goalRepository.findEmployeeGoalByEmployeeIdAndStatusIn(
                    user.getId(),
                    Arrays.asList(GoalStatus.NOT_STARTED, GoalStatus.IN_PROGRESS)
            );

            List<String> reminders = new ArrayList<>();
            LocalDate currentDate = LocalDate.now();

            for (EmployeeGoal goal : goals) {
                LocalDate targetDate = goal.getTargetDate();
                long daysPassed = ChronoUnit.DAYS.between(currentDate, targetDate);
                if (daysPassed > 7) {
                    String reminderMessage = String.format(
                            "Reminder: Your goal '%s' is due. Keep up the good work and continue improving!",
                            goal.getGoalDescription()
                    );

                    List<Notification> notifs = notificationRepository.findByUserAndMessage(user, reminderMessage);
                    if (notifs.size() < 3) {
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
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating reminders", e);
        }
    }

    @Override
    public EmployeeGoal updateEmployeeGoalSupported(boolean supported, long goalId) {
        try {
            EmployeeGoal goal = getGoal(goalId);
            goal.setSupported(supported);
            return goalRepository.save(goal);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating goal support status", e);
        }
    }

    @Override
    public List<EmployeeGoal> getAllGoals() {
        try {
            return goalRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving all goals", e);
        }
    }
}
