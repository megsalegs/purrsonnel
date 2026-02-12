package com.techelevator.controller;

import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.CatImage;
import com.techelevator.model.User;
import com.techelevator.service.CatImageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cats")
public class CatImageController {

    private final CatImageService catImageService;
    private final UserDao userDao;

    public CatImageController(CatImageService catImageService, UserDao userDao) {
        this.catImageService = catImageService;
        this.userDao = userDao;
    }

    @GetMapping("/{catId}/images")
    public List<CatImage> getImages(@PathVariable @Positive int catId) {
        return catImageService.getImagesForCat(catId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/{catId}/images")
    public CatImage addImage(@PathVariable @Positive int catId,
                             @Valid @RequestBody CatImage image,
                             Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        image.setCatId(catId);
        return catImageService.addImage(catId, image, userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/{catId}/images/{imageId}/primary")
    public void setPrimary(@PathVariable @Positive int catId,
                           @PathVariable @Positive int imageId) {
        catImageService.setPrimary(catId, imageId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('STAFF')")
    @DeleteMapping("/{catId}/images/{imageId}")
    public void delete(@PathVariable @Positive int catId,
                        @PathVariable @Positive int imageId) {
        catImageService.deleteImage(imageId);
    }

    private int getUserIdFromPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        try {
            User user = userDao.getUserByUsername(principal.getName());
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            return user.getId();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }
}
