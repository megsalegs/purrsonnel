// HireFeedbackDao interface
package com.techelevator.dao;

import com.techelevator.model.HireFeedback;

public interface HireFeedbackDao {

    HireFeedback createFeedbackForRequest(HireFeedback feedback);

    HireFeedback getFeedbackByHireRequestId(int hireRequestId);

    HireFeedback getFeedbackById(int feedbackId);
}
