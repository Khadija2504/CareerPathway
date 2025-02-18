package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Message;
import com.CareerPathway.CareerPathway.repository.MessageRepository;
import com.CareerPathway.CareerPathway.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
    }
}
