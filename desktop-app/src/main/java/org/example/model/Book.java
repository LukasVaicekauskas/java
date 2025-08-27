package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private String description;
    private BookStatus status;
    private String ownerId;
    private String ownerName;
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    private double rating;
    private int reviewCount;

    private Integer publicationYear;
    private Double price;

    public enum BookStatus {
        AVAILABLE,
        BORROWED,
        RESERVED,
        SOLD
    }



    // Default constructor for JSON deserialization
    public Book() {
        this.createdAt = LocalDateTime.now();
        this.rating = 0.0;
        this.reviewCount = 0;
        this.status = BookStatus.AVAILABLE;
        this.price = 0.0;
    }

    public Book(String id, String title, String author, String category, String description,
                BookStatus status, String ownerId, String ownerName) {
        this();
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.status = status;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public BookStatus getStatus() { return status; }
    public String getOwnerId() { return ownerId; }
    public String getOwnerName() { return ownerName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }

    public Integer getPublicationYear() { return publicationYear; }
    public Double getPrice() { return price; }

    // Setters for JSON deserialization
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(BookStatus status) { this.status = status; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setRating(double rating) { this.rating = rating; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    public void setPrice(Double price) { this.price = price; }

    @Override
    public String toString() {
        return title + " by " + author + " (" + status + ")";
    }
}
