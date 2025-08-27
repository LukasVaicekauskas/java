package org.example.web.service;

import org.example.web.model.Book;
import org.example.web.model.BookStatusHistory;
import org.example.web.model.BookStatusNotification;
import org.example.web.model.User;
import org.example.web.repository.BookRepository;
import org.example.web.repository.BookStatusHistoryRepository;
import org.example.web.repository.BookStatusNotificationRepository;
import org.example.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookStatusService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookStatusHistoryRepository statusHistoryRepository;

    @Autowired
    private BookStatusNotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Change book status and record history
     */
    @Transactional
    public Book changeBookStatus(String bookId, Book.BookStatus newStatus, String userId, String reason, String notes) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Book.BookStatus oldStatus = book.getStatus();

        // Update book status
        book.setStatus(newStatus);
        Book savedBook = bookRepository.save(book);

        // Record status change history
        BookStatusHistory history = new BookStatusHistory(book, oldStatus, newStatus, user, reason, notes);
        statusHistoryRepository.save(history);

        // Create notifications for relevant users
        createStatusChangeNotifications(book, oldStatus, newStatus, user, reason);

        return savedBook;
    }

    /**
     * Change book status without reason
     */
    @Transactional
    public Book changeBookStatus(String bookId, Book.BookStatus newStatus, String userId) {
        return changeBookStatus(bookId, newStatus, userId, null, null);
    }

    /**
     * Bulk status change for multiple books
     */
    @Transactional


    /**
     * Get status history for a book
     */
    public List<BookStatusHistory> getBookStatusHistory(String bookId) {
        return statusHistoryRepository.findByBookIdOrderByChangedAtDesc(bookId);
    }

    /**
     * Get recent status changes
     */
    public List<BookStatusHistory> getRecentStatusChanges(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return statusHistoryRepository.findRecentStatusChanges(since);
    }





    /**
     * Get status changes from one status to another
     */
    public List<BookStatusHistory> getStatusChangesByTransition(Book.BookStatus oldStatus, Book.BookStatus newStatus) {
        return statusHistoryRepository.findByOldStatusAndNewStatusOrderByChangedAtDesc(oldStatus.toString(), newStatus.toString());
    }





    /**
     * Create notifications for status changes
     */
    private void createStatusChangeNotifications(Book book, Book.BookStatus oldStatus, Book.BookStatus newStatus, User changedByUser, String reason) {
        // Notify book owner
        if (!book.getOwner().getId().equals(changedByUser.getId())) {
            String title = "Book Status Changed";
            String message = String.format("Your book '%s' status changed from %s to %s by %s",
                book.getTitle(), oldStatus, newStatus, changedByUser.getFullName());

            if (reason != null && !reason.isEmpty()) {
                message += ". Reason: " + reason;
            }

            BookStatusNotification notification = new BookStatusNotification(
                book, book.getOwner(), BookStatusNotification.NotificationType.STATUS_CHANGE, title, message
            );
            notificationRepository.save(notification);
        }

        // Create specific notifications based on status change
        switch (newStatus) {
            case AVAILABLE:
                createAvailableNotification(book, changedByUser);
                break;
            case BORROWED:
                createBorrowedNotification(book, changedByUser);
                break;
            case RESERVED:
                createReservedNotification(book, changedByUser);
                break;
            case MAINTENANCE:
                createMaintenanceNotification(book, changedByUser, reason);
                break;
            case LOST:
                createLostNotification(book, changedByUser, reason);
                break;
            case DAMAGED:
                createDamagedNotification(book, changedByUser, reason);
                break;
        }
    }

    /**
     * Create notification when book becomes available
     */
    private void createAvailableNotification(Book book, User changedByUser) {
        String title = "Book Available";
        String message = String.format("The book '%s' is now available for borrowing.", book.getTitle());

        BookStatusNotification notification = new BookStatusNotification(
            book, book.getOwner(), BookStatusNotification.NotificationType.BOOK_AVAILABLE, title, message
        );
        notificationRepository.save(notification);
    }

    /**
     * Create notification when book is borrowed
     */
    private void createBorrowedNotification(Book book, User changedByUser) {
        String title = "Book Borrowed";
        String message = String.format("Your book '%s' has been borrowed by %s.", book.getTitle(), changedByUser.getFullName());

        BookStatusNotification notification = new BookStatusNotification(
            book, book.getOwner(), BookStatusNotification.NotificationType.BOOK_BORROWED, title, message
        );
        notificationRepository.save(notification);
    }

    /**
     * Create notification when book is reserved
     */
    private void createReservedNotification(Book book, User changedByUser) {
        String title = "Book Reserved";
        String message = String.format("Your book '%s' has been reserved by %s.", book.getTitle(), changedByUser.getFullName());

        BookStatusNotification notification = new BookStatusNotification(
            book, book.getOwner(), BookStatusNotification.NotificationType.BOOK_RESERVED, title, message
        );
        notificationRepository.save(notification);
    }

    /**
     * Create notification when book goes under maintenance
     */
    private void createMaintenanceNotification(Book book, User changedByUser, String reason) {
        String title = "Book Under Maintenance";
        String message = String.format("Your book '%s' is now under maintenance.", book.getTitle());

        if (reason != null && !reason.isEmpty()) {
            message += " Reason: " + reason;
        }

        BookStatusNotification notification = new BookStatusNotification(
            book, book.getOwner(), BookStatusNotification.NotificationType.BOOK_MAINTENANCE, title, message
        );
        notificationRepository.save(notification);
    }

    /**
     * Create notification when book is lost
     */
    private void createLostNotification(Book book, User changedByUser, String reason) {
        String title = "Book Lost";
        String message = String.format("Your book '%s' has been marked as lost.", book.getTitle());

        if (reason != null && !reason.isEmpty()) {
            message += " Reason: " + reason;
        }

        BookStatusNotification notification = new BookStatusNotification(
            book, book.getOwner(), BookStatusNotification.NotificationType.BOOK_LOST, title, message
        );
        notificationRepository.save(notification);
    }

    /**
     * Create notification when book is damaged
     */
    private void createDamagedNotification(Book book, User changedByUser, String reason) {
        String title = "Book Damaged";
        String message = String.format("Your book '%s' has been marked as damaged.", book.getTitle());

        if (reason != null && !reason.isEmpty()) {
            message += " Reason: " + reason;
        }

        BookStatusNotification notification = new BookStatusNotification(
            book, book.getOwner(), BookStatusNotification.NotificationType.BOOK_DAMAGED, title, message
        );
        notificationRepository.save(notification);
    }

    /**
     * Get notifications for a user
     */
    public List<BookStatusNotification> getUserNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Get unread notifications for a user
     */
    public List<BookStatusNotification> getUserUnreadNotifications(String userId) {
        return notificationRepository.findUnreadByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Mark notification as read
     */
    @Transactional
    public BookStatusNotification markNotificationAsRead(String notificationId) {
        BookStatusNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + notificationId));

        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    /**
     * Mark all notifications as read for a user
     */
    @Transactional
    public void markAllNotificationsAsRead(String userId) {
        notificationRepository.markAllAsReadByUserId(userId, LocalDateTime.now());
    }

    /**
     * Get notification count for a user
     */
    public long getUnreadNotificationCount(String userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    /**
     * Delete old notifications
     */
    @Transactional


    /**
     * Get recent notifications for a user
     */
    public List<BookStatusNotification> getRecentNotificationsForUser(String userId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return notificationRepository.findRecentNotificationsByUserId(userId, since);
    }
}
