package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Message;
import com.CareerPathway.CareerPathway.model.User;

import java.util.List;

public interface MessageService {
    List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId);
    Message sendMessage(Message message);
    List<Message> getUnreadMessages(User user);
    List<Message> getUnreadMessagesBetweenUsers(Long senderId, Long receiverId);
}
