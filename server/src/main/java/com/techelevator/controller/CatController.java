package com.techelevator.controller;

import com.techelevator.dao.CatDao;
import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Cat;
import com.techelevator.model.User;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/cats")
public class CatController {
    private final CatDao catDao;
    private final UserDao userDao;
    public CatController(CatDao catDao, UserDao userDao) {
        this.catDao = catDao;
        this.userDao = userDao;
    }

@RequestMapping(path = "", method = RequestMethod.GET)
public List<Cat> list(@RequestParam(required = false) String sort,
                      @RequestParam(required = false, name = "q") String query) {
    return catDao.searchAndSort(query, sort);
}


@RequestMapping(path = "/featured", method = RequestMethod.GET)
public List<Cat> featured() {
        return catDao.getFeaturedCats();
}


@RequestMapping(path = "/{id}", method = RequestMethod.GET)
public Cat get(@PathVariable int id) {
        Cat cat = catDao.getCatById(id);
        if (cat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found");
        } else {
            return cat;
        }
    }

@PreAuthorize("hasRole('STAFF')")
@ResponseStatus(HttpStatus.CREATED)
@RequestMapping(path = "", method = RequestMethod.POST)
public Cat create(@Valid @RequestBody Cat cat, Principal principal) {
        if (principal != null) {
            Integer userId = userDao.findIdByUsername(principal.getName());
            cat.setCreatedBy(userId);
        }
        try{
            return catDao.createCat(cat);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cat not created");
        }
    }


@PreAuthorize("hasRole('STAFF')")
@RequestMapping(path = "/{id}/featured", method = RequestMethod.PUT)
public Cat markFeatured(@PathVariable int id, @RequestParam boolean featured) {
        Cat cat = catDao.getCatById(id);
        if (cat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found");
        }
        cat.setFeatured(featured);
try {
    Cat updated = catDao.updateCat(cat);
    if (updated == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found");
    }
    return updated;
} catch (Exception e) {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to mark featured: " + e.getMessage(), e);
}
}

    @PreAuthorize("hasRole('STAFF')")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Cat update(@Valid @RequestBody Cat cat, @PathVariable int id) {

        if (cat.getCatId() != null && cat.getCatId() != id) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID mismatch");
        }

        cat.setCatId(id);

        try {
            Cat updated = catDao.updateCat(cat);
            if (updated == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cat not found");
            }
            return updated;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to update cat: " + e.getMessage(),
                    e
            );
        }
    }


@PreAuthorize("hasRole('STAFF')")
@ResponseStatus(HttpStatus.NO_CONTENT)
@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
public void delete(@PathVariable int id) {
        try {
    catDao.deleteCatById(id);
} catch (Exception e) {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete cat: " + e.getMessage(), e);
}
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
        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "DAO error - " + e.getMessage()
        );
    }
}

}
