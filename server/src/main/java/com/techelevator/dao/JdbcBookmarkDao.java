package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Cat;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcBookmarkDao implements BookmarkDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcBookmarkDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
public List<Cat> getBookmarkedCatsByUserId(int userId) {
    List<Cat> cats = new ArrayList<>();

      String sql = """
    SELECT
    c.cat_id,
    c.created_by,
    c.name,
    c.age,
    c.color,
    c.fur_length,
    c.skills,
    c.description,
    c.featured,
    (
        SELECT AVG(r.rating)::INT
        FROM review r
        WHERE r.cat_id = c.cat_id
    ) AS rank_score,
    c.is_active,
    ci.file_path AS primary_image_path
FROM user_bookmark ub
JOIN cat c
    ON ub.cat_id = c.cat_id
LEFT JOIN cat_image ci
    ON ci.cat_id = c.cat_id
   AND ci.is_primary = true
WHERE ub.user_id = ?
ORDER BY c.name;
""";

    SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
    while (rs.next()) {
        cats.add(mapRowToCat(rs));
    }

    return cats;
}



    @Override
    public void addBookmark(int userId, int catId) {
        String sql =
                "INSERT INTO user_bookmark (user_id, cat_id) " +
                "VALUES (?, ?);";

        try {
            jdbcTemplate.update(sql, userId, catId);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Bookmark already exists or invalid cat/user", e);
        } catch (Exception e) {
            throw new DaoException("Unable to add bookmark", e);
        }
    }

    @Override
    public void removeBookmark(int userId, int catId) {
        String sql =
                "DELETE FROM user_bookmark " +
                "WHERE user_id = ? AND cat_id = ?;";

        try {
            jdbcTemplate.update(sql, userId, catId);
        } catch (Exception e) {
            throw new DaoException("Unable to remove bookmark", e);
        }
    }

    private Cat mapRowToCat(SqlRowSet rs) {
        Cat cat = new Cat();
        cat.setCatId(rs.getInt("cat_id"));
        cat.setName(rs.getString("name"));
        cat.setAge(rs.getInt("age"));
        cat.setColor(rs.getString("color"));
        cat.setFur_Length(rs.getString("fur_length"));
        cat.setDescription(rs.getString("description"));
        cat.setSkills(rs.getString("skills"));
        cat.setFeatured(rs.getBoolean("featured"));
        cat.setRankScore((Integer) rs.getObject("rank_score"));
        cat.setActive(rs.getBoolean("is_active"));
        cat.setPrimaryImagePath(rs.getString("primary_image_path"));

        return cat;
    }
}


























