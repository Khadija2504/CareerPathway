package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Message;
import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.MentorshipStatus;
import com.CareerPathway.CareerPathway.repository.MessageRepository;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import com.CareerPathway.CareerPathway.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Message sendMessage(Message message) {
        Notification notification = new Notification();
        notification.setRead(false);
        notification.setUser(message.getReceiver());
        String msg = "U have new message from mentor Mr." + message.getSender().getLastName();
        notification.setMessage(msg);
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        List<Message> messages = messageRepository.findMessagesBetweenUsers(senderId, receiverId);
        for (Message message : messages) {
            message.setRead(true);
        }
        messageRepository.saveAll(messages);
        return messages;
    }

    @Override
    public List<Message> getUnreadMessages(User user) {
        return messageRepository.findByReceiverAndRead(user, false);
    }
}