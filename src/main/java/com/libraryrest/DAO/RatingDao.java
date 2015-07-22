package com.libraryrest.DAO;

import com.libraryrest.models.Rating;

import java.util.List;

public interface RatingDao {

    public Integer saveOrUpdate(Rating rating);

    public List<Rating> getAllRatings();

    public Rating findById(Integer id);

    public Rating findByBookId(Integer id);

    public void deleteRating(Integer id);

}
