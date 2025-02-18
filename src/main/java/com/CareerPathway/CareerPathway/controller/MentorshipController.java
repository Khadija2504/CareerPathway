package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.MentorshipDTO;
import com.CareerPathway.CareerPathway.mapper.MentorshipMapper;
import com.CareerPathway.CareerPathway.model.Employee;
import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.MentorshipStatus;
import com.CareerPathway.CareerPathway.service.MentorshipService;
import com.CareerPathway.CareerPathway.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mentorship")
public class MentorshipController {
    @Autowired
    private MentorshipService mentorshipService;

    @Autowired
    private MentorshipMapper mentorshipMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/create-mentorship")
    public ResponseEntity<?> createMentorship(@RequestBody MentorshipDTO mentorshipDTO, HttpServletRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        int userId = Integer.parseInt(request.getAttribute("userId").toString());
        User user = userService.userDetails(userId);
        Mentorship mentorship = mentorshipMapper.toEntity(mentorshipDTO);
        mentorship.setStatus(MentorshipStatus.Active);
        mentorship.setMentee(user);
        mentorship.setStartDate(LocalDateTime.now());
        Mentorship savedMentorship = mentorshipService.save(mentorship);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMentorship);
    }
}
