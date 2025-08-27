package org.example.web.repository;

import org.example.web.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    @Query("SELECT m FROM Message m WHERE m.book.id = :bookId ORDER BY m.createdAt ASC")
    List<Message> findByBookIdOrderByCreatedAtAsc(@Param("bookId") String bookId);

    @Query("SELECT m FROM Message m WHERE (m.sender.id = :senderId OR m.book.owner.id = :bookOwnerId) ORDER BY m.createdAt DESC")
    List<Message> findBySenderIdOrBookOwnerIdOrderByCreatedAtDesc(@Param("senderId") String senderId, @Param("bookOwnerId") String bookOwnerId);

    @Query("SELECT m FROM Message m WHERE m.sender.id = :senderId ORDER BY m.createdAt DESC")
    List<Message> findBySenderIdOrderByCreatedAtDesc(@Param("senderId") String senderId);

    @Query("SELECT m FROM Message m WHERE m.book.owner.id = :bookOwnerId AND m.sender.id != :senderId ORDER BY m.createdAt DESC")
    List<Message> findByBookOwnerIdAndSenderIdNotOrderByCreatedAtDesc(@Param("bookOwnerId") String bookOwnerId, @Param("senderId") String senderId);

    @Query("SELECT m FROM Message m WHERE m.book.owner.id = :bookOwnerId AND m.sender.id != :senderId AND m.isRead = false ORDER BY m.createdAt DESC")
    List<Message> findByBookOwnerIdAndSenderIdNotAndReadFalseOrderByCreatedAtDesc(@Param("bookOwnerId") String bookOwnerId, @Param("senderId") String senderId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.book.owner.id = :bookOwnerId AND m.sender.id != :senderId AND m.isRead = false")
    int countByBookOwnerIdAndSenderIdNotAndReadFalse(@Param("bookOwnerId") String bookOwnerId, @Param("senderId") String senderId);

    // Find messages by book ID and depth
    @Query("SELECT m FROM Message m WHERE m.book.id = :bookId AND m.depth = :depth ORDER BY m.createdAt ASC")
    List<Message> findByBookIdAndDepthOrderByCreatedAtAsc(@Param("bookId") String bookId, @Param("depth") int depth);

    // New methods for the updated messaging system
    @Query("SELECT m FROM Message m WHERE m.recipient.id = :recipientId ORDER BY m.createdAt DESC")
    List<Message> findByRecipientIdAndDeletedByRecipientFalseOrderByCreatedAtDesc(@Param("recipientId") String recipientId);

    @Query("SELECT m FROM Message m WHERE m.sender.id = :senderId ORDER BY m.createdAt DESC")
    List<Message> findBySenderIdAndDeletedBySenderFalseOrderByCreatedAtDesc(@Param("senderId") String senderId);

    @Query("SELECT m FROM Message m WHERE ((m.sender.id = :userId1 AND m.recipient.id = :userId2) OR (m.sender.id = :userId2 AND m.recipient.id = :userId1)) ORDER BY m.createdAt ASC")
    List<Message> findConversationBetweenUsers(@Param("userId1") String userId1, @Param("userId2") String userId2);

    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId OR m.recipient.id = :userId) AND (LOWER(m.content) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.subject) LIKE LOWER(CONCAT('%', :query, '%'))) ORDER BY m.createdAt DESC")
    List<Message> searchMessagesByUserAndContent(@Param("userId") String userId, @Param("query") String query);

    // Find messages for a book where user is involved (sender or recipient)
    @Query("SELECT m FROM Message m WHERE m.book.id = :bookId AND (m.sender.id = :userId OR m.recipient.id = :userId) ORDER BY m.createdAt ASC")
    List<Message> findByBookIdAndUserInvolvedOrderByCreatedAtAsc(@Param("bookId") String bookId, @Param("userId") String userId);

    // Find messages for users involved in conversations
    @Query("SELECT DISTINCT m FROM Message m WHERE m.book.id IN " +
           "(SELECT DISTINCT m2.book.id FROM Message m2 WHERE m2.sender.id = :userId OR m2.recipient.id = :userId) " +
           "AND (m.sender.id = :userId OR m.recipient.id = :userId) " +
           "ORDER BY m.createdAt DESC")
    List<Message> findMessagesForUserInConversations(@Param("userId") String userId);

    @Query("SELECT m FROM Message m WHERE m.book.id IN " +
           "(SELECT DISTINCT m2.book.id FROM Message m2 WHERE " +
           "(m2.sender.id = :userId1 OR m2.recipient.id = :userId1 OR m2.sender.id = :userId2 OR m2.recipient.id = :userId2)) " +
           "AND (m.sender.id = :userId1 OR m.sender.id = :userId2 OR m.recipient.id = :userId1 OR m.recipient.id = :userId2) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findConversationBetweenUsersInSameBook(@Param("userId1") String userId1, @Param("userId2") String userId2);
}
