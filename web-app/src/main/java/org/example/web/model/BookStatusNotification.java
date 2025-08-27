package org.example.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_status_notifications")
public class BookStatusNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnore
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isRead = false;

    @Column
    private LocalDateTime readAt;

    @Column(nullable = false)
    private boolean isEmailSent = false;

    @Column
    private LocalDateTime emailSentAt;

    @Column(columnDefinition = "TEXT")
    private String emailContent;

    // Notification types
    public enum NotificationType {
        BOOK_BORROWED("Book Borrowed"),
        BOOK_RETURNED("Book Returned"),
        BOOK_RESERVED("Book Reserved"),
        BOOK_AVAILABLE("Book Available"),
        BOOK_MAINTENANCE("Book Under Maintenance"),
        BOOK_LOST("Book Lost"),
        BOOK_DAMAGED("Book Damaged"),
        DUE_DATE_REMINDER("Due Date Reminder"),
        OVERDUE_NOTICE("Overdue Notice"),
        RESERVATION_READY("Reservation Ready"),
        STATUS_CHANGE("Status Change");

        private final String displayName;

        NotificationType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public BookStatusNotification() {}

    public BookStatusNotification(Book book, User user, NotificationType type, String title, String message) {
        this.book = book;
        this.user = user;
        this.type = type;
        this.title = title;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { 
        this.isRead = read; 
        if (read && this.readAt == null) {
            this.readAt = LocalDateTime.now();
        }
    }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public boolean isEmailSent() { return isEmailSent; }
    public void setEmailSent(boolean emailSent) { 
        this.isEmailSent = emailSent; 
        if (emailSent && this.emailSentAt == null) {
            this.emailSentAt = LocalDateTime.now();
        }
    }

    public LocalDateTime getEmailSentAt() { return emailSentAt; }
    public void setEmailSentAt(LocalDateTime emailSentAt) { this.emailSentAt = emailSentAt; }

    public String getEmailContent() { return emailContent; }
    public void setEmailContent(String emailContent) { this.emailContent = emailContent; }

    // Helper methods for JSON serialization
    public String getBookId() {
        return book != null ? book.getId() : null;
    }

    public String getBookTitle() {
        return book != null ? book.getTitle() : null;
    }

    public String getUserId() {
        return user != null ? user.getId() : null;
    }

    public String getUserName() {
        return user != null ? user.getFullName() : null;
    }

    public String getFormattedCreatedAt() {
        if (createdAt != null) {
            return createdAt.toString().replace('T', ' ').substring(0, 19);
        }
        return "";
    }

    public String getFormattedReadAt() {
        if (readAt != null) {
            return readAt.toString().replace('T', ' ').substring(0, 19);
        }
        return "";
    }

    public String getFormattedEmailSentAt() {
        if (emailSentAt != null) {
            return emailSentAt.toString().replace('T', ' ').substring(0, 19);
        }
        return "";
    }

    public String getTypeDisplayName() {
        return type != null ? type.getDisplayName() : "";
    }

    public boolean isUnread() {
        return !isRead;
    }

    public long getAgeInHours() {
        if (createdAt == null) return 0;
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toHours();
    }

    public boolean isRecent() {
        return getAgeInHours() < 24; // Less than 24 hours old
    }
}
