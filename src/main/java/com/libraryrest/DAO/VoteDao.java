package com.libraryrest.DAO;

import com.libraryrest.models.Vote;

import java.util.List;

public interface VoteDao {

    public Integer saveOrUpdate(Vote vote);

    public List<Vote> getAllVotes();

    public Vote findByBookIdAndUserId(Integer bookId, Integer userId);

    public Vote findById(Integer id);

    public void deleteVote(Integer id);

}
