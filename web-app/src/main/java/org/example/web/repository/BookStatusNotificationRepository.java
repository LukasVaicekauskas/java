package org.example.web.repository;

import org.example.web.model.BookStatusNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookStatusNotificationRepository extends JpaRepository<BookStatusNotification, String> {

    // Find all notifications for a specific user
    @Query("SELECT n FROM BookStatusNotification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findByUserIdOrderByCreatedAtDesc(@Param("userId") String userId);

    // Find unread notifications for a user
    @Query("SELECT n FROM BookStatusNotification n WHERE n.user.id = :userId AND n.isRead = false ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findUnreadByUserIdOrderByCreatedAtDesc(@Param("userId") String userId);

    // Find notifications for a specific book
    @Query("SELECT n FROM BookStatusNotification n WHERE n.book.id = :bookId ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findByBookIdOrderByCreatedAtDesc(@Param("bookId") String bookId);

    // Find notifications by type
    @Query("SELECT n FROM BookStatusNotification n WHERE n.type = :type ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findByTypeOrderByCreatedAtDesc(@Param("type") BookStatusNotification.NotificationType type);

    // Find notifications by type for a specific user
    @Query("SELECT n FROM BookStatusNotification n WHERE n.user.id = :userId AND n.type = :type ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findByUserIdAndTypeOrderByCreatedAtDesc(
        @Param("userId") String userId, 
        @Param("type") BookStatusNotification.NotificationType type
    );

    // Find recent notifications (last 7 days)
    @Query("SELECT n FROM BookStatusNotification n WHERE n.createdAt >= :since ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findRecentNotifications(@Param("since") LocalDateTime since);

    // Find recent notifications for a user
    @Query("SELECT n FROM BookStatusNotification n WHERE n.user.id = :userId AND n.createdAt >= :since ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findRecentNotificationsByUserId(
        @Param("userId") String userId, 
        @Param("since") LocalDateTime since
    );

    // Count unread notifications for a user
    @Query("SELECT COUNT(n) FROM BookStatusNotification n WHERE n.user.id = :userId AND n.isRead = false")
    long countUnreadByUserId(@Param("userId") String userId);

    // Count notifications by type
    @Query("SELECT COUNT(n) FROM BookStatusNotification n WHERE n.type = :type")
    long countByType(@Param("type") BookStatusNotification.NotificationType type);

    // Count notifications by type for a user
    @Query("SELECT COUNT(n) FROM BookStatusNotification n WHERE n.user.id = :userId AND n.type = :type")
    long countByUserIdAndType(@Param("userId") String userId, @Param("type") BookStatusNotification.NotificationType type);

    // Find notifications that haven't been sent via email
    @Query("SELECT n FROM BookStatusNotification n WHERE n.isEmailSent = false ORDER BY n.createdAt ASC")
    List<BookStatusNotification> findUnsentEmailNotifications();

    // Find notifications in date range
    @Query("SELECT n FROM BookStatusNotification n WHERE n.createdAt BETWEEN :startDate AND :endDate ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findByCreatedAtBetweenOrderByCreatedAtDesc(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );

    // Find notifications in date range for a user
    @Query("SELECT n FROM BookStatusNotification n WHERE n.user.id = :userId AND n.createdAt BETWEEN :startDate AND :endDate ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
        @Param("userId") String userId,
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );

    // Find notifications for books owned by a specific user
    @Query("SELECT n FROM BookStatusNotification n WHERE n.book.owner.id = :ownerId ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findByBookOwnerIdOrderByCreatedAtDesc(@Param("ownerId") String ownerId);

    // Find overdue notifications
    @Query("SELECT n FROM BookStatusNotification n WHERE n.type = 'OVERDUE_NOTICE' ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findOverdueNotifications();

    // Find due date reminder notifications
    @Query("SELECT n FROM BookStatusNotification n WHERE n.type = 'DUE_DATE_REMINDER' ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findDueDateReminderNotifications();

    // Find status change notifications
    @Query("SELECT n FROM BookStatusNotification n WHERE n.type = 'STATUS_CHANGE' ORDER BY n.createdAt DESC")
    List<BookStatusNotification> findStatusChangeNotifications();

    // Find notifications that need email sending (created more than 1 hour ago but not sent)
    @Query("SELECT n FROM BookStatusNotification n WHERE n.isEmailSent = false AND n.createdAt < :cutoff ORDER BY n.createdAt ASC")
    List<BookStatusNotification> findPendingEmailNotifications(@Param("cutoff") LocalDateTime cutoff);

    // Mark all notifications as read for a user
    @Query("UPDATE BookStatusNotification n SET n.isRead = true, n.readAt = :readAt WHERE n.user.id = :userId AND n.isRead = false")
    void markAllAsReadByUserId(@Param("userId") String userId, @Param("readAt") LocalDateTime readAt);

    // Delete old notifications (older than specified date)
    @Query("DELETE FROM BookStatusNotification n WHERE n.createdAt < :cutoff")
    void deleteOldNotifications(@Param("cutoff") LocalDateTime cutoff);
}
