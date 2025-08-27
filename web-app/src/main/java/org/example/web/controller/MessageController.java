package org.example.web.controller;

import org.example.web.model.Message;
import org.example.web.model.User;
import org.example.web.service.MessageService;
import org.example.web.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// Request classes


@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;



    // Get messages for a specific user (frontend expects this endpoint)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageDto>> getUserMessages(@PathVariable String userId) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            List<Message> messages = messageService.getUserMessages(userId);
            List<MessageDto> messageDtos = messages.stream()
                .map(MessageDto::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(messageDtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Send a message (frontend expects this endpoint)
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@RequestBody SendMessageRequest request) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            Message message = messageService.sendMessage(
                currentUser.getId(),
                request.getBookId(),
                request.getContent(),
                request.getParentMessageId(),
                request.getRecipientId()
            );
            return ResponseEntity.ok(new MessageDto(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }















    // Delete a message (soft delete)
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String messageId) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            boolean deleted = messageService.deleteMessage(messageId, currentUser.getId());
            return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Inner classes for request/response
    public static class SendMessageRequest {
        private String bookId;
        private String recipientId; // User can explicitly choose recipient
        private String content;
        private String parentMessageId;

        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        public String getRecipientId() { return recipientId; }
        public void setRecipientId(String recipientId) { this.recipientId = recipientId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getParentMessageId() { return parentMessageId; }
        public void setParentMessageId(String parentMessageId) { this.parentMessageId = parentMessageId; }
    }


}
