package org.example.web.repository;

import org.example.web.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    // Search books excluding current user (for browse functionality)
    @Query("SELECT b FROM Book b WHERE " +
           "(:query IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:category IS NULL OR b.category = :category) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "b.owner.id != :excludeUserId")
    List<Book> searchBooksExcludingUser(@Param("query") String query,
                                       @Param("category") String category,
                                       @Param("author") String author,
                                       @Param("excludeUserId") String excludeUserId);

    @Query("SELECT b FROM Book b WHERE b.owner.id = :ownerId")
    List<Book> findByOwnerId(@Param("ownerId") String ownerId);

    @Query("SELECT b FROM Book b WHERE b.status = :status AND b.owner.id != :ownerId")
    List<Book> findByStatusAndOwnerIdNot(@Param("status") Book.BookStatus status, @Param("ownerId") String ownerId);

    List<Book> findByStatus(Book.BookStatus status);
    List<Book> findByCategoryIgnoreCase(String category);
    List<Book> findByAuthorIgnoreCase(String author);
    List<Book> findByPublicationYear(Integer publicationYear);
    List<Book> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Book> findTop10ByOrderByRatingDesc();
    List<Book> findTop10ByOrderByCreatedAtDesc();
    long countByStatus(Book.BookStatus status);

    // Advanced filtering queries
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:category IS NULL OR b.category = :category) AND " +
           "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR b.price <= :maxPrice) AND " +
           "(:minYear IS NULL OR b.publicationYear >= :minYear) AND " +
           "(:maxYear IS NULL OR b.publicationYear <= :maxYear) AND " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:ownerId IS NULL OR b.owner.id = :ownerId)")
    List<Book> findBooksWithAdvancedFilters(
        @Param("title") String title,
        @Param("author") String author,
        @Param("category") String category,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("minYear") Integer minYear,
        @Param("maxYear") Integer maxYear,
        @Param("status") String status,
        @Param("ownerId") String ownerId
    );

    // Find books by multiple categories
    @Query("SELECT b FROM Book b WHERE b.category IN :categories")
    List<Book> findByCategories(@Param("categories") List<String> categories);



    // Count books by category
    @Query("SELECT b.category, COUNT(b) FROM Book b GROUP BY b.category")
    List<Object[]> countBooksByCategory();



    // Count books by status
    @Query("SELECT b.status, COUNT(b) FROM Book b GROUP BY b.status")
    List<Object[]> countBooksByStatus();

    // Find popular authors
    @Query("SELECT b.author, COUNT(b) FROM Book b GROUP BY b.author ORDER BY COUNT(b) DESC")
    List<Object[]> findPopularAuthors();

    // Find books added this month
    @Query("SELECT b FROM Book b WHERE b.createdAt >= :startOfMonth AND b.createdAt <= :endOfMonth")
    List<Book> findBooksAddedThisMonth(@Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth);

    // Find books by rating range
    @Query("SELECT b FROM Book b WHERE b.rating BETWEEN :minRating AND :maxRating")
    List<Book> findByRatingRange(@Param("minRating") Double minRating, @Param("maxRating") Double maxRating);

    // Find recently added books
    @Query("SELECT b FROM Book b ORDER BY b.createdAt DESC")
    List<Book> findRecentlyAddedBooks();

    // Time period filtering queries for requirement #8
    @Query("SELECT b FROM Book b WHERE b.status = :status AND b.createdAt >= :startDate AND b.createdAt <= :endDate")
    List<Book> findByStatusAndTimePeriod(
        @Param("status") Book.BookStatus status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(b) FROM Book b WHERE b.status = :status AND b.createdAt >= :startDate AND b.createdAt <= :endDate")
    long countByStatusAndTimePeriod(
        @Param("status") Book.BookStatus status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT b FROM Book b WHERE b.owner.id = :ownerId AND b.status = :status AND b.createdAt >= :startDate AND b.createdAt <= :endDate")
    List<Book> findByOwnerAndStatusAndTimePeriod(
        @Param("ownerId") String ownerId,
        @Param("status") Book.BookStatus status,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT b FROM Book b WHERE b.status = 'BORROWED' AND b.createdAt >= :startDate AND b.createdAt <= :endDate")
    List<Book> findBorrowedBooksInTimePeriod(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT b FROM Book b WHERE b.status = 'SOLD' AND b.createdAt >= :startDate AND b.createdAt <= :endDate")
    List<Book> findSoldBooksInTimePeriod(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT b FROM Book b WHERE b.status = 'RESERVED' AND b.createdAt >= :startDate AND b.createdAt <= :endDate")
    List<Book> findReservedBooksInTimePeriod(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT b FROM Book b WHERE b.status = 'AVAILABLE' AND b.createdAt >= :startDate AND b.createdAt <= :endDate")
    List<Book> findAvailableBooksInTimePeriod(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Get books by time periods (This Month, This Year, Last 30 Days, etc.)
    @Query("SELECT b FROM Book b WHERE b.createdAt >= :startDate AND b.createdAt <= :endDate")
    List<Book> findBooksInTimePeriod(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Count books by status in time period
    @Query("SELECT b.status, COUNT(b) FROM Book b WHERE b.createdAt >= :startDate AND b.createdAt <= :endDate GROUP BY b.status")
    List<Object[]> countBooksByStatusInTimePeriod(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Advanced filtering methods
    List<Book> findByCategoryIn(List<String> categories);

    List<Book> findByPublicationYearLessThanEqual(Integer year);

    List<Book> findByPublicationYearGreaterThanEqual(Integer year);

    List<Book> findByPublicationYearBetween(Integer minYear, Integer maxYear);

    List<Book> findByRatingLessThanEqual(Double rating);

    List<Book> findByRatingGreaterThanEqual(Double rating);

    List<Book> findByRatingBetween(Double minRating, Double maxRating);

    // Time-based filtering methods
    List<Book> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate);

    List<Book> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime date);

    List<Book> findByStatusAndCreatedAtBetweenOrderByCreatedAtDesc(Book.BookStatus status, LocalDateTime startDate, LocalDateTime endDate);
}
