package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId);
    Message sendMessage(Message message);
}
