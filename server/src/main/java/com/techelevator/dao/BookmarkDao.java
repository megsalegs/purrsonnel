package com.techelevator.dao;
import com.techelevator.model.Cat;
import java.util.List;
public interface BookmarkDao {

    List<Cat> getBookmarkedCatsByUserId(int userId);

    void addBookmark(int userId, int catId);

    void removeBookmark(int userId, int catId);
}
