package com.techelevator.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class HireRequest {

    private Integer hireRequestId;
    private String catName;
    private String primaryImagePath;


    @NotNull
    @Positive
    private Integer catId;

    private Integer requestedBy;

    //pending, approved, declined, cancelled, completed
    private String status;
    private LocalDate requestedStartDate;
    private LocalDate requestedEndDate;
    public HireRequest() { }

    public HireRequest(
            Integer hireRequestId,
            String catName,
            String primaryImagePath,
            Integer catId,
            Integer requestedBy,
            String status,
            LocalDate requestedStartDate,
            LocalDate requestedEndDate
            ) {
        this.hireRequestId= hireRequestId;
        this.catName = catName;
        this.primaryImagePath = primaryImagePath;
        this.catId = catId;
        this.requestedBy = requestedBy;
        this.status = status;
        this.requestedStartDate = requestedStartDate;
        this.requestedEndDate = requestedEndDate;
    }

    public int getHireRequestId() {
        return hireRequestId;
    }
    public void setHireRequestId(int hireRequestId) {
        this.hireRequestId = hireRequestId;
    }
    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getPrimaryImagePath() {
        return primaryImagePath;
    }

    public void setPrimaryImagePath (String primaryImagePath) {
        this.primaryImagePath = primaryImagePath;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Integer requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getRequestedStartDate() {
        return requestedStartDate;
    }

    public void setRequestedStartDate(LocalDate requestedStartDate) {
        this.requestedStartDate = requestedStartDate;
    }

    public LocalDate getRequestedEndDate() {
        return requestedEndDate;
    }

    public void setRequestedEndDate(LocalDate requestedEndDate) {
        this.requestedEndDate = requestedEndDate;
    }

    @Override
    public String toString() {
        return "HireRequest{" +
                "hireRequestId=" + hireRequestId +
                ", catId=" + catId +
                ", requestedBy=" + requestedBy +
                ", status='" + status + '\'' +
                ", requestedStartDate=" + requestedStartDate +
                ", requestedEndDate=" + requestedEndDate +
                '}';
    }


}
