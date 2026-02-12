package com.techelevator.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class Review {
    private int reviewId;
    private int catId;
    private int createdBy;
    @Size(max = 500, message = "The reviewBody must be 500 characters or less")
    private String reviewBody;
    @Min(value = 1, message = "Rating must be between 1 and 5.")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    public Review() {}

    public Review(int reviewId, int catId, int createdBy, String reviewBody, Integer rating) {
        this.reviewId = reviewId;
        this.catId = catId;
        this.createdBy = createdBy;
        this.reviewBody = reviewBody;
        this.rating = rating;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", catId=" + catId +
                ", createdBy=" + createdBy +
                ", reviewBody='" + reviewBody + '\'' +
                ", rating=" + rating +
                '}';
    }
}
