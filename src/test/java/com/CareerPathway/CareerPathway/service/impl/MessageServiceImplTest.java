package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Message;
import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.MessageRepository;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        return user;
    }

    private Message createMessage() {
        User sender = createUser();
        User receiver = createUser();
        receiver.setId(2L);

        Message message = new Message();
        message.setId(1L);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Hello!");
        message.setRead(false);
        return message;
    }

    @Test
    void sendMessage_Success() {
        Message message = createMessage();
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message result = messageService.sendMessage(message);

        assertNotNull(result);
        assertEquals("Hello!", result.getContent());
        verify(messageRepository, times(1)).save(message);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void sendMessage_NullMessage() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            messageService.sendMessage(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Message, sender, and receiver cannot be null", exception.getReason());
    }

    @Test
    void sendMessage_Error() {
        Message message = createMessage();
        when(messageRepository.save(any(Message.class))).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            messageService.sendMessage(message);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error sending message", exception.getReason());
    }

    @Test
    void getMessagesBetweenUsers_Success() {
        Message message1 = createMessage();
        Message message2 = createMessage();
        message2.setId(2L);

        when(messageRepository.findMessagesBetweenUsers(1L, 2L)).thenReturn(Arrays.asList(message1, message2));
        when(messageRepository.saveAll(anyList())).thenReturn(Arrays.asList(message1, message2));

        List<Message> result = messageService.getMessagesBetweenUsers(1L, 2L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(messageRepository, times(1)).findMessagesBetweenUsers(1L, 2L);
        verify(messageRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getMessagesBetweenUsers_NullIds() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            messageService.getMessagesBetweenUsers(null, null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Sender ID and receiver ID cannot be null", exception.getReason());
    }

    @Test
    void getMessagesBetweenUsers_Error() {
        when(messageRepository.findMessagesBetweenUsers(1L, 2L)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            messageService.getMessagesBetweenUsers(1L, 2L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving messages between users", exception.getReason());
    }

    @Test
    void getUnreadMessages_Success() {
        User user = createUser();
        Message message = createMessage();

        when(messageRepository.findByReceiverAndRead(user, false)).thenReturn(Arrays.asList(message));

        List<Message> result = messageService.getUnreadMessages(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(messageRepository, times(1)).findByReceiverAndRead(user, false);
    }

    @Test
    void getUnreadMessages_NullUser() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            messageService.getUnreadMessages(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User cannot be null", exception.getReason());
    }

    @Test
    void getUnreadMessages_Error() {
        User user = createUser();
        when(messageRepository.findByReceiverAndRead(user, false)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            messageService.getUnreadMessages(user);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving unread messages", exception.getReason());
    }

    @Test
    void getUnreadMessagesBetweenUsers_Success() {
        Message message1 = createMessage();
        Message message2 = createMessage();
        message2.setId(2L);

        when(messageRepository.findMessagesBetweenUsersByRead(1L, 2L)).thenReturn(Arrays.asList(message1, message2));
        when(messageRepository.saveAll(anyList())).thenReturn(Arrays.asList(message1, message2));

        List<Message> result = messageService.getUnreadMessagesBetweenUsers(1L, 2L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(messageRepository, times(1)).findMessagesBetweenUsersByRead(1L, 2L);
        verify(messageRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getUnreadMessagesBetweenUsers_NullIds() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            messageService.getUnreadMessagesBetweenUsers(null, null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Sender ID and receiver ID cannot be null", exception.getReason());
    }

    @Test
    void getUnreadMessagesBetweenUsers_Error() {
        when(messageRepository.findMessagesBetweenUsersByRead(1L, 2L)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            messageService.getUnreadMessagesBetweenUsers(1L, 2L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving unread messages between users", exception.getReason());
    }
}