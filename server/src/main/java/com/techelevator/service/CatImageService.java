package com.techelevator.service;

import com.techelevator.dao.CatDao;
import com.techelevator.dao.CatImageDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Cat;
import com.techelevator.model.CatImage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CatImageService {
    private final CatImageDao catImageDao;
    private final CatDao catDao;

    public CatImageService(CatImageDao catImageDao, CatDao catDao) {
        this.catImageDao = catImageDao;
        this.catDao = catDao;
    }

    public List<CatImage> getImagesForCat(int catId) {
        if (catId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid catId.");
        }

        try {
            Cat cat = catDao.getCatById(catId);
            if (cat == null || !cat.isActive()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found.");
            }
            return catImageDao.getImagesByCatId(catId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    public CatImage addImage(int catId, CatImage incoming, int uploadedBy) {
        if (catId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid catId.");
        }
        if (incoming == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image body is required.");
        }
        if (incoming.getFilePath() == null || incoming.getFilePath().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "filePath is required.");
        }

        try {
            Cat cat = catDao.getCatById(catId);
            if (cat == null || !cat.isActive()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found.");
            }

            CatImage toCreate = new CatImage();
            toCreate.setCatId(catId);
            toCreate.setUploadedBy(uploadedBy);
            toCreate.setFilePath(incoming.getFilePath());
            toCreate.setPrimary(incoming.isPrimary());

            return catImageDao.addImage(toCreate);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public void setPrimary(int catId, int imageId) {
        if (catId <= 0 || imageId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid catId or imageId.");
        }

        try {
            Cat cat = catDao.getCatById(catId);
            if (cat == null || !cat.isActive()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found.");
            }

            boolean updated = catImageDao.setPrimaryImage(imageId, catId);
            if (!updated) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found for that cat.");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    public void deleteImage(int imageId) {
        if (imageId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid imageId.");
        }

        try {
            boolean deleted = catImageDao.deleteImage(imageId);
            if (!deleted) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found.");
            }
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }
}
