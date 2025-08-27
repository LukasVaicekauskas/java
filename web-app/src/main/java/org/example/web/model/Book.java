package org.example.web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String category;

    private String description;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    private LocalDateTime createdAt;

    private double rating;

    private int reviewCount;

    private String review;



    private Integer publicationYear;

    private Double price;

    public enum BookStatus {
        AVAILABLE,
        BORROWED,
        RESERVED,
        SOLD,
        MAINTENANCE,
        LOST,
        DAMAGED
    }



    // Default constructor
    public Book() {
        this.createdAt = LocalDateTime.now();
        this.rating = 0.0;
        this.reviewCount = 0;
        this.status = BookStatus.AVAILABLE;
        this.price = 0.0;
    }

    public Book(String title, String author, String category, String description, User owner) {
        this();
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.owner = owner;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public String getOwnerId() {
        return owner != null ? owner.getId() : null;
    }

    public String getOwnerName() {
        return owner != null ? owner.getFullName() : null;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }



    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    @Override
    public String toString() {
        return title + " by " + author + " (" + status + ")";
    }
}
