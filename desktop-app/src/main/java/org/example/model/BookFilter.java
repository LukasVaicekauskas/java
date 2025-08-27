package org.example.model;

import java.util.List;

public class BookFilter {
    private String title;
    private String author;
    private String category;

    private Double minPrice;
    private Double maxPrice;
    private Integer minPublicationYear;
    private Integer maxPublicationYear;
    private String status;
    private String ownerId;
    private List<String> categories;

    private Boolean availableOnly;
    private String sortBy;
    private String sortOrder;
    private Integer page;
    private Integer size;

    // Constructors
    public BookFilter() {}

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }



    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }

    public Integer getMinPublicationYear() { return minPublicationYear; }
    public void setMinPublicationYear(Integer minPublicationYear) { this.minPublicationYear = minPublicationYear; }

    public Integer getMaxPublicationYear() { return maxPublicationYear; }
    public void setMaxPublicationYear(Integer maxPublicationYear) { this.maxPublicationYear = maxPublicationYear; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }



    public Boolean getAvailableOnly() { return availableOnly; }
    public void setAvailableOnly(Boolean availableOnly) { this.availableOnly = availableOnly; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    // Helper methods
    public boolean hasFilters() {
        return title != null || author != null || category != null ||
               minPrice != null || maxPrice != null || minPublicationYear != null || maxPublicationYear != null ||
               status != null || ownerId != null || (categories != null && !categories.isEmpty()) ||
               availableOnly != null;
    }

    public boolean hasPriceFilter() {
        return minPrice != null || maxPrice != null;
    }

    public boolean hasYearFilter() {
        return minPublicationYear != null || maxPublicationYear != null;
    }

    public boolean hasMultipleCategories() {
        return categories != null && categories.size() > 1;
    }


}
