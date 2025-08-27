package org.example.web.repository;

import org.example.web.model.Transaction;
import org.example.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("SELECT t FROM Transaction t WHERE t.book = :book AND t.status = :status")
    List<Transaction> findByBookAndStatus(@Param("book") org.example.web.model.Book book,
                                        @Param("status") Transaction.TransactionStatus status);

    @Query("SELECT t FROM Transaction t WHERE t.borrower = :user OR t.lender = :user")
    List<Transaction> findByBorrowerOrLender(@Param("user") User user);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.book WHERE t.borrower.id = :userId AND t.status = :status")
    List<Transaction> findByBorrowerIdAndStatus(@Param("userId") String userId, @Param("status") Transaction.TransactionStatus status);

    List<Transaction> findByStatus(Transaction.TransactionStatus status);

    @Query("SELECT t FROM Transaction t WHERE t.book.id = :bookId")
    List<Transaction> findByBookId(@Param("bookId") String bookId);
    long countByStatus(Transaction.TransactionStatus status);
    long countByType(Transaction.TransactionType type);
}
