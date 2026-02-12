package com.techelevator.model;

import java.time.LocalDateTime;

public class Bookmark {

    private Integer userId;
    private Integer catId;
    private LocalDateTime createdAt;

    public Bookmark() { }

    public Bookmark(Integer userId, Integer catId) {
        this.userId = userId;
        this.catId = catId;
    }

    public Bookmark(Integer userId, Integer catId, LocalDateTime createdAt) {
        this.userId = userId;
        this.catId = catId;
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "userId=" + userId +
                ", catId=" + catId +
                ", createdAt=" + createdAt +
                '}';
    }
}
