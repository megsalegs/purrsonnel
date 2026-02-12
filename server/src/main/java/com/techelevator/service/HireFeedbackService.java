package com.techelevator.service;

import com.techelevator.dao.HireFeedbackDao;
import com.techelevator.dao.HireRequestDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.HireFeedback;
import com.techelevator.model.HireRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HireFeedbackService {
    private final HireFeedbackDao hireFeedbackDao;
    private final HireRequestDao hireRequestDao;

    public HireFeedbackService(HireFeedbackDao hireFeedbackDao, HireRequestDao hireRequestDao) {
        this.hireFeedbackDao = hireFeedbackDao;
        this.hireRequestDao = hireRequestDao;
    }

    public HireFeedback createFeedback(int hireRequestId, HireFeedback incoming, int userId) {
        if (hireRequestId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid hireRequestId.");
        }
        if (incoming == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feedback body is required.");
        }

        HireRequest request;
        try {
            request = hireRequestDao.getHireRequestById(hireRequestId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hire request not found.");
        }
        if (request.getRequestedBy() == null || request.getRequestedBy() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only leave feedback on your own hire requests.");
        }
        if (request.getStatus() == null || !request.getStatus().equalsIgnoreCase("COMPLETED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feedback can only be left for COMPLETED hire requests.");
        }

        try {
            HireFeedback existing = hireFeedbackDao.getFeedbackByHireRequestId(hireRequestId);
            if (existing != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feedback already exists for this hire request.");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }

        HireFeedback toCreate = new HireFeedback();
        toCreate.setHireRequestId(hireRequestId);
        toCreate.setCreatedBy(userId);
        toCreate.setRating(incoming.getRating());
        toCreate.setFeedbackBody(incoming.getFeedbackBody());

        try {
            return hireFeedbackDao.createFeedbackForRequest(toCreate);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public HireFeedback getFeedbackForRequest(int hireRequestId) {
        if (hireRequestId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid hireRequestId.");
        }

        try {
            HireFeedback feedback = hireFeedbackDao.getFeedbackByHireRequestId(hireRequestId);
            if (feedback == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return feedback;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }
}
