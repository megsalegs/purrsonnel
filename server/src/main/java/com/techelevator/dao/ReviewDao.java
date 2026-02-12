package com.techelevator.dao;

import com.techelevator.model.Review;

import java.util.List;

public interface ReviewDao {
    List<Review> getReviewsByCatId(int catId);

    Review getReviewById(int id);
    Review createReview(Review review);
    Review updateReview(Review review);
    void deleteReview(int reviewId);

}
