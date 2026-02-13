package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.HireRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcHireRequestDao implements HireRequestDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcHireRequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

@Override
public HireRequest createHireRequest(HireRequest hireRequest) {
    String sql = """
        INSERT INTO hire_request
            (cat_id, requested_by, status, requested_start_date, requested_end_date, created_at)
        VALUES (?, ?, ?, ?, ?, now())
        RETURNING hire_request_id;
        """;

    try {
        Integer newId = jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            hireRequest.getCatId(),
            hireRequest.getRequestedBy(),
            hireRequest.getStatus(),
            hireRequest.getRequestedStartDate(),
            hireRequest.getRequestedEndDate()
        );

        return getHireRequestById(newId);

    } catch (DataAccessException e) {
        throw new DaoException("Unable to create hire request.", e);
    }
}


@Override
public List<HireRequest> getAll() {
    List<HireRequest> requests = new ArrayList<>();

    String sql = """
        SELECT
            hr.hire_request_id,
            hr.cat_id,
            c.name AS cat_name,
            ci.file_path AS primary_image_path,
            hr.requested_by,
            u.username,
            hr.status,
            hr.requested_start_date,
            hr.requested_end_date,
            hr.created_at
        FROM hire_request hr
        JOIN cat c ON hr.cat_id = c.cat_id
        LEFT JOIN cat_image ci
        ON ci.cat_id = c.cat_id
            AND ci.is_primary = true
        LEFT JOIN users u
            ON hr.requested_by = u.user_id
        ORDER BY hr.created_at DESC;
        """;

    try {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        while (rs.next()) {
            requests.add(mapRowToHireRequest(rs));
        }
        return requests;
    } catch (DataAccessException e) {
        throw new DaoException("Unable to retrieve hire requests.", e);
    }
}


    @Override
    public List<HireRequest> getHireRequestsByUserId(int userId) {
        List<HireRequest> requests = new ArrayList<>();

        String sql = """
        SELECT
          hr.hire_request_id,
          hr.cat_id,
          c.name AS cat_name,
          ci.file_path AS primary_image_path,
          hr.requested_by,
          u.username,
          hr.status,
          hr.requested_start_date,
          hr.requested_end_date
        FROM hire_request hr
        JOIN cat c ON hr.cat_id = c.cat_id
        LEFT JOIN cat_image ci
          ON c.cat_id = ci.cat_id
         AND ci.is_primary = true
        LEFT JOIN users u 
            ON hr.requested_by = u.user_id
        WHERE hr.requested_by = ?
        ORDER BY hr.hire_request_id DESC;
        """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
            while (rs.next()) {
                requests.add(mapRowToHireRequest(rs));
            }
        } catch (DataAccessException e) {
            throw new DaoException("Unable to retrieve hire requests", e);
        }

        return requests;
    }


@Override
public HireRequest getHireRequestById(int hireRequestId) {
    String sql = """
        SELECT hr.hire_request_id,
            hr.cat_id,
            hr.requested_by,
            hr.status,
            hr.requested_start_date,
            hr.requested_end_date,
            c.name AS cat_name,
            ci.file_path AS primary_image_path,
            u.username
        FROM hire_request hr
        JOIN cat c ON hr.cat_id = c.cat_id
        LEFT JOIN cat_image ci 
            ON c.cat_id = ci.cat_id AND ci.is_primary = true
        JOIN users u ON hr.requested_by = u.user_id
        WHERE hr.hire_request_id = ?;
        """;

    try {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, hireRequestId);
        return rs.next() ? mapRowToHireRequest(rs) : null;
    } catch (DataAccessException e) {
        throw new DaoException("Unable to retrieve hire request.", e);
    }
}


    @Override
    public boolean cancelHireRequest(int hireRequestId, int userId) {
        String sql =
                "UPDATE hire_request " +
                "SET status = 'CANCELLED' " +
                "WHERE hire_request_id = ? " +
                "  AND requested_by = ? " +
                "  AND status IN ('PENDING','APPROVED');";

        try {
            int rows = jdbcTemplate.update(sql, hireRequestId, userId);
            return rows > 0;
        } catch (Exception e) {
            throw new DaoException("Unable to cancel hire request.", e);
        }
    }

    @Override
    public boolean updateStatus(int hireRequestId, String status) {
    String sql = "UPDATE hire_request SET status = ?, updated_at = now() WHERE hire_request_id = ?;";

    try {
        int rows = jdbcTemplate.update(sql, status, hireRequestId);
        return rows == 1;
    } catch (DataAccessException e) {
        throw new DaoException("Unable to update hire request status.", e);
    }
}

private HireRequest mapRowToHireRequest(SqlRowSet rs) {
    HireRequest hr = new HireRequest();

    hr.setHireRequestId(rs.getInt("hire_request_id"));
    hr.setCatId(rs.getInt("cat_id"));
    hr.setRequestedBy(rs.getInt("requested_by"));
    hr.setStatus(rs.getString("status"));
    hr.setCatName(rs.getString("cat_name"));
    hr.setPrimaryImagePath(rs.getString("primary_image_path"));
    hr.setRequesterUsername(rs.getString("username"));

    if (rs.getDate("requested_start_date") != null) {
        hr.setRequestedStartDate(rs.getDate("requested_start_date").toLocalDate());
    }
    if (rs.getDate("requested_end_date") != null) {
        hr.setRequestedEndDate(rs.getDate("requested_end_date").toLocalDate());
    }

    return hr;
}






}





















