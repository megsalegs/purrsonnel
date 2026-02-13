package com.techelevator.controller;

import com.techelevator.dao.BookmarkDao;
import com.techelevator.dao.CatDao;
import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Cat;
import com.techelevator.model.User;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bookmarks")
@PreAuthorize("isAuthenticated()")
public class BookmarkController {

    private final BookmarkDao bookmarkDao;
    private final UserDao userDao;
    private final CatDao catDao;

    public BookmarkController(BookmarkDao bookmarkDao, UserDao userDao, CatDao catDao) {
        this.bookmarkDao = bookmarkDao;
        this.userDao = userDao;
        this.catDao = catDao;
    }

    @GetMapping
    public List<Cat> getMyBookmarks(Principal principal) {
        try {
            int userId = getUserIdFromPrincipal(principal);
            return bookmarkDao.getBookmarkedCatsByUserId(userId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{catId}")
    public void addBookmark(@PathVariable @Positive int catId, Principal principal) {
        try {
            int userId = getUserIdFromPrincipal(principal);

            Cat cat = catDao.getCatById(catId);
            if (cat == null || !cat.isActive()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found.");
            }

            bookmarkDao.addBookmark(userId, catId);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void removeBookmark(@PathVariable @Positive int catId, Principal principal) {
        try {
            int userId = getUserIdFromPrincipal(principal);
            bookmarkDao.removeBookmark(userId, catId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

        private int getUserIdFromPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        User user;
        try {
            user = userDao.getUserByUsername(principal.getName());
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return user.getId();
    }
}
