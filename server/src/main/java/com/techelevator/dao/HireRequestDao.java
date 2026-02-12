package com.techelevator.dao;

import com.techelevator.model.HireRequest;

import java.util.List;

public interface HireRequestDao {

    HireRequest createHireRequest(HireRequest hireRequest);

    List<HireRequest> getHireRequestsByUserId(int userId);

    HireRequest getHireRequestById(int hireRequestId);

    boolean cancelHireRequest(int hireRequestId, int userId);
    
    boolean updateStatus(int hireRequestId, String status);

    List<HireRequest> getAll();
}
