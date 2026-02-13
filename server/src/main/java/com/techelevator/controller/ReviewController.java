package com.techelevator.controller;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Review;
import com.techelevator.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.techelevator.service.ReviewService;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cats")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserDao userDao;

    public ReviewController(ReviewService reviewService, UserDao userDao) {
        this.reviewService = reviewService;
        this.userDao = userDao;
    }

    @GetMapping("/{catId}/reviews")
    public List<Review> list(@PathVariable @Positive int catId) {
        return reviewService.getReviewsByCatId(catId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{catId}/reviews")
    public Review create(@PathVariable @Positive int catId,
                         @Valid @RequestBody Review review,
                         Principal principal) {

        int userId = getUserIdFromPrincipal(principal);
        review.setCatId(catId);
        review.setCreatedBy(userId);

        return reviewService.createReview(review);
    }

    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/{catId}/reviews/{reviewId}")
    public Review update(@PathVariable @Positive int catId,
                         @PathVariable @Positive int reviewId,
                         @Valid @RequestBody Review review) {

        review.setCatId(catId);
        return reviewService.updateReview(reviewId, review);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('STAFF')")
    @DeleteMapping("/{catId}/reviews/{reviewId}")
    public void delete(@PathVariable @Positive int catId,
                       @PathVariable @Positive int reviewId) {

        reviewService.deleteReview(reviewId);
    }

    private int getUserIdFromPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        User user = userDao.getUserByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return user.getId();
    }
}
