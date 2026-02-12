package com.techelevator.service;

import com.techelevator.dao.CatDao;
import com.techelevator.dao.HireRequestDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Cat;
import com.techelevator.model.HireRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class HireRequestService {
    private final HireRequestDao hireRequestDao;
    private final CatDao catDao;

    public HireRequestService(HireRequestDao hireRequestDao, CatDao catDao) {
        this.hireRequestDao = hireRequestDao;
        this.catDao = catDao;
    }

    public List<HireRequest> getAll() {
        return hireRequestDao.getAll();
    }

    public HireRequest createRequest(HireRequest incoming, int userId) {
        if (incoming == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hire request body is required.");
        }
        if (incoming.getCatId() == null || incoming.getCatId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "catId is required.");
        }

        Cat cat;
        try {
            cat = catDao.getCatById(incoming.getCatId());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
        if (cat == null || !cat.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found.");
        }
        validateDates(incoming.getRequestedStartDate(), incoming.getRequestedEndDate());

        HireRequest toCreate = new HireRequest();
        toCreate.setCatId(incoming.getCatId());
        toCreate.setRequestedBy(userId);
        toCreate.setStatus("PENDING");
        toCreate.setRequestedStartDate(incoming.getRequestedStartDate());
        toCreate.setRequestedEndDate(incoming.getRequestedEndDate());

        try {
            return hireRequestDao.createHireRequest(toCreate);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public List<HireRequest> getRequestsForUser(int userId) {
        try {
            return hireRequestDao.getHireRequestsByUserId(userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    public void updateStatus(int hireRequestId, String status) {
    if (hireRequestId <= 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid hireRequestId.");
    }
    if (status == null || status.isBlank()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required.");
    }

    String normalized = status.trim().toUpperCase();

    if (!normalized.equals("PENDING") &&
        !normalized.equals("APPROVED") &&
        !normalized.equals("REJECTED") &&
        !normalized.equals("CANCELLED") &&
        !normalized.equals("COMPLETED")) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status.");
    }

    try {
        boolean updated = hireRequestDao.updateStatus(hireRequestId, normalized);
        if (!updated) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hire request not found.");
        }
    } catch (DaoException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
    }
}


    public void cancelRequest(int hireRequestId, int userId) {
        if (hireRequestId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid hireRequestId.");
        }

        try {
            boolean updated = hireRequestDao.cancelHireRequest(hireRequestId, userId);
            if (!updated) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hire request not found or cannot be cancelled.");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date.");
        }
    }
}
