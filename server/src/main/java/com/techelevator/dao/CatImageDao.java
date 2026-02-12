
package com.techelevator.dao;

import com.techelevator.model.CatImage;

import java.util.List;

public interface CatImageDao {

    List<CatImage> getImagesByCatId(int catId);

    CatImage addImage(CatImage image);

    boolean deleteImage(int imageId);

    boolean setPrimaryImage(int imageId, int catId);
}
