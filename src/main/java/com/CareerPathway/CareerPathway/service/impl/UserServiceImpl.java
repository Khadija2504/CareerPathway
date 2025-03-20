package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.dto.AggregatedResultDTO;
import com.CareerPathway.CareerPathway.dto.RegistrationDTO;
import com.CareerPathway.CareerPathway.model.*;
import com.CareerPathway.CareerPathway.model.enums.GoalStatus;
import com.CareerPathway.CareerPathway.model.enums.Role;
import com.CareerPathway.CareerPathway.repository.*;
import com.CareerPathway.CareerPathway.service.UserService;
import com.CareerPathway.CareerPathway.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillAssessmentRepository skillAssessmentRepository;
    @Autowired
    private CareerPathRepository careerPathRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private GoalRepository employeeGoalRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerUser(RegistrationDTO registrationDTO) {
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email already registered");
        }

        User user;
        if (registrationDTO.getRole() == Role.EMPLOYEE) {
            user = Employee.builder()
                    .firstName(registrationDTO.getFirstName())
                    .lastName(registrationDTO.getLastName())
                    .email(registrationDTO.getEmail())
                    .password(PasswordUtil.hashPassword(registrationDTO.getPassword()))
                    .role(registrationDTO.getRole())
                    .imgUrl(registrationDTO.getImgUrl())
                    .department(registrationDTO.getDepartment())
                    .jobTitle(registrationDTO.getJobTitle())
                    .build();
        } else if (registrationDTO.getRole() == Role.MENTOR) {
            user = Mentor.builder()
                    .firstName(registrationDTO.getFirstName())
                    .lastName(registrationDTO.getLastName())
                    .email(registrationDTO.getEmail())
                    .password(PasswordUtil.hashPassword(registrationDTO.getPassword()))
                    .role(registrationDTO.getRole())
                    .imgUrl(registrationDTO.getImgUrl())
                    .expertiseArea(registrationDTO.getExpertiseArea())
                    .yearsOfExperience(registrationDTO.getYearsOfExperience())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid role");
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUserDetails(long id, RegistrationDTO registrationDTO) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found");
        }

        User user = optionalUser.get();

        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(PasswordUtil.hashPassword(registrationDTO.getPassword()));
        user.setRole(registrationDTO.getRole());

        if (registrationDTO.getRole() == Role.EMPLOYEE) {
            Employee employee = (Employee) user;
            employee.setDepartment(registrationDTO.getDepartment());
            employee.setJobTitle(registrationDTO.getJobTitle());
        } else if (registrationDTO.getRole() == Role.MENTOR) {
            Mentor mentor = (Mentor) user;
            mentor.setExpertiseArea(registrationDTO.getExpertiseArea());
            mentor.setYearsOfExperience(registrationDTO.getYearsOfExperience());
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid role");
        }

        return userRepository.save(user);
    }

    @Override
    public User userDetails(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found"));
    }

    @Override
    public List<User> allMentors() {
        return userRepository.findAllByRole(Role.MENTOR);
    }

    @Override
    public List<User> allEmployees() {
        return userRepository.findAllByRole(Role.EMPLOYEE);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<AggregatedResultDTO> calculateAggregatedResults() {
        List<User> employees = userRepository.findAllByRole(Role.EMPLOYEE);
        List<AggregatedResultDTO> results = new ArrayList<>();

        for (User employee : employees) {
            AggregatedResultDTO result = new AggregatedResultDTO();
            result.setEmployeeId(employee.getId());
            result.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());

            List<SkillAssessment> assessments = skillAssessmentRepository.findByUserId(employee.getId());
            result.setSkillAssessmentPercentage(calculateSkillAssessmentPercentage(assessments));

            List<CareerPath> careerPaths = careerPathRepository.findCareerPathByEmployee(employee);
            result.setCareerPathProgressPercentage(calculateCareerPathProgress(careerPaths));

            List<Training> trainings = trainingRepository.findByUser(employee);
            result.setTrainingProgramsCount(trainings.size());

            List<EmployeeGoal> goals = employeeGoalRepository.findEmployeeGoalByEmployeeIdAndStatusIn(
                    employee.getId(),
                    List.of(GoalStatus.NOT_STARTED, GoalStatus.IN_PROGRESS)
            );
            result.setIncompleteGoalsCount(goals.size());

            results.add(result);
        }

        return results;
    }

    @Override
    public int calculateSkillAssessmentPercentage(List<SkillAssessment> assessments) {
        if (assessments.isEmpty()) return 0;
        double totalScore = assessments.stream().mapToInt(SkillAssessment::getScore).sum();
        return (int) (totalScore / assessments.size());
    }

    @Override
    public int calculateCareerPathProgress(List<CareerPath> careerPaths) {
        if (careerPaths == null || careerPaths.isEmpty()) {
            return 0;
        }

        int totalSteps = 0;
        int completedSteps = 0;

        for (CareerPath careerPath : careerPaths) {
            if (careerPath != null && careerPath.getSteps() != null) {
                totalSteps += careerPath.getSteps().size();
                completedSteps += (int) careerPath.getSteps().stream().filter(CareerPathStep::isDone).count();
            }
        }

        if (totalSteps == 0) {
            return 0;
        }

        return (int) ((completedSteps * 100) / totalSteps);
    }

}
