package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(int userId) {
        String sql = """
            SELECT user_id, username, password_hash, role
            FROM users
            WHERE user_id = ?;
            """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
            return rs.next() ? mapRowToUser(rs) : null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        String sql = """
            SELECT user_id, username, password_hash, role
            FROM users
            ORDER BY username;
            """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
            return users;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = """
            SELECT user_id, username, password_hash, role
            FROM users
            WHERE username = ?;
            """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, username == null ? "" : username);
            return rs.next() ? mapRowToUser(rs) : null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    @Override
    public Integer findIdByUsername(String username) {
        final String sql = "SELECT user_id FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new DaoException("Failed to query user id by username", e);
        }
    }

    @Override
    public User createUser(User newUser) {
        if (newUser.getHashedPassword() == null) {
            throw new DaoException("User cannot be created with null password");
        }

        String sql = """
            INSERT INTO users (username, password_hash, role)
            VALUES (?, ?, ?)
            RETURNING user_id;
            """;

        try {
            Integer userId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                newUser.getUsername(),
                newUser.getHashedPassword(),
                newUser.getRole()
            );

            return getUserById(userId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Username already exists", e);
        }
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setHashedPassword(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
