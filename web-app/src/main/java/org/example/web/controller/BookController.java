package org.example.web.controller;

import org.example.web.model.Book;
import org.example.web.model.User;
import org.example.web.service.BookService;
import org.example.web.dto.CreateBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String author) {
        try {
            // Always exclude current user's books for browse/search functionality
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();
            List<Book> books = bookService.searchBooksExcludingUser(query, category, author, currentUser.getId());
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody CreateBookRequest request) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            // Set the owner to the current authenticated user
            request.setOwnerId(currentUser.getId());

            Book newBook = bookService.createBook(request);
            return ResponseEntity.ok(newBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable String id, @Valid @RequestBody CreateBookRequest request) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            // Check if user owns the book
            Optional<Book> existingBook = bookService.getBookById(id);
            if (existingBook.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Book book = existingBook.get();
            if (!book.getOwner().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(403).build();
            }

            Optional<Book> updatedBook = bookService.updateBook(id, request);
            return updatedBook.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            // Check if user owns the book
            Optional<Book> existingBook = bookService.getBookById(id);
            if (existingBook.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Book book = existingBook.get();
            if (!book.getOwner().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(403).build();
            }

            boolean deleted = bookService.deleteBook(id);
            return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }





    @GetMapping("/user/{userId}/all-related")
    public ResponseEntity<List<Book>> getAllUserRelatedBooks(@PathVariable String userId) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            // Users can only view their own related books
            if (!currentUser.getId().equals(userId)) {
                return ResponseEntity.status(403).build();
            }

            List<Book> books = bookService.getAllUserRelatedBooks(userId);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }



    @GetMapping("/available/exclude-current-user")
    public ResponseEntity<List<Book>> getAvailableBooksExcludingCurrentUser() {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            List<Book> books = bookService.getAvailableBooksExcludingUser(currentUser.getId());
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }





    @GetMapping("/status/{status}")
    public ResponseEntity<List<Book>> getBooksByStatus(@PathVariable String status) {
        List<Book> books = bookService.getBooksByStatus(Book.BookStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(books);
    }






















    @PutMapping("/{id}/status")
    public ResponseEntity<Book> changeBookStatus(@PathVariable String id, @RequestBody StatusChangeRequest request) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            // Check if user owns the book
            Optional<Book> existingBook = bookService.getBookById(id);
            if (existingBook.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Book book = existingBook.get();
            if (!book.getOwner().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(403).build();
            }

            Book.BookStatus newStatus = Book.BookStatus.valueOf(request.getStatus().toUpperCase());
            Book updatedBook = bookService.changeBookStatus(id, newStatus);
            return ResponseEntity.ok(updatedBook);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Inner class for rating requests
    public static class RatingRequest {
        private double rating;
        private String review;

        public double getRating() { return rating; }
        public void setRating(double rating) { this.rating = rating; }
        public String getReview() { return review; }
        public void setReview(String review) { this.review = review; }
    }

    public static class StatusChangeRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
