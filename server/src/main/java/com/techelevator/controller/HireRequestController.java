package com.techelevator.controller;

import com.techelevator.dao.HireRequestDao;
import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.HireRequest;
import com.techelevator.model.User;
import com.techelevator.service.HireRequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/hire-requests")
public class HireRequestController {

    private final HireRequestService hireRequestService;
    private final UserDao userDao;
    private final HireRequestDao hireRequestDao;

    public HireRequestController(HireRequestService hireRequestService, UserDao userDao, HireRequestDao hireRequestDao) {
        this.hireRequestService = hireRequestService;
        this.userDao = userDao;
        this.hireRequestDao = hireRequestDao;
    }

    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public List<HireRequest> getAll() {
        return hireRequestService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public HireRequest create(@Valid @RequestBody HireRequest hireRequest, Principal principal) {
        hireRequest.setRequestedBy(userDao.getUserByUsername(principal.getName()).getId());
        hireRequest.setStatus("PENDING");
 
        return hireRequestDao.createHireRequest(hireRequest);    
    }





    @GetMapping("/mine")
    public List<HireRequest> getMine(Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return hireRequestService.getRequestsForUser(userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{hireRequestId}/cancel")
    public void cancel(@PathVariable @Positive int hireRequestId, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        hireRequestService.cancelRequest(hireRequestId, userId);
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

    @PutMapping("/{hireRequestId}/status")
    @PreAuthorize("hasRole('STAFF')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable @Positive int hireRequestId,
                             @RequestBody Map<String, String> body) {

        String status = body.get("status");

        System.out.println("Status received: " + status);

        if (status == null || status.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Status is required"
            );
        }

        hireRequestService.updateStatus(hireRequestId, status);
    }


}
