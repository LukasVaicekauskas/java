package org.example.model;

import java.time.LocalDateTime;

public class BookStatusHistory {
    private String id;
    private String bookId;
    private String bookTitle;
    private String oldStatus;
    private String newStatus;
    private String changedByUserId;
    private String changedByUserName;
    private LocalDateTime changedAt;
    private String reason;
    private String notes;

    // Constructors
    public BookStatusHistory() {}

    public BookStatusHistory(String bookId, String oldStatus, String newStatus, String changedByUserId) {
        this.bookId = bookId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedByUserId = changedByUserId;
        this.changedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // Helper methods
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

    private int getStatusPriority(String status) {
        switch (status.toUpperCase()) {
            case "AVAILABLE": return 1;
            case "RESERVED": return 2;
            case "BORROWED": return 3;
            case "MAINTENANCE": return 4;
            case "LOST": return 5;
            case "DAMAGED": return 6;
            case "SOLD": return 7;
            default: return 8;
        }
    }

    public String getStatusChangeIcon() {
        if (isStatusUpgrade()) {
            return "✓"; // Checkmark for upgrades
        } else if (isStatusDowngrade()) {
            return "⚠"; // Warning for downgrades
        } else {
            return "→"; // Arrow for neutral changes
        }
    }

    public String getStatusChangeColor() {
        if (isStatusUpgrade()) {
            return "green";
        } else if (isStatusDowngrade()) {
            return "red";
        } else {
            return "blue";
        }
    }
}
