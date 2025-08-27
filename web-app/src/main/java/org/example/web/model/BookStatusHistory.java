package org.example.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_status_history")
public class BookStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnore
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Book.BookStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Book.BookStatus newStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id")
    @JsonIgnore
    private User changedByUser;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Constructors
    public BookStatusHistory() {}

    public BookStatusHistory(Book book, Book.BookStatus oldStatus, Book.BookStatus newStatus, User changedByUser) {
        this.book = book;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedByUser = changedByUser;
        this.changedAt = LocalDateTime.now();
    }

    public BookStatusHistory(Book book, Book.BookStatus oldStatus, Book.BookStatus newStatus, User changedByUser, String reason) {
        this(book, oldStatus, newStatus, changedByUser);
        this.reason = reason;
    }

    public BookStatusHistory(Book book, Book.BookStatus oldStatus, Book.BookStatus newStatus, User changedByUser, String reason, String notes) {
        this(book, oldStatus, newStatus, changedByUser, reason);
        this.notes = notes;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public Book.BookStatus getOldStatus() { return oldStatus; }
    public void setOldStatus(Book.BookStatus oldStatus) { this.oldStatus = oldStatus; }

    public Book.BookStatus getNewStatus() { return newStatus; }
    public void setNewStatus(Book.BookStatus newStatus) { this.newStatus = newStatus; }

    public User getChangedByUser() { return changedByUser; }
    public void setChangedByUser(User changedByUser) { this.changedByUser = changedByUser; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // Helper methods for JSON serialization
    public String getBookId() {
        return book != null ? book.getId() : null;
    }

    public String getBookTitle() {
        return book != null ? book.getTitle() : null;
    }

    public String getChangedByUserId() {
        return changedByUser != null ? changedByUser.getId() : null;
    }

    public String getChangedByUserName() {
        return changedByUser != null ? changedByUser.getFullName() : null;
    }

    public String getFormattedChangedAt() {
        if (changedAt != null) {
            return changedAt.toString().replace('T', ' ').substring(0, 19);
        }
        return "";
    }

    public String getStatusChangeDescription() {
        if (oldStatus == null || newStatus == null) {
            return "Status changed";
        }
        return oldStatus + " → " + newStatus;
    }

    public boolean isStatusUpgrade() {
        if (oldStatus == null || newStatus == null) {
            return false;
        }
        // Define status hierarchy (lower is better)
        int oldPriority = getStatusPriority(oldStatus);
        int newPriority = getStatusPriority(newStatus);
        return newPriority < oldPriority; // Lower number = better status
    }

    public boolean isStatusDowngrade() {
        if (oldStatus == null || newStatus == null) {
            return false;
        }
        int oldPriority = getStatusPriority(oldStatus);
        int newPriority = getStatusPriority(newStatus);
        return newPriority > oldPriority; // Higher number = worse status
    }

    private int getStatusPriority(Book.BookStatus status) {
        switch (status) {
            case AVAILABLE: return 1;
            case RESERVED: return 2;
            case BORROWED: return 3;
            case MAINTENANCE: return 4;
            case LOST: return 5;
            case DAMAGED: return 6;
            default: return 7;
        }
    }
}
