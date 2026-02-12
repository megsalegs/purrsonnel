package com.techelevator.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Cat {


    private Integer catId;
    private Integer createdBy;
    @NotBlank(message = "The name field must not be blank.")
    @Size(max = 50)
    private String name;
    @Min(value = 0, message = "The age field must be 0 or greater.")
    private Integer age;
    @Size(max = 50)
    private String color;
    @Size(max = 20)
    private String fur_length;
    @Size(max = 500)
    private String description;
    @Size(max = 500)
    private String skills;
    private boolean featured;
    private Integer rankScore;
    private boolean active;
    private Integer removedBy;
    private LocalDateTime removedAt;
    private List<Review> reviews = new ArrayList<>();
    private String primaryImagePath;


    public Cat(Integer catId,
               Integer createdBy,
               String name,
               Integer age,
               String color,
               String fur_length,
               String description,
               String skills,
               boolean featured,
               Integer rankScore,
               boolean active,
               String primaryImagePath) {
        this.catId = catId;
        this.createdBy = createdBy;
        this.name = name;
        this.age = age;
        this.color = color;
        this.fur_length = fur_length;
        this.description = description;
        this.skills = skills;
        this.featured = featured;
        this.rankScore = rankScore;
        this.active = active;
        this.primaryImagePath = primaryImagePath;
    }

    public Cat() {

    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }
    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFur_length() {
        return fur_length;
    }

    public void setFur_Length(String fur_length) {
        this.fur_length = fur_length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public boolean getFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }
    public Integer getRankScore() {
        return rankScore;
    }

    public void setRankScore(Integer rankScore) {
        this.rankScore = rankScore;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getRemovedBy() {
        return removedBy;
    }
    public void setRemovedBy(Integer removedBy) {
        this.removedBy = removedBy;
    }

    public LocalDateTime getRemovedAt() {
        return removedAt;
    }
    public void setRemovedAt(LocalDateTime removedAt) {
        this.removedAt = removedAt;
    }


    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = (reviews == null) ? new ArrayList<>() : reviews;
    }



    public String getPrimaryImagePath() {
        return primaryImagePath;
    }

    public void setPrimaryImagePath(String primaryImagePath) {
        this.primaryImagePath = primaryImagePath;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "catId=" + catId +
                ", createdBy=" + createdBy +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                ", furLength='" + fur_length + '\'' +
                ", featured=" + featured +
                ", rankScore=" + rankScore +
                ", active=" + active +
                '}';
    }
}
