package com.techelevator.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CatImage {

    private Integer imageId;

    private  Integer catId;
    private Integer  uploadedBy;
    @NotBlank(message = "filePath is required.")
    @Size(max = 500)
    private String filePath;
    private boolean primary;
    private LocalDateTime createdAt;

    public CatImage() { }

    public CatImage(Integer imageId, Integer catId, Integer uploadedBy, String filePath, boolean primary, LocalDateTime createdAt) {
        this.imageId = imageId;
        this.catId = catId;
        this.uploadedBy = uploadedBy;
        this.filePath = filePath;
        this.primary = primary;
        this.createdAt = createdAt;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Integer uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CatImage{" +
                "imageId=" + imageId +
                ", catId=" + catId +
                ", uploadedBy=" + uploadedBy +
                ", filePath='" + filePath + '\'' +
                ", primary=" + primary +
                ", createdAt=" + createdAt +
                '}';
    }
}
