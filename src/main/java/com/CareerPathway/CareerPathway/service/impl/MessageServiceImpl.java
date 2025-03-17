package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Message;
import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.MessageRepository;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import com.CareerPathway.CareerPathway.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Message sendMessage(Message message) {
        if (message == null || message.getSender() == null || message.getReceiver() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message, sender, and receiver cannot be null");
        }

        try {
            Notification notification = new Notification();
            notification.setRead(false);
            notification.setUser(message.getReceiver());
            String msg = "You have a new message from mentor Mr." + message.getSender().getLastName();
            notification.setMessage(msg);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);

            return messageRepository.save(message);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending message", e);
        }
    }

    @Override
    public List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        if (senderId == null || receiverId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender ID and receiver ID cannot be null");
        }

        try {
            List<Message> messages = messageRepository.findMessagesBetweenUsers(senderId, receiverId);
            for (Message message : messages) {
                message.setRead(true);
            }
            messageRepository.saveAll(messages);
            return messages;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving messages between users", e);
        }
    }

    @Override
    public List<Message> getUnreadMessages(User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be null");
        }

        try {
            return messageRepository.findByReceiverAndRead(user, false);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving unread messages", e);
        }
    }

    @Override
    public List<Message> getUnreadMessagesBetweenUsers(Long senderId, Long receiverId) {
        if (senderId == null || receiverId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender ID and receiver ID cannot be null");
        }

        try {
            List<Message> messages = messageRepository.findMessagesBetweenUsersByRead(senderId, receiverId);
            messageRepository.saveAll(messages);
            return messages;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving unread messages between users", e);
        }
    }
}