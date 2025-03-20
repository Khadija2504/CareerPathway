package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.*;
import com.CareerPathway.CareerPathway.repository.*;
import com.CareerPathway.CareerPathway.service.impl.QuestionnaireServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuestionnaireServiceImplTest {

    @Mock
    private QuestionnaireRepository questionnaireRepository;

    @Mock
    private SkillAssessmentRepository skillAssessmentRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuestionnaireServiceImpl questionnaireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    private Skill createSkill() {
        Skill skill = new Skill();
        skill.setId(1L);
        skill.setName("programming");
        return skill;
    }

    @Test
    void getQuestionnairesBySkillId_ShouldReturnList() {
        List<String> options1 = new ArrayList<>();
        options1.add("A programming language");
        options1.add("language java");

        List<String> options2 = new ArrayList<>();
        options2.add("A programming language");
        options2.add("language java");

        List<Questionnaire> mockQuestionnaires = Arrays.asList(
                new Questionnaire(1L, "What is Java?", options1, "B", createSkill()),
                new Questionnaire(2L, "What is OOP?", options2, "A", createSkill())
        );

        when(questionnaireRepository.findBySkillId(1L)).thenReturn(mockQuestionnaires);

        List<Questionnaire> result = questionnaireService.getQuestionnairesBySkillId(1L);

        assertEquals(2, result.size());
        assertEquals("What is Java?", result.get(0).getQuestionText());
    }

    @Test
    void submitQuestionnaireResponses_ShouldReturnAssessment() {
        // Arrange
        List<String> options1 = new ArrayList<>();
        options1.add("A programming language");
        options1.add("language java");

        List<String> options2 = new ArrayList<>();
        options2.add("Object-Oriented Programming");
        options2.add("language angular");

        Skill mockSkill = new Skill();
        mockSkill.setId(1L);
        mockSkill.setName("Java");

        User mockUser = new User();
        mockUser.setId(1L);

        List<Questionnaire> mockQuestionnaires = Arrays.asList(
                new Questionnaire(1L, "What is Java?", options1, "B", mockSkill),
                new Questionnaire(2L, "What is OOP?", options2, "A", mockSkill)
        );

        List<String> responses = Arrays.asList("B", "A");

        when(questionnaireRepository.findBySkillId(1L)).thenReturn(mockQuestionnaires);
        when(skillRepository.findSkillById(1L)).thenReturn(mockSkill);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(skillAssessmentRepository.save(any(SkillAssessment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SkillAssessment result = questionnaireService.submitQuestionnaireResponses(1L, 1L, responses);

        // Assert
        assertEquals(100, result.getScore());
        assertEquals(2, result.getStrengths().size());
        assertEquals(0, result.getWeaknesses().size());
    }

    @Test
    void submitQuestionnaireResponses_ShouldThrowException_WhenSkillNotFound() {
        when(questionnaireRepository.findBySkillId(1L)).thenReturn(Collections.emptyList());
        when(skillRepository.findSkillById(1L)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () ->
                questionnaireService.submitQuestionnaireResponses(1L, 1L, Arrays.asList("A")));
    }

    @Test
    void createQuestionnaire_ShouldReturnSavedQuestionnaire() {
        List<String> options = new ArrayList<>();
        options.add("A programming language");
        options.add("language java");
        Questionnaire questionnaire = new Questionnaire(1L, "What is Java?", options, "B", createSkill());
        when(questionnaireRepository.save(any(Questionnaire.class))).thenReturn(questionnaire);

        Questionnaire result = questionnaireService.createQuestionnaire(questionnaire);

        assertNotNull(result);
        assertEquals("What is Java?", result.getQuestionText());
    }

    @Test
    void deleteQuestionnaire_ShouldReturnTrue_WhenExists() {
        doNothing().when(questionnaireRepository).deleteById(1L);

        assertDoesNotThrow(() -> questionnaireService.deleteQuestionnaire(1L));
    }

    @Test
    void deleteQuestionnaire_ShouldThrowException_WhenNotFound() {
        doThrow(new EmptyResultDataAccessException(1)).when(questionnaireRepository).deleteById(1L);

        assertThrows(ResponseStatusException.class, () -> questionnaireService.deleteQuestionnaire(1L));
    }
}
