package com.techelevator.controller;

import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.HireFeedback;
import com.techelevator.model.User;
import com.techelevator.service.HireFeedbackService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/hire-requests")
public class HireFeedbackController {

    private final HireFeedbackService hireFeedbackService;
    private final UserDao userDao;

    public HireFeedbackController(HireFeedbackService hireFeedbackService, UserDao userDao) {
        this.hireFeedbackService = hireFeedbackService;
        this.userDao = userDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{hireRequestId}/feedback")
    public HireFeedback createFeedback(@PathVariable @Positive int hireRequestId,
                                       @Valid @RequestBody HireFeedback feedback,
                                       Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        feedback.setHireRequestId(hireRequestId);
        feedback.setCreatedBy(userId);
        return hireFeedbackService.createFeedback(hireRequestId, feedback, userId);
    }

    @GetMapping("/{hireRequestId}/feedback")
    public HireFeedback getFeedback(@PathVariable @Positive int hireRequestId) {
        HireFeedback feedback =
        hireFeedbackService.getFeedbackForRequest(hireRequestId);
        if (feedback == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found.");
        }
        return feedback;
    }

    private int getUserIdFromPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        try {
            User user = userDao.getUserByUsername(principal.getName());
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            return user.getId();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }
}
