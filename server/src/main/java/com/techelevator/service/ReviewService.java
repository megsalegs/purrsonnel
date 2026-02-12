package com.techelevator.service;

import com.techelevator.dao.ReviewDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Review;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewDao reviewDao;

    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    public List<Review> getReviewsByCatId(int catId) {
        try {
            return reviewDao.getReviewsByCatId(catId);
        } catch (DaoException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "DAO error - " + e.getMessage()
            );
        }
    }

    public Review createReview(Review review) {
        try {
            return reviewDao.createReview(review);
        } catch (DaoException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Failed to create review - " + e.getMessage()
            );
        }
    }

    public Review updateReview(int reviewId, Review review) {
        Review existing = reviewDao.getReviewById(reviewId);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }

        review.setReviewId(reviewId);

        try {
            Review updated = reviewDao.updateReview(review);
            if (updated == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
            }
            return updated;
        } catch (DaoException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Failed to update review - " + e.getMessage()
            );
        }
    }

    public void deleteReview(int reviewId) {
        Review existing = reviewDao.getReviewById(reviewId);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }

        try {
            reviewDao.deleteReview(reviewId);
        } catch (DaoException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to delete review - " + e.getMessage()
            );
        }
    }
}
