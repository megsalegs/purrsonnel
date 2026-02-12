package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Cat;
import com.techelevator.model.Review;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCatDao implements CatDao{
    private final JdbcTemplate jdbcTemplate;
    private final ReviewDao reviewDao;
    public JdbcCatDao(JdbcTemplate jdbcTemplate, ReviewDao reviewDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.reviewDao = reviewDao;
    }

   @Override
public List<Cat> getCats() {
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
        FROM cat c
        LEFT JOIN cat_image ci
            ON ci.cat_id = c.cat_id
           AND ci.is_primary = true
        ORDER BY c.cat_id;
        """;

    try {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        while (rs.next()) {
            cats.add(mapRowToCat(rs));
        }
        return cats;
    } catch (DataAccessException e) {
        throw new DaoException("Failed to retrieve cats", e);
    }
}


@Override
public Cat getCatById(int id) {
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
            c.is_active,
            COALESCE(AVG(r.rating)::INT, NULL) AS rank_score,
            ci.file_path AS primary_image_path
        FROM cat c
        LEFT JOIN review r
            ON c.cat_id = r.cat_id
        LEFT JOIN cat_image ci
            ON c.cat_id = ci.cat_id
           AND ci.is_primary = true
        WHERE c.cat_id = ?
        GROUP BY
            c.cat_id,
            c.created_by,
            c.name,
            c.age,
            c.color,
            c.fur_length,
            c.skills,
            c.description,
            c.featured,
            c.is_active,
            ci.file_path
        """;

    try {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);

        if (!rs.next()) {
            return null;
        }

        return mapRowToCat(rs);

    } catch (DataAccessException e) {
        throw new DaoException("Failed to retrieve cat by id", e);
    }
}


@Override
public Cat createCat(Cat cat) {
    String sql = """
        INSERT INTO cat (
            created_by,
            name,
            age,
            color,
            fur_length,
            skills,
            description,
            featured
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        RETURNING cat_id
        """;

    try {
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, cat.getCreatedBy(), cat.getName(), cat.getAge(), cat.getColor(),
                cat.getFur_length(), cat.getSkills(), cat.getDescription(), cat.getFeatured());
        if (id == null) {
            throw new DaoException("Failed to create cat, no ID returned");
        }  return getCatById(id);
    } catch (DataAccessException e) {
        throw new DaoException("Failed to create cat", e);
    }
}


    @Override
    public Cat updateCat(Cat cat) {
       String sql = """
            UPDATE cat
            SET
                created_by = ?,
                name = ?,
                age = ?,
                color = ?,
                fur_length = ?,
                skills = ?,
                description = ?,
                featured = ?
            WHERE cat_id = ?
            """;

        try {
            int rows = jdbcTemplate.update(sql, cat.getCreatedBy(), cat.getName(), cat.getAge(), cat.getColor(), cat.getFur_length(), cat.getSkills(), cat.getDescription(), cat.getFeatured(), cat.getCatId());
            return rows == 1 ? getCatById(cat.getCatId()) : null;
        } catch (DataAccessException e) {
            throw new DaoException("Failed to update cat", e);
        }
    }
    @Override
    public void deleteCatById(int id) {
        String sql = "DELETE FROM cat WHERE cat_id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            throw new DaoException("Failed to delete cat", e);
        }
    }

   @Override
public List<Cat> getFeaturedCats() {
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
        FROM cat c
        LEFT JOIN cat_image ci
            ON ci.cat_id = c.cat_id
           AND ci.is_primary = true
        WHERE c.featured = true
        ORDER BY
            (
                SELECT AVG(r.rating)::INT
                FROM review r
                WHERE r.cat_id = c.cat_id
            ) DESC NULLS LAST;
        """;

    SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
    while (rs.next()) {
        cats.add(mapRowToCat(rs));
    }
    return cats;
}






@Override
public List<Cat> searchAndSort(String query, String sortKey) {

    StringBuilder sql = new StringBuilder("""
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
        FROM cat c
        LEFT JOIN cat_image ci
            ON ci.cat_id = c.cat_id
           AND ci.is_primary = true
        WHERE 1=1
    """);

    List<Object> params = new ArrayList<>();

    if (query != null && !query.isBlank()) {
        sql.append("""
            AND (
                c.name ILIKE ?
                OR c.skills ILIKE ?
                OR c.color ILIKE ?
                OR c.fur_length ILIKE ?
                OR c.description ILIKE ?
            )
        """);

        String pattern = "%" + query + "%";
        for (int i = 0; i < 5; i++) {
            params.add(pattern);
        }
    }

    String orderBy = switch (sortKey == null ? "" : sortKey.toLowerCase()) {
        case "name" -> "c.name ASC";
        case "age" -> "c.age ASC NULLS LAST";
        case "color" -> "c.color ASC";
        case "featured" -> "c.featured DESC";
        case "rating" -> "rank_score DESC NULLS LAST";
        default -> "c.cat_id DESC";
    };

    sql.append(" ORDER BY ").append(orderBy);

    List<Cat> cats = new ArrayList<>();

    try {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql.toString(), params.toArray());
        while (rs.next()) {
            cats.add(mapRowToCat(rs));
        }
        return cats;
    } catch (DataAccessException e) {
        throw new DaoException("Failed to search and sort cats", e);
    }
}



    @Override
    public Cat getCatByIdWithDetails(int id) {
        Cat cat = getCatById(id);
        if (cat == null) return null;

        try {
            if (reviewDao != null) {
                List<Review> reviews = reviewDao.getReviewsByCatId(id);
                cat.setReviews(reviews);
            }
        } catch (DataAccessException e) {
            throw new DaoException("Failed to load cat details", e);
        }

        return cat;
    }
    private Cat mapRowToCat(SqlRowSet rs) {
        Cat cat = new Cat();

        int id = rs.getInt("cat_id");
        cat.setCatId(rs.wasNull() ? null : id);

        int createdBy = rs.getInt("created_by");
        cat.setCreatedBy(rs.wasNull() ? null : createdBy);

        cat.setName(rs.getString("name"));

        int age = rs.getInt("age");
        cat.setAge(rs.wasNull() ? null : age);

        cat.setColor(rs.getString("color"));

        cat.setFur_Length(rs.getString("fur_length"));

        cat.setSkills(rs.getString("skills"));
        cat.setDescription(rs.getString("description"));

        cat.setFeatured(rs.getBoolean("featured"));
        cat.setActive(rs.getBoolean("is_active"));
        Integer rankScore = (Integer) rs.getObject("rank_score");
        cat.setRankScore(rankScore);
        cat.setPrimaryImagePath(rs.getString("primary_image_path"));

        return cat;
    }
}
