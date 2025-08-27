package org.example.web.service;

import org.example.web.model.Book;
import org.example.web.model.User;
import org.example.web.model.Transaction;
import org.example.web.repository.BookRepository;
import org.example.web.repository.UserRepository;
import org.example.web.repository.TransactionRepository;
import org.example.web.dto.CreateBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

        public Book createBook(CreateBookRequest request) {
        // Require ownerId to be provided - no default user creation
        if (request.getOwnerId() == null) {
            throw new IllegalArgumentException("Owner ID is required to create a book");
        }

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getOwnerId()));

        Book book = new Book(
            request.getTitle(),
            request.getAuthor(),
            request.getCategory(),
            request.getDescription(),
            owner
        );

        // Set additional fields if provided
        if (request.getPublicationYear() != null) {
            book.setPublicationYear(request.getPublicationYear());
        }
        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }
        if (request.getStatus() != null) {
            book.setStatus(request.getStatus());
        }

        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(String id, CreateBookRequest request) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setCategory(request.getCategory());
            book.setDescription(request.getDescription());

            // Update additional fields if provided
            if (request.getPublicationYear() != null) {
                book.setPublicationYear(request.getPublicationYear());
            }
            if (request.getPrice() != null) {
                book.setPrice(request.getPrice());
            }
            if (request.getStatus() != null) {
                book.setStatus(request.getStatus());
            }

            return Optional.of(bookRepository.save(book));
        }
        return Optional.empty();
    }

    public boolean deleteBook(String id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Book changeBookStatus(String bookId, Book.BookStatus newStatus) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // Prevent status changes for sold books
        if (book.getStatus() == Book.BookStatus.SOLD) {
            throw new IllegalStateException("Cannot change status of a sold book");
        }

        // Allow setting to SOLD status
        if (newStatus == Book.BookStatus.SOLD) {
            book.setStatus(newStatus);
            return bookRepository.save(book);
        }

        // For other status changes, validate the transition
        switch (book.getStatus()) {
            case AVAILABLE:
                // Can change to BORROWED, RESERVED, or SOLD
                if (newStatus == Book.BookStatus.BORROWED ||
                    newStatus == Book.BookStatus.RESERVED ||
                    newStatus == Book.BookStatus.SOLD) {
                    book.setStatus(newStatus);
                    return bookRepository.save(book);
                }
                break;
            case BORROWED:
                // Can change to AVAILABLE, RESERVED, or SOLD
                if (newStatus == Book.BookStatus.AVAILABLE ||
                    newStatus == Book.BookStatus.RESERVED ||
                    newStatus == Book.BookStatus.SOLD) {
                    book.setStatus(newStatus);
                    return bookRepository.save(book);
                }
                break;
            case RESERVED:
                // Can change to AVAILABLE, BORROWED, or SOLD
                if (newStatus == Book.BookStatus.AVAILABLE ||
                    newStatus == Book.BookStatus.BORROWED ||
                    newStatus == Book.BookStatus.SOLD) {
                    book.setStatus(newStatus);
                    return bookRepository.save(book);
                }
                break;
        }

        throw new IllegalStateException("Invalid status transition from " + book.getStatus() + " to " + newStatus);
    }



    public List<Book> getBooksByOwner(String ownerId) {
        return bookRepository.findByOwnerId(ownerId);
    }

    public List<Book> getBooksByStatus(Book.BookStatus status) {
        return bookRepository.findByStatus(status);
    }



    public List<Book> getAllUserRelatedBooks(String userId) {
        try {
            // Get books owned by user
            List<Book> ownedBooks = bookRepository.findByOwnerId(userId);

            // Get transactions where user is the borrower (borrowed or reserved books)
            List<Transaction> userTransactions = transactionRepository.findByBorrowerIdAndStatus(userId, Transaction.TransactionStatus.ACTIVE);

            // Extract books from transactions (handle potential null books)
            List<Book> borrowedReservedBooks = userTransactions.stream()
                .map(Transaction::getBook)
                .filter(Objects::nonNull) // Filter out null books
                .collect(Collectors.toList());

            // Combine both lists and remove duplicates
            Set<String> seenBookIds = new HashSet<>();
            List<Book> allBooks = new ArrayList<>();

            // Add owned books
            for (Book book : ownedBooks) {
                if (book != null && book.getId() != null && seenBookIds.add(book.getId())) {
                    allBooks.add(book);
                }
            }

            // Add borrowed/reserved books
            for (Book book : borrowedReservedBooks) {
                if (book != null && book.getId() != null && seenBookIds.add(book.getId())) {
                    allBooks.add(book);
                }
            }

            return allBooks;
        } catch (Exception e) {
            // Log the error and return only owned books as fallback
            System.err.println("Error in getAllUserRelatedBooks: " + e.getMessage());
            e.printStackTrace();
            return bookRepository.findByOwnerId(userId);
        }
    }



    public List<Book> getAvailableBooksExcludingUser(String userId) {
        return bookRepository.findByStatusAndOwnerIdNot(Book.BookStatus.AVAILABLE, userId);
    }

    // Search books excluding current user (for browse functionality)
    public List<Book> searchBooksExcludingUser(String query, String category, String author, String excludeUserId) {
        return bookRepository.searchBooksExcludingUser(query, category, author, excludeUserId);
    }



























}
