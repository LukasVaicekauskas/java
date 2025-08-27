package org.example.web.dto;

import java.util.List;

public class BookFilterRequest {
    private String query;
    private List<String> categories;

    private List<String> statuses;
    private Integer minYear;
    private Integer maxYear;
    private Double minPrice;
    private Double maxPrice;
    private Double minRating;
    private Double maxRating;
    private String author;
    private String ownerId;

    // Default constructor
    public BookFilterRequest() {}

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }



    public List<String> getStatuses() { return statuses; }
    public void setStatuses(List<String> statuses) { this.statuses = statuses; }

    public Integer getMinYear() { return minYear; }
    public void setMinYear(Integer minYear) { this.minYear = minYear; }

    public Integer getMaxYear() { return maxYear; }
    public void setMaxYear(Integer maxYear) { this.maxYear = maxYear; }

    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }

    public Double getMinRating() { return minRating; }
    public void setMinRating(Double minRating) { this.minRating = minRating; }

    public Double getMaxRating() { return maxRating; }
    public void setMaxRating(Double maxRating) { this.maxRating = maxRating; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
}
