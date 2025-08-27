package org.example.web.repository;

import org.example.web.model.BookStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookStatusHistoryRepository extends JpaRepository<BookStatusHistory, String> {

    // Find all status changes for a specific book
    @Query("SELECT h FROM BookStatusHistory h WHERE h.book.id = :bookId ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findByBookIdOrderByChangedAtDesc(@Param("bookId") String bookId);

    // Find recent status changes (last 30 days)
    @Query("SELECT h FROM BookStatusHistory h WHERE h.changedAt >= :since ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findRecentStatusChanges(@Param("since") LocalDateTime since);

    // Find status changes by user
    @Query("SELECT h FROM BookStatusHistory h WHERE h.changedByUser.id = :userId ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findByChangedByUserIdOrderByChangedAtDesc(@Param("userId") String userId);

    // Find status changes for a specific status
    @Query("SELECT h FROM BookStatusHistory h WHERE h.newStatus = :status ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findByNewStatusOrderByChangedAtDesc(@Param("status") String status);

    // Find status changes from one status to another
    @Query("SELECT h FROM BookStatusHistory h WHERE h.oldStatus = :oldStatus AND h.newStatus = :newStatus ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findByOldStatusAndNewStatusOrderByChangedAtDesc(
        @Param("oldStatus") String oldStatus,
        @Param("newStatus") String newStatus
    );

    // Find status changes with reason
    @Query("SELECT h FROM BookStatusHistory h WHERE h.reason IS NOT NULL AND h.reason != '' ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findStatusChangesWithReasonOrderByChangedAtDesc();

    // Count status changes for a book
    @Query("SELECT COUNT(h) FROM BookStatusHistory h WHERE h.book.id = :bookId")
    long countByBookId(@Param("bookId") String bookId);

    // Count status changes by user
    @Query("SELECT COUNT(h) FROM BookStatusHistory h WHERE h.changedByUser.id = :userId")
    long countByChangedByUserId(@Param("userId") String userId);

    // Find status changes in date range
    @Query("SELECT h FROM BookStatusHistory h WHERE h.changedAt BETWEEN :startDate AND :endDate ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findByChangedAtBetweenOrderByChangedAtDesc(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Find status upgrades (improvements)
    @Query("SELECT h FROM BookStatusHistory h WHERE " +
           "CASE h.oldStatus " +
           "  WHEN 'LOST' THEN 5 " +
           "  WHEN 'DAMAGED' THEN 6 " +
           "  WHEN 'MAINTENANCE' THEN 4 " +
           "  WHEN 'BORROWED' THEN 3 " +
           "  WHEN 'RESERVED' THEN 2 " +
           "  WHEN 'AVAILABLE' THEN 1 " +
           "  ELSE 7 " +
           "END > " +
           "CASE h.newStatus " +
           "  WHEN 'LOST' THEN 5 " +
           "  WHEN 'DAMAGED' THEN 6 " +
           "  WHEN 'MAINTENANCE' THEN 4 " +
           "  WHEN 'BORROWED' THEN 3 " +
           "  WHEN 'RESERVED' THEN 2 " +
           "  WHEN 'AVAILABLE' THEN 1 " +
           "  ELSE 7 " +
           "END " +
           "ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findStatusUpgradesOrderByChangedAtDesc();

    // Find status downgrades (worsening)
    @Query("SELECT h FROM BookStatusHistory h WHERE " +
           "CASE h.oldStatus " +
           "  WHEN 'LOST' THEN 5 " +
           "  WHEN 'DAMAGED' THEN 6 " +
           "  WHEN 'MAINTENANCE' THEN 4 " +
           "  WHEN 'BORROWED' THEN 3 " +
           "  WHEN 'RESERVED' THEN 2 " +
           "  WHEN 'AVAILABLE' THEN 1 " +
           "  ELSE 7 " +
           "END < " +
           "CASE h.newStatus " +
           "  WHEN 'LOST' THEN 5 " +
           "  WHEN 'DAMAGED' THEN 6 " +
           "  WHEN 'MAINTENANCE' THEN 4 " +
           "  WHEN 'BORROWED' THEN 3 " +
           "  WHEN 'RESERVED' THEN 2 " +
           "  WHEN 'AVAILABLE' THEN 1 " +
           "  ELSE 7 " +
           "END " +
           "ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findStatusDowngradesOrderByChangedAtDesc();

    // Find the latest status change for each book
    @Query("SELECT h FROM BookStatusHistory h WHERE h.id IN " +
           "(SELECT MAX(h2.id) FROM BookStatusHistory h2 GROUP BY h2.book.id) " +
           "ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findLatestStatusChangeForEachBook();

    // Find status changes for books owned by a specific user
    @Query("SELECT h FROM BookStatusHistory h WHERE h.book.owner.id = :ownerId ORDER BY h.changedAt DESC")
    List<BookStatusHistory> findByBookOwnerIdOrderByChangedAtDesc(@Param("ownerId") String ownerId);
}
