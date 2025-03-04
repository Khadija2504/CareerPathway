package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Message;
import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.enums.MentorshipStatus;
import com.CareerPathway.CareerPathway.repository.MessageRepository;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import com.CareerPathway.CareerPathway.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    private NotificationRepository notificationRepository;

    @Override
    public Message sendMessage(Message message) {
        Notification notification = new Notification();
        notification.setRead(false);
        notification.setEmployee(message.getReceiver());
        String msg = "U have new message from mentor Mr." + message.getSender().getLastName();
        notification.setMessage(msg);
        notificationRepository.save(notification);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        return messageRepository.findMessagesBetweenUsers(senderId, receiverId);
    }
}