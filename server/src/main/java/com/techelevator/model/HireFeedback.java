package com.techelevator.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class HireFeedback {
    private Integer feedbackId;
    private Integer hireRequestId;
    private Integer createdBy;
    private Integer rating;
    @NotBlank(message = "Feedback body is required.")
    @Size(max = 500)
    private String feedbackBody;
    private LocalDateTime createdAt;

    public HireFeedback() { }

    public HireFeedback(Integer feedbackId, Integer hireRequestId, Integer createdBy,
                        Integer rating, String feedbackBody, LocalDateTime createdAt) {
        this.feedbackId = feedbackId;
        this.hireRequestId = hireRequestId;
        this.createdBy = createdBy;
        this.rating = rating;
        this.feedbackBody = feedbackBody;
        this.createdAt = createdAt;
    }

    public Integer getFeedbackId() {
        return feedbackId;
    }
    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Integer getHireRequestId() {
        return hireRequestId;
    }
    public void setHireRequestId(Integer hireRequestId) {
        this.hireRequestId = hireRequestId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getFeedbackBody() {
        return feedbackBody;
    }
    public void setFeedbackBody(String feedbackBody) {
        this.feedbackBody = feedbackBody;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "HireFeedback{" +
                "feedbackId=" + feedbackId +
                ", hireRequestId=" + hireRequestId +
                ", createdBy=" + createdBy +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                '}';
    }
}
