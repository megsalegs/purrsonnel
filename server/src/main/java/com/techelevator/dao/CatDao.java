package com.techelevator.dao;

import com.techelevator.model.Cat;

import java.util.List;

public interface CatDao {
    List<Cat> getCats();
    Cat getCatById(int id);
    Cat createCat(Cat cat);
    Cat updateCat(Cat cat);
    void deleteCatById(int id);
    List<Cat> searchAndSort(String query, String sortKey);

    Cat getCatByIdWithDetails(int id);



    List<Cat> getFeaturedCats();
}
