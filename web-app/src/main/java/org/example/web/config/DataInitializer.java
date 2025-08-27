package org.example.web.config;

import org.example.web.service.UserService;
import org.example.web.service.BookService;
import org.example.web.model.Book;
import org.example.web.model.User;
import org.example.web.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Initializing Books Exchange System...");

        // Create default users
        userService.initializeDefaultUsers();
        System.out.println("✅ Default users created");

        // Create sample books for each user
        createSampleBooks();
        System.out.println("✅ Sample books created");

        System.out.println("🎉 System initialization complete!");
        System.out.println("📝 Default users:");
        System.out.println("   Admin: username=admin, password=admin");
        System.out.println("   User 1: username=user, password=password (Alice Johnson)");
        System.out.println("   User 2: username=user2, password=password2 (Bob Wilson)");
        System.out.println("   User 3: username=user3, password=password3 (Carol Davis)");
        System.out.println("🔐 All passwords are securely hashed using BCrypt");
        System.out.println("📚 3 sample books added for each user");
    }

    private void createSampleBooks() {
        // Check if books already exist
        if (bookRepository.count() > 0) {
            System.out.println("📚 Books already exist, skipping sample book creation");
            return;
        }

        // Get users
        User alice = userService.getUserByUsername("user");
        User bob = userService.getUserByUsername("user2");
        User carol = userService.getUserByUsername("user3");

                // Create 3 sample books for Alice (user)
        createBookForUser(alice, "The Great Gatsby", "F. Scott Fitzgerald", "Fiction",
            "A classic American novel about the Jazz Age and the American Dream.", 1925, 15.99);

        createBookForUser(alice, "Pride and Prejudice", "Jane Austen", "Romance",
            "A witty romance novel about manners, marriage, and social expectations.", 1813, 12.99);

        createBookForUser(alice, "The Catcher in the Rye", "J.D. Salinger", "Fiction",
            "A coming-of-age story following Holden Caulfield in New York City.", 1951, 13.50);

        // Create 3 sample books for Bob (user2)
        createBookForUser(bob, "To Kill a Mockingbird", "Harper Lee", "Fiction",
            "A powerful story about racial injustice and the loss of innocence.", 1960, 12.50);

        createBookForUser(bob, "The Lord of the Rings", "J.R.R. Tolkien", "Fantasy",
            "An epic fantasy adventure about the quest to destroy the One Ring.", 1954, 25.99);

        createBookForUser(bob, "Dune", "Frank Herbert", "Science Fiction",
            "A science fiction epic set on the desert planet Arrakis.", 1965, 18.99);

        // Create 3 sample books for Carol (user3)
        createBookForUser(carol, "1984", "George Orwell", "Science Fiction",
            "A dystopian novel about totalitarianism and surveillance society.", 1949, 10.00);

        createBookForUser(carol, "Harry Potter and the Philosopher's Stone", "J.K. Rowling", "Fantasy",
            "The first book in the magical Harry Potter series.", 1997, 14.99);

        createBookForUser(carol, "The Hunger Games", "Suzanne Collins", "Young Adult",
            "A dystopian novel about survival in a televised death match.", 2008, 11.99);
    }

        private void createBookForUser(User owner, String title, String author, String category,
                                 String description, Integer year, Double price) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setDescription(description);
        book.setOwner(owner);
        book.setStatus(Book.BookStatus.AVAILABLE);
        book.setPublicationYear(year);
        book.setPrice(price);
        bookRepository.save(book);
    }
}
