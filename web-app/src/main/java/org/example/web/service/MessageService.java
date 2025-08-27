package org.example.web.service;

import org.example.web.model.Book;
import org.example.web.model.Message;
import org.example.web.model.User;
import org.example.web.repository.BookRepository;
import org.example.web.repository.MessageRepository;
import org.example.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(String id) {
        return messageRepository.findById(id);
    }

    public Message createMessage(String senderId, String bookId, String content, String parentMessageId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Message parentMessage = null;
        if (parentMessageId != null) {
            parentMessage = messageRepository.findById(parentMessageId)
                    .orElseThrow(() -> new RuntimeException("Parent message not found"));
        }

        Message message = new Message();
        message.setSender(sender);
        message.setBook(book);
        message.setContent(content);
        message.setParentMessage(parentMessage);
        message.setCreatedAt(LocalDateTime.now());
        message.setRead(false);
        message.setDepth(parentMessage != null ? parentMessage.getDepth() + 1 : 0);

        return messageRepository.save(message);
    }



    public List<Message> getUserMessages(String userId) {
        // Fetch messages where user is sender, book owner, or recipient
        List<Message> sent = messageRepository.findBySenderIdOrderByCreatedAtDesc(userId);
        List<Message> owned = messageRepository.findByBookOwnerIdAndSenderIdNotOrderByCreatedAtDesc(userId, userId);
        List<Message> received = messageRepository.findByRecipientIdAndDeletedByRecipientFalseOrderByCreatedAtDesc(userId);
        List<Message> all = new ArrayList<>();
        all.addAll(sent);
        all.addAll(owned);
        all.addAll(received);
        // Remove duplicates by message id
        return all.stream().collect(Collectors.toMap(Message::getId, m -> m, (m1, m2) -> m1)).values().stream()
            .sorted((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()))
            .collect(Collectors.toList());
    }

    public List<Message> getUserSentMessages(String userId) {
        return messageRepository.findBySenderIdOrderByCreatedAtDesc(userId);
    }

    public List<Message> getUserReceivedMessages(String userId) {
        return messageRepository.findByBookOwnerIdAndSenderIdNotOrderByCreatedAtDesc(userId, userId);
    }

    public Message markAsRead(String messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setRead(true);
        return messageRepository.save(message);
    }

    public boolean deleteMessage(String messageId, String userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Only allow deletion by sender or recipient
        if (!message.getSender().getId().equals(userId) &&
            (message.getRecipient() == null || !message.getRecipient().getId().equals(userId))) {
            throw new RuntimeException("User not authorized to delete this message");
        }

        // Soft delete - mark as deleted but keep in database
        message.setDeleted(true);
        messageRepository.save(message);
        return true;
    }

    public List<Message> getUnreadMessages(String userId) {
        return messageRepository.findByBookOwnerIdAndSenderIdNotAndReadFalseOrderByCreatedAtDesc(userId, userId);
    }

    public int getUnreadMessageCount(String userId) {
        return messageRepository.countByBookOwnerIdAndSenderIdNotAndReadFalse(userId, userId);
    }

    // New methods for the updated messaging system
    public Message sendMessage(String senderId, String bookId, String content, String parentMessageId, String recipientId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Message parentMessage = null;
        if (parentMessageId != null && !parentMessageId.isEmpty()) {
            parentMessage = messageRepository.findById(parentMessageId)
                    .orElseThrow(() -> new RuntimeException("Parent message not found"));
        }

        Message message = new Message(book, sender, content, parentMessage);

        // If recipientId is provided, use it
        if (recipientId != null && !recipientId.isEmpty()) {
            User recipient = userRepository.findById(recipientId)
                    .orElseThrow(() -> new RuntimeException("Recipient not found"));
            message.setRecipient(recipient);
        }
        // If no recipientId provided, set book owner as recipient (unless sender is the book owner)
        else if (!sender.getId().equals(book.getOwner().getId())) {
            message.setRecipient(book.getOwner());
        }
        // If sender is the book owner and no recipient specified, this is a general message to the book
        else {
            // For now, we'll set the book owner as recipient even if they're the sender
            // This allows for general book-related messages
            message.setRecipient(book.getOwner());
        }

        return messageRepository.save(message);
    }












}
