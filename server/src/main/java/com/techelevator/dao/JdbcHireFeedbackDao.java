// JdbcHireFeedbackDao
package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.HireFeedback;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcHireFeedbackDao implements HireFeedbackDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcHireFeedbackDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
public HireFeedback createFeedbackForRequest(HireFeedback feedback) {
    String sql = """
        INSERT INTO hire_feedback (hire_request_id, created_by, rating, feedback_body)
        SELECT
            hr.hire_request_id,
            ?,
            ?,
            ?
        FROM hire_request hr
        WHERE hr.hire_request_id = ?
          AND hr.requested_by = ?
          AND hr.status IN ('APPROVED','COMPLETED')
        RETURNING feedback_id;
        """;

    try {
        int userId = feedback.getCreatedBy();

        Integer feedbackId = jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            userId,
            feedback.getRating(),
            feedback.getFeedbackBody(),
            feedback.getHireRequestId(),
            userId
        );

        return getFeedbackById(feedbackId);

    } catch (DataIntegrityViolationException e) {
        throw new DaoException(
            "Unable to create feedback (already exists or invalid data).",
            e
        );
    } catch (Exception e) {
        throw new DaoException("Unable to create feedback.", e);
    }
}


    @Override
    public HireFeedback getFeedbackByHireRequestId(int hireRequestId) {
        String sql =
                "SELECT feedback_id, hire_request_id, created_by, rating, feedback_body, created_at " +
                        "FROM hire_feedback " +
                        "WHERE hire_request_id = ?;";

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, hireRequestId);
            if (rs.next()) {
                return mapRowToHireFeedback(rs);
            }
            return null;
        } catch (Exception e) {
            throw new DaoException("Unable to retrieve feedback by hire_request_id.", e);
        }
    }

    @Override
    public HireFeedback getFeedbackById(int feedbackId) {
        String sql =
                "SELECT feedback_id, hire_request_id, created_by, rating, feedback_body, created_at " +
                        "FROM hire_feedback " +
                        "WHERE feedback_id = ?;";

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, feedbackId);
            if (rs.next()) {
                return mapRowToHireFeedback(rs);
            }
            return null;
        } catch (Exception e) {
            throw new DaoException("Unable to retrieve feedback by id.", e);
        }
    }

private HireFeedback mapRowToHireFeedback(SqlRowSet rs) {
    HireFeedback hf = new HireFeedback();

    hf.setFeedbackId(rs.getInt("feedback_id"));
    hf.setHireRequestId(rs.getInt("hire_request_id"));
    hf.setCreatedBy(rs.getInt("created_by"));
    hf.setRating((Integer) rs.getObject("rating"));
    hf.setFeedbackBody(rs.getString("feedback_body"));

    if (rs.getTimestamp("created_at") != null) {
        hf.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
    }

    return hf;
}

}
