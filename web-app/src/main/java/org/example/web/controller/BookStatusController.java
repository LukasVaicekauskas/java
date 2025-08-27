package org.example.web.controller;

import org.example.web.model.Book;
import org.example.web.model.BookStatusHistory;
import org.example.web.model.BookStatusNotification;
import org.example.web.model.User;
import org.example.web.service.BookStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/book-status")
@CrossOrigin(origins = "*")
public class BookStatusController {

    @Autowired
    private BookStatusService bookStatusService;

    // Change book status
    @PutMapping("/{bookId}/status")
    public ResponseEntity<Book> changeBookStatus(
            @PathVariable String bookId,
            @RequestBody ChangeStatusRequest request) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            Book updatedBook = bookStatusService.changeBookStatus(
                bookId,
                request.getNewStatus(),
                currentUser.getId(),
                request.getReason(),
                request.getNotes()
            );
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Bulk status change


    // Get status history for a book
    @GetMapping("/{bookId}/history")
    public ResponseEntity<List<BookStatusHistory>> getBookStatusHistory(@PathVariable String bookId) {
        try {
            List<BookStatusHistory> history = bookStatusService.getBookStatusHistory(bookId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get recent status changes
    @GetMapping("/recent-changes")
    public ResponseEntity<List<BookStatusHistory>> getRecentStatusChanges(@RequestParam(defaultValue = "30") int days) {
        try {
            List<BookStatusHistory> changes = bookStatusService.getRecentStatusChanges(days);
            return ResponseEntity.ok(changes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }













    // Get user notifications
    @GetMapping("/notifications")
    public ResponseEntity<List<BookStatusNotification>> getUserNotifications() {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            List<BookStatusNotification> notifications = bookStatusService.getUserNotifications(currentUser.getId());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get unread notifications
    @GetMapping("/notifications/unread")
    public ResponseEntity<List<BookStatusNotification>> getUserUnreadNotifications() {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            List<BookStatusNotification> notifications = bookStatusService.getUserUnreadNotifications(currentUser.getId());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Mark notification as read
    @PutMapping("/notifications/{notificationId}/read")
    public ResponseEntity<BookStatusNotification> markNotificationAsRead(@PathVariable String notificationId) {
        try {
            BookStatusNotification notification = bookStatusService.markNotificationAsRead(notificationId);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Mark all notifications as read
    @PutMapping("/notifications/mark-all-read")
    public ResponseEntity<Void> markAllNotificationsAsRead() {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            bookStatusService.markAllNotificationsAsRead(currentUser.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get unread notification count
    @GetMapping("/notifications/unread-count")
    public ResponseEntity<Long> getUnreadNotificationCount() {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            long count = bookStatusService.getUnreadNotificationCount(currentUser.getId());
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get notifications by type


    // Get recent notifications




    // Inner classes for request/response
    public static class ChangeStatusRequest {
        private Book.BookStatus newStatus;
        private String reason;
        private String notes;

        public Book.BookStatus getNewStatus() { return newStatus; }
        public void setNewStatus(Book.BookStatus newStatus) { this.newStatus = newStatus; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }


}
