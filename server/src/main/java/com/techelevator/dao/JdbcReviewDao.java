package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Review;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcReviewDao implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReviewDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review createReview(Review review) {
        String sql = """
            INSERT INTO review (cat_id, created_by, review_body, rating)
            VALUES (?, ?, ?, ?)
            RETURNING review_id;
            """;

        try {
            Integer id = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                review.getCatId(),
                review.getCreatedBy(),
                review.getReviewBody(),
                review.getRating()
            );

            return id != null ? getReviewById(id) : null;

        } catch (DataAccessException e) {
            throw new DaoException("Failed to create review", e);
        }
    }


    @Override
    public Review updateReview(Review review) {
        String sql = "UPDATE review SET review_body = ?, rating = ? WHERE review_id = ?";
        try {
            int rows = jdbcTemplate.update(sql,
                    review.getReviewBody(),
                    review.getRating(),
                    review.getReviewId());
            return rows == 1 ? getReviewById(review.getReviewId()) : null;
        } catch (DataAccessException e) {
            throw new DaoException("Failed to update review", e);
        }
    }

    @Override
    public void deleteReview(int reviewId) {
        String sql = "DELETE FROM review WHERE review_id = ?";
        try {
            jdbcTemplate.update(sql, reviewId);
        } catch (DataAccessException e) {
            throw new DaoException("Failed to delete review", e);
        }
    }


    @Override
    public List<Review> getReviewsByCatId(int catId) {
        String sql = """
            SELECT review_id, cat_id, created_by, review_body, rating
            FROM review
            WHERE cat_id = ?
            ORDER BY review_id;
            """;

        List<Review> reviews = new ArrayList<>();

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, catId);
            while (rs.next()) {
                reviews.add(mapRowFromSqlRowSet(rs));
            }
            return reviews;
        } catch (DataAccessException e) {
            throw new DaoException("Failed to get reviews for cat", e);
        }
    }


    public Review getReviewById(int id) {
        String sql = """
            SELECT review_id, cat_id, created_by, review_body, rating
            FROM review
            WHERE review_id = ?;
            """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
            return rs.next() ? mapRowFromSqlRowSet(rs) : null;
        } catch (DataAccessException e) {
            throw new DaoException("Failed to query review by id", e);
        }
    }


    private Review mapRowFromSqlRowSet(SqlRowSet rs) {
        Review r = new Review();

        r.setReviewId(rs.getInt("review_id"));
        r.setCatId(rs.getInt("cat_id"));
        r.setCreatedBy(rs.getInt("created_by"));
        r.setReviewBody(rs.getString("review_body"));
        r.setRating((Integer) rs.getObject("rating"));

        return r;
    }

}
