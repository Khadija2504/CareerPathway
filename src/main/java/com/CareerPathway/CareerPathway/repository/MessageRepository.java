package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Message;
import com.CareerPathway.CareerPathway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) OR (m.sender.id = :receiverId AND m.receiver.id = :senderId)")
    List<Message> findMessagesBetweenUsers(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    List<Message> findByReceiverAndRead(User receiver, boolean read);
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.read = false) OR (m.sender.id = :receiverId AND m.receiver.id = :senderId AND m.read = false)")
    List<Message> findMessagesBetweenUsersByRead(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}