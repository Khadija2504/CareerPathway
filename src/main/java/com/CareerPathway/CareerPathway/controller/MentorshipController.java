package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.MentorshipDTO;
import com.CareerPathway.CareerPathway.mapper.MentorshipMapper;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping("/employee/create-mentorship")
    public ResponseEntity<?> createMentorship(@RequestBody MentorshipDTO mentorshipDTO, HttpServletRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        int userId = Integer.parseInt(request.getAttribute("userId").toString());
        User user = userService.userDetails(userId);
        User mentor = userService.userDetails(mentorshipDTO.getMentorId());
        System.out.println(mentorshipDTO);
        Mentorship mentorship = mentorshipMapper.toEntity(mentorshipDTO);
        mentorship.setStatus(MentorshipStatus.Active);
        mentorship.setMentee(user);
        mentorship.setMentor(mentor);
        mentorship.setStartDate(LocalDateTime.now());
        Mentorship savedMentorship = mentorshipService.save(mentorship);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMentorship);
    }

    @PostMapping("/isMentorshipExist")
    public ResponseEntity<?> isMentorshipExist(@RequestBody Long mentorId, HttpServletRequest request) {
        long menteeId = Integer.parseInt(request.getAttribute("userId").toString());
        User mentee = userService.userDetails(menteeId);
        User mentor = userService.userDetails(mentorId);
        boolean isExist = mentorshipService.isMentorshipExist(mentor, mentee);
        System.out.println(isExist);
        return ResponseEntity.status(HttpStatus.OK).body(isExist);
    }

    @GetMapping("/employee/getAllEmployeeMentorShips")
    public ResponseEntity<?> getAllMentorShips(HttpServletRequest request) {
        long userId = Integer.parseInt(request.getAttribute("userId").toString());
        User mentee = userService.userDetails(userId);
        List<Mentorship> mentorships = mentorshipService.getAllEmployeeMentorships(mentee);
        return ResponseEntity.status(HttpStatus.OK).body(mentorships);
    }

    @GetMapping("/mentor/getAllMentorMentorShips")
    public ResponseEntity<?> getAllMentorMentorShips(HttpServletRequest request) {
        long userId = Integer.parseInt(request.getAttribute("userId").toString());
        User mentor = userService.userDetails(userId);
        List<Mentorship> mentorships = mentorshipService.getAllMentorMentorships(mentor);
        System.out.println(mentorships);
        return ResponseEntity.status(HttpStatus.OK).body(mentorships);
    }

    @PutMapping("/mentor/updateMentorshipStatus/{mentorshipId}")
    public ResponseEntity<?> updateMentorshipStatus(@RequestBody String statusStr, @PathVariable long mentorshipId) {
        MentorshipStatus status = MentorshipStatus.valueOf(statusStr);
        Mentorship mentorship = mentorshipService.updateMentorshipStatus(status, mentorshipId);
        return ResponseEntity.status(HttpStatus.OK).body(mentorship);
    }
}
