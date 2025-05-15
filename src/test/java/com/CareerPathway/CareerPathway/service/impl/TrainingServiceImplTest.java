package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.*;
import com.CareerPathway.CareerPathway.model.enums.Level;
import com.CareerPathway.CareerPathway.repository.*;
import com.CareerPathway.CareerPathway.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainingServiceImplTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private SkillAssessmentRepository skillAssessmentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private TrainingStepRepository trainingStepRepository;
    @Mock
    private UserService userService;

    private Skill skill;
    private User user;
    private List<SkillAssessment> skillAssessments;
    private Training training;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        skill = new Skill();
        skill.setId(1L);
        skill.setName("Java");

        user = new User();
        user.setId(1L);

        skillAssessments = new ArrayList<>();
        SkillAssessment skillAssessment = new SkillAssessment();
        skillAssessment.setUser(user);
        skillAssessment.setSkill(skill);
        skillAssessments.add(skillAssessment);

        training = Training.builder()
                .title("Java: Mastering Exception Handling")
                .description("This training program is designed to improve your Java skills by focusing on the following areas: Exception Handling.")
                .provider("Career Pathway Training Provider")
                .duration(6)
                .level(Level.BEGINNER)
                .user(user)
                .skillsCovered(Collections.singletonList("Java"))
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    void generateTrainingProgram() {
        Long userId = 1L;
        Long skillId = 1L;
        List<String> weaknesses = Collections.singletonList("exception handling");
        List<String> skillGaps = Collections.singletonList("multithreading");
        int score = 30;

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skill));
        when(trainingRepository.save(any(Training.class))).thenReturn(training);

        trainingService.generateTrainingProgram(userId, skillId, weaknesses, skillGaps, score);

        verify(trainingRepository, times(1)).save(any(Training.class));

        assertNotNull(training);
        assertEquals("Java: Mastering Exception Handling", training.getTitle());
        assertEquals(6, training.getDuration());
        assertEquals(Level.BEGINNER, training.getLevel());
    }

    @Test
    void calculateDuration() {
        List<String> weaknesses = Arrays.asList("exception handling", "multithreading");
        List<String> skillGaps = Collections.singletonList("lambda expressions");

        int duration = trainingService.calculateDuration(weaknesses, skillGaps);

        assertEquals(6 + 8 + 4, duration);
    }

    @Test
    void getTopicDuration() {
        assertEquals(6, trainingService.getTopicDuration("exception handling"));
        assertEquals(8, trainingService.getTopicDuration("multithreading"));
        assertEquals(5, trainingService.getTopicDuration("stream api"));
        assertEquals(4, trainingService.getTopicDuration("lambda expressions"));
        assertEquals(5, trainingService.getTopicDuration("unknown topic"));
    }

    @Test
    void generateTrainingTitle() {
        List<String> weaknesses = Arrays.asList("exception handling", "multithreading");
        String title = trainingService.generateTrainingTitle("Java", weaknesses);
        assertEquals("Java: Mastering exception handling, multithreading", title);
    }

    @Test
    void generateTrainingDescription() {
        List<String> weaknesses = Arrays.asList("exception handling", "multithreading");
        List<String> skillGaps = Collections.singletonList("lambda expressions");

        String description = trainingService.generateTrainingDescription("Java", weaknesses, skillGaps);
        assertTrue(description.contains("This training program is designed to improve your Java skills"));
        assertTrue(description.contains("exception handling, multithreading"));
    }

    @Test
    void getRecommendations() {
        Long userId = 1L;
        when(skillAssessmentRepository.findByUserId(userId)).thenReturn(skillAssessments);
        when(trainingRepository.findTop1ByUserIdOrderByCreatedDateDesc(userId)).thenReturn(Collections.singletonList(training));
        when(courseRepository.findByCategoryIn(anyList())).thenReturn(Collections.singletonList(new Course()));
        when(resourceRepository.findByCategoryIn(anyList())).thenReturn(Collections.singletonList(new Resource()));

        Map<String, Object> recommendations = trainingService.getRecommendations(userId);

        assertNotNull(recommendations);
        assertTrue(recommendations.containsKey("courses"));
        assertTrue(recommendations.containsKey("resources"));
        assertTrue(recommendations.containsKey("trainingPrograms"));
    }

    @Test
    void getAdditionalTrainingPrograms() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Training> page = new PageImpl<>(Collections.singletonList(training), pageable, 1);
        when(trainingRepository.findByUser_Id(eq(userId), eq(pageable))).thenReturn(page);

        List<Training> additionalTrainingPrograms = trainingService.getAdditionalTrainingPrograms(userId, 0, 5);

        assertNotNull(additionalTrainingPrograms);
        assertEquals(1, additionalTrainingPrograms.size());
        assertEquals(training, additionalTrainingPrograms.get(0));
    }

    @Test
    void createTrainingStep_shouldSaveStepsAndSendNotification() {
        Training training = Training.builder()
                .id(1L)
                .user(user)
                .title("Java Training")
                .build();
        User mentor = new User();
        mentor.setId(2L);
        mentor.setLastName("Smith");

        TrainingStep step = new TrainingStep();
        step.setTitle("Step 1");

        when(trainingRepository.findById(1L)).thenReturn(Optional.of(training));
        when(userService.findById(2L)).thenReturn(Optional.of(mentor));
        when(trainingStepRepository.save(any())).thenReturn(step);

        List<TrainingStep> steps = trainingService.createTrainingStep(1L, 2L, Collections.singletonList(step));

        assertEquals(1, steps.size());
        verify(notificationRepository).save(any(Notification.class));
    }
    @Test
    void getAdditionalTrainingPrograms_shouldReturnPagedResult() {
        Page<Training> page = new PageImpl<>(List.of(training));
        when(trainingRepository.findByUser_Id(eq(user.getId()), any(Pageable.class))).thenReturn(page);

        List<Training> result = trainingService.getAdditionalTrainingPrograms(user.getId(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

}
