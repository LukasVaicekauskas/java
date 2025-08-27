package org.example.web.service;

import org.example.web.model.Book;
import org.example.web.model.Transaction;
import org.example.web.model.User;
import org.example.web.model.Message;
import org.example.web.model.Comment;
import org.example.web.model.BookStatusHistory;
import org.example.web.repository.BookRepository;
import org.example.web.repository.TransactionRepository;
import org.example.web.repository.UserRepository;
import org.example.web.repository.MessageRepository;
import org.example.web.repository.CommentRepository;
import org.example.web.repository.BookStatusHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookStatusHistoryRepository bookStatusHistoryRepository;



    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public boolean deleteUser(String userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public boolean deleteBook(String bookId) {
        if (bookRepository.existsById(bookId)) {
            bookRepository.deleteById(bookId);
            return true;
        }
        return false;
    }









    // DTO classes for admin dashboard
    public static class UserActionLog {
        private String id;
        private String userId;
        private String userName;
        private String actionType;
        private String description;
        private String targetId;
        private String targetType;
        private LocalDateTime timestamp;
        private String ipAddress;
        private String userAgent;

        public UserActionLog() {}

        public UserActionLog(String userId, String userName, String actionType, String description,
                           String targetId, String targetType) {
            this.userId = userId;
            this.userName = userName;
            this.actionType = actionType;
            this.description = description;
            this.targetId = targetId;
            this.targetType = targetType;
            this.timestamp = LocalDateTime.now();
        }

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getActionType() { return actionType; }
        public void setActionType(String actionType) { this.actionType = actionType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public String getTargetType() { return targetType; }
        public void setTargetType(String targetType) { this.targetType = targetType; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public String getUserAgent() { return userAgent; }
        public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    }

    public static class BookActivityLog {
        private String bookId;
        private String bookTitle;
        private String actionType;
        private String userId;
        private String userName;
        private LocalDateTime timestamp;
        private String details;

        public BookActivityLog() {}

        public BookActivityLog(String bookId, String bookTitle, String actionType, String userId, String userName, String details) {
            this.bookId = bookId;
            this.bookTitle = bookTitle;
            this.actionType = actionType;
            this.userId = userId;
            this.userName = userName;
            this.details = details;
            this.timestamp = LocalDateTime.now();
        }

        // Getters and setters
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        public String getBookTitle() { return bookTitle; }
        public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
        public String getActionType() { return actionType; }
        public void setActionType(String actionType) { this.actionType = actionType; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }
    }

    public static class MessageActivityLog {
        private String messageId;
        private String bookId;
        private String bookTitle;
        private String senderId;
        private String senderName;
        private String receiverId;
        private String receiverName;
        private String actionType;
        private LocalDateTime timestamp;

        public MessageActivityLog() {}

        public MessageActivityLog(String messageId, String bookId, String bookTitle, String senderId,
                                String senderName, String receiverId, String receiverName, String actionType) {
            this.messageId = messageId;
            this.bookId = bookId;
            this.bookTitle = bookTitle;
            this.senderId = senderId;
            this.senderName = senderName;
            this.receiverId = receiverId;
            this.receiverName = receiverName;
            this.actionType = actionType;
            this.timestamp = LocalDateTime.now();
        }

        // Getters and setters
        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        public String getBookTitle() { return bookTitle; }
        public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
        public String getSenderId() { return senderId; }
        public void setSenderId(String senderId) { this.senderId = senderId; }
        public String getSenderName() { return senderName; }
        public void setSenderName(String senderName) { this.senderName = senderName; }
        public String getReceiverId() { return receiverId; }
        public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
        public String getReceiverName() { return receiverName; }
        public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
        public String getActionType() { return actionType; }
        public void setActionType(String actionType) { this.actionType = actionType; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }

    public static class CommentActivityLog {
        private String commentId;
        private String bookId;
        private String bookTitle;
        private String authorId;
        private String authorName;
        private String actionType;
        private LocalDateTime timestamp;
        private String content;

        public CommentActivityLog() {}

        public CommentActivityLog(String commentId, String bookId, String bookTitle, String authorId,
                                String authorName, String actionType, String content) {
            this.commentId = commentId;
            this.bookId = bookId;
            this.bookTitle = bookTitle;
            this.authorId = authorId;
            this.authorName = authorName;
            this.actionType = actionType;
            this.content = content;
            this.timestamp = LocalDateTime.now();
        }

        // Getters and setters
        public String getCommentId() { return commentId; }
        public void setCommentId(String commentId) { this.commentId = commentId; }
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        public String getBookTitle() { return bookTitle; }
        public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
        public String getAuthorId() { return authorId; }
        public void setAuthorId(String authorId) { this.authorId = authorId; }
        public String getAuthorName() { return authorName; }
        public void setAuthorName(String authorName) { this.authorName = authorName; }
        public String getActionType() { return actionType; }
        public void setActionType(String actionType) { this.actionType = actionType; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    public static class StatusChangeActivityLog {
        private String bookId;
        private String bookTitle;
        private String oldStatus;
        private String newStatus;
        private String changedByUserId;
        private String changedByUserName;
        private LocalDateTime timestamp;
        private String reason;

        public StatusChangeActivityLog() {}

        public StatusChangeActivityLog(String bookId, String bookTitle, String oldStatus, String newStatus,
                                     String changedByUserId, String changedByUserName, String reason) {
            this.bookId = bookId;
            this.bookTitle = bookTitle;
            this.oldStatus = oldStatus;
            this.newStatus = newStatus;
            this.changedByUserId = changedByUserId;
            this.changedByUserName = changedByUserName;
            this.reason = reason;
            this.timestamp = LocalDateTime.now();
        }

        // Getters and setters
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        public String getBookTitle() { return bookTitle; }
        public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
        public String getOldStatus() { return oldStatus; }
        public void setOldStatus(String oldStatus) { this.oldStatus = oldStatus; }
        public String getNewStatus() { return newStatus; }
        public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
        public String getChangedByUserId() { return changedByUserId; }
        public void setChangedByUserId(String changedByUserId) { this.changedByUserId = changedByUserId; }
        public String getChangedByUserName() { return changedByUserName; }
        public void setChangedByUserName(String changedByUserName) { this.changedByUserName = changedByUserName; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    public static class LoginActivityLog {
        private String userId;
        private String userName;
        private LocalDateTime loginTime;
        private String ipAddress;
        private String userAgent;
        private boolean success;

        public LoginActivityLog() {}

        public LoginActivityLog(String userId, String userName, String ipAddress, String userAgent, boolean success) {
            this.userId = userId;
            this.userName = userName;
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.success = success;
            this.loginTime = LocalDateTime.now();
        }

        // Getters and setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public LocalDateTime getLoginTime() { return loginTime; }
        public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public String getUserAgent() { return userAgent; }
        public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
    }

    public static class AdminDashboardSummary {
        private long totalUsers;
        private long totalBooks;
        private long totalMessages;
        private long totalComments;
        private long activeUsersToday;
        private long booksAddedToday;
        private long statusChangesToday;
        private long messagesSentToday;
        private long commentsAddedToday;
        private List<Object[]> topActiveUsers;
        private List<Object[]> topBooks;
        private List<Object[]> recentActivity;

        public AdminDashboardSummary() {}

        // Getters and setters
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        public long getTotalBooks() { return totalBooks; }
        public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }
        public long getTotalMessages() { return totalMessages; }
        public void setTotalMessages(long totalMessages) { this.totalMessages = totalMessages; }
        public long getTotalComments() { return totalComments; }
        public void setTotalComments(long totalComments) { this.totalComments = totalComments; }
        public long getActiveUsersToday() { return activeUsersToday; }
        public void setActiveUsersToday(long activeUsersToday) { this.activeUsersToday = activeUsersToday; }
        public long getBooksAddedToday() { return booksAddedToday; }
        public void setBooksAddedToday(long booksAddedToday) { this.booksAddedToday = booksAddedToday; }
        public long getStatusChangesToday() { return statusChangesToday; }
        public void setStatusChangesToday(long statusChangesToday) { this.statusChangesToday = statusChangesToday; }
        public long getMessagesSentToday() { return messagesSentToday; }
        public void setMessagesSentToday(long messagesSentToday) { this.messagesSentToday = messagesSentToday; }
        public long getCommentsAddedToday() { return commentsAddedToday; }
        public void setCommentsAddedToday(long commentsAddedToday) { this.commentsAddedToday = commentsAddedToday; }
        public List<Object[]> getTopActiveUsers() { return topActiveUsers; }
        public void setTopActiveUsers(List<Object[]> topActiveUsers) { this.topActiveUsers = topActiveUsers; }
        public List<Object[]> getTopBooks() { return topBooks; }
        public void setTopBooks(List<Object[]> topBooks) { this.topBooks = topBooks; }
        public List<Object[]> getRecentActivity() { return recentActivity; }
        public void setRecentActivity(List<Object[]> recentActivity) { this.recentActivity = recentActivity; }
    }

    // Comprehensive admin dashboard methods for requirement #4

    public List<UserActionLog> getAllUserActions() {
        List<UserActionLog> actions = new ArrayList<>();

        // Collect book-related actions
        List<Book> allBooks = bookRepository.findAll();
        for (Book book : allBooks) {
            actions.add(new UserActionLog(
                book.getOwner().getId(),
                book.getOwner().getFullName(),
                "BOOK_CREATED",
                "Created book: " + book.getTitle(),
                book.getId(),
                "BOOK"
            ));
        }

        // Collect message-related actions
        List<Message> allMessages = messageRepository.findAll();
        for (Message message : allMessages) {
            actions.add(new UserActionLog(
                message.getSender().getId(),
                message.getSender().getFullName(),
                "MESSAGE_SENT",
                "Sent message about book: " + message.getBook().getTitle(),
                message.getId(),
                "MESSAGE"
            ));
        }

        // Collect comment-related actions
        List<Comment> allComments = commentRepository.findAll();
        for (Comment comment : allComments) {
            actions.add(new UserActionLog(
                comment.getAuthor().getId(),
                comment.getAuthor().getFullName(),
                "COMMENT_ADDED",
                "Added comment to book: " + comment.getBook().getTitle(),
                comment.getId(),
                "COMMENT"
            ));
        }

        // Collect status change actions
        List<BookStatusHistory> allStatusChanges = bookStatusHistoryRepository.findAll();
        for (BookStatusHistory history : allStatusChanges) {
            actions.add(new UserActionLog(
                history.getChangedByUser().getId(),
                history.getChangedByUser().getFullName(),
                "STATUS_CHANGED",
                "Changed book status: " + history.getBook().getTitle() + " from " +
                history.getOldStatus() + " to " + history.getNewStatus(),
                history.getBook().getId(),
                "BOOK_STATUS"
            ));
        }

        // Sort by timestamp (most recent first)
        return actions.stream()
            .sorted((a1, a2) -> a2.getTimestamp().compareTo(a1.getTimestamp()))
            .collect(Collectors.toList());
    }





    public List<UserActionLog> getActionsByType(String actionType) {
        return getAllUserActions().stream()
            .filter(action -> action.getActionType().equals(actionType))
            .collect(Collectors.toList());
    }












}
