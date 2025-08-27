package org.example.model;

import java.time.LocalDateTime;

public class BookStatusNotification {
    private String id;
    private String bookId;
    private String bookTitle;
    private String userId;
    private String userName;
    private NotificationType type;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private boolean isRead;
    private LocalDateTime readAt;
    private boolean isEmailSent;
    private LocalDateTime emailSentAt;
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

    public BookStatusNotification(String bookId, String userId, NotificationType type, String title, String message) {
        this.bookId = bookId;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { this.isRead = read; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public boolean isEmailSent() { return isEmailSent; }
    public void setEmailSent(boolean emailSent) { this.isEmailSent = emailSent; }

    public LocalDateTime getEmailSentAt() { return emailSentAt; }
    public void setEmailSentAt(LocalDateTime emailSentAt) { this.emailSentAt = emailSentAt; }

    public String getEmailContent() { return emailContent; }
    public void setEmailContent(String emailContent) { this.emailContent = emailContent; }

    // Helper methods
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

    public String getNotificationIcon() {
        if (type == null) return "📢";
        
        switch (type) {
            case BOOK_BORROWED: return "📖";
            case BOOK_RETURNED: return "📚";
            case BOOK_RESERVED: return "🔖";
            case BOOK_AVAILABLE: return "✅";
            case BOOK_MAINTENANCE: return "🔧";
            case BOOK_LOST: return "❌";
            case BOOK_DAMAGED: return "💔";
            case DUE_DATE_REMINDER: return "⏰";
            case OVERDUE_NOTICE: return "🚨";
            case RESERVATION_READY: return "🎉";
            case STATUS_CHANGE: return "🔄";
            default: return "📢";
        }
    }

    public String getNotificationColor() {
        if (type == null) return "blue";
        
        switch (type) {
            case BOOK_BORROWED: return "blue";
            case BOOK_RETURNED: return "green";
            case BOOK_RESERVED: return "orange";
            case BOOK_AVAILABLE: return "green";
            case BOOK_MAINTENANCE: return "yellow";
            case BOOK_LOST: return "red";
            case BOOK_DAMAGED: return "red";
            case DUE_DATE_REMINDER: return "orange";
            case OVERDUE_NOTICE: return "red";
            case RESERVATION_READY: return "green";
            case STATUS_CHANGE: return "blue";
            default: return "blue";
        }
    }
}
