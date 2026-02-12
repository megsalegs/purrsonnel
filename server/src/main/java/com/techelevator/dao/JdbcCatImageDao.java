
package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.CatImage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCatImageDao implements CatImageDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCatImageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CatImage> getImagesByCatId(int catId) {
        List<CatImage> images = new ArrayList<>();

        String sql =
                "SELECT image_id, cat_id, uploaded_by, file_path, is_primary, created_at " +
                        "FROM cat_image " +
                        "WHERE cat_id = ? " +
                        "ORDER BY is_primary DESC, created_at DESC, image_id DESC;";

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, catId);
            while (rs.next()) {
                images.add(mapRowToCatImage(rs));
            }
        } catch (Exception e) {
            throw new DaoException("Unable to retrieve cat images.", e);
        }

        return images;
    }

@Override
public CatImage addImage(CatImage image) {
    try {
        if (image.isPrimary()) {
            jdbcTemplate.update(
                "UPDATE cat_image SET is_primary = false WHERE cat_id = ?;",
                image.getCatId()
            );
        }

        String sql = """
            INSERT INTO cat_image (cat_id, uploaded_by, file_path, is_primary)
            VALUES (?, ?, ?, ?)
            RETURNING image_id;
            """;

        Integer imageId = jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            image.getCatId(),
            image.getUploadedBy(),
            image.getFilePath(),
            image.isPrimary()
        );

        if (image.isPrimary()) {
            jdbcTemplate.update(
                """
                UPDATE cat
                SET primary_image_path = ?
                WHERE cat_id = ?;
                """,
                image.getFilePath(),
                image.getCatId()
            );
        }

        return getImageById(imageId);

    } catch (DataIntegrityViolationException e) {
        throw new DaoException("Invalid cat image (FK/constraint violation).", e);
    } catch (Exception e) {
        throw new DaoException("Unable to add cat image.", e);
    }
}



    @Override
    public boolean deleteImage(int imageId) {
        String sql = "DELETE FROM cat_image WHERE image_id = ?;";

        try {
            int rows = jdbcTemplate.update(sql, imageId);
            return rows > 0;
        } catch (Exception e) {
            throw new DaoException("Unable to delete cat image.", e);
        }
    }

   @Override
    public boolean setPrimaryImage(int imageId, int catId) {
    try {
        jdbcTemplate.update(
            "UPDATE cat_image SET is_primary = false WHERE cat_id = ?;",
            catId
        );

        int rows = jdbcTemplate.update(
            "UPDATE cat_image SET is_primary = true WHERE image_id = ? AND cat_id = ?;",
            imageId, catId
        );

        if (rows == 0) {
            return false;
        }

        jdbcTemplate.update(
            """
            UPDATE cat
            SET primary_image_path = (
                SELECT file_path
                FROM cat_image
                WHERE image_id = ?
            )
            WHERE cat_id = ?;
            """,
            imageId, catId
        );

        return true;

    } catch (Exception e) {
        throw new DaoException("Unable to set primary image.", e);
    }
}



private CatImage getImageById(int imageId) {
    String sql = """
        SELECT image_id, cat_id, uploaded_by, file_path, is_primary, created_at
        FROM cat_image
        WHERE image_id = ?;
        """;

    try {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, imageId);
        return rs.next() ? mapRowToCatImage(rs) : null;
    } catch (Exception e) {
        throw new DaoException("Unable to retrieve cat image by id.", e);
    }
}


    private CatImage mapRowToCatImage(SqlRowSet rs) {
        CatImage img = new CatImage();
        img.setImageId(rs.getInt("image_id"));
        img.setCatId(rs.getInt("cat_id"));
        img.setUploadedBy(rs.getInt("uploaded_by"));
        img.setFilePath(rs.getString("file_path"));
        img.setPrimary(rs.getBoolean("is_primary"));

        Timestamp ts = rs.getTimestamp("created_at");
        img.setCreatedAt(ts == null ? null : ts.toLocalDateTime());

        return img;
    }
}
