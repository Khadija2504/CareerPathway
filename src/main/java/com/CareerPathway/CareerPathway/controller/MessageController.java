package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.MessageDTO;
import com.CareerPathway.CareerPathway.mapper.MessageMapper;
import com.CareerPathway.CareerPathway.model.Message;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.service.MessageService;
import com.CareerPathway.CareerPathway.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserService userService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO, HttpServletRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        long userId = Long.parseLong(request.getAttribute("userId").toString());
//        messageDTO.setSenderId(userId);

//        if (messageDTO.getReceiverId() == null) {
//            return ResponseEntity.badRequest().body("Receiver ID is required");
//        }
        User sender = userService.userDetails(userId);
        User receiver = userService.userDetails(messageDTO.getReceiverId());
        Message message = messageMapper.toEntity(messageDTO);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);
        Message savedMessage = messageService.sendMessage(message);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/between")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(
            @RequestParam Long receiverId, HttpServletRequest request) {
        System.out.println(receiverId);
        long senderId = Long.parseLong(request.getAttribute("userId").toString());
        System.out.println(senderId);
        List<Message> messages = messageService.getMessagesBetweenUsers(senderId, receiverId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getUnreadMessages(HttpServletRequest request) {
        long userId = Long.parseLong(request.getAttribute("userId").toString());
        User user = userService.userDetails(userId);
        List<Message> unreadMessages = messageService.getUnreadMessages(user);
        return ResponseEntity.ok(unreadMessages);
    }
}
