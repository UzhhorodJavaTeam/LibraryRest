package com.libraryrest.controllers;

import com.libraryrest.DAO.BookDAO;
import com.libraryrest.DAO.RatingDao;
import com.libraryrest.DAO.UserDao;
import com.libraryrest.DAO.VoteDao;
import com.libraryrest.models.Book;
import com.libraryrest.models.Rating;
import com.libraryrest.models.User;
import com.libraryrest.models.Vote;
import org.apache.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/votes")
public class VoteController {
    @Autowired
    VoteDao voteDao;

    @Autowired
    BookDAO bookDAO;

    @Autowired
    UserDao userDao;

    @Autowired
    RatingDao ratingDao;

    final org.apache.log4j.Logger logger = LogManager.getLogger(getClass());

    @RequestMapping(method = RequestMethod.POST)
    public Vote addVote(@RequestParam("value") Integer value,@RequestParam("bookId") Integer bookId,
                        @RequestParam("userId") Integer userId){
        logger.info("POST: /votes");
        Book book = bookDAO.findById(userId);
        User user = userDao.findById(userId);
        Vote vote = null;
        Vote foundVote = voteDao.findByBookIdAndUserId(bookId, userId);
        if (foundVote==null){
            vote = new Vote(value, book, user);
            Integer id = voteDao.saveOrUpdate(vote);
            vote.setId(id);
        }else{
            foundVote.setValue(value);
            voteDao.saveOrUpdate(foundVote);
        }
        
        Rating rating = ratingDao.findByBookId(bookId);
        Book foundedBook = bookDAO.findById(bookId);

        Set<Vote> votes = foundedBook.getVotes();
        Integer count = 0;
        Double tmpAverageValue = 0.0;
        for (Vote voteItr : votes) {
            Integer valueItr = voteItr.getValue();
            if (valueItr!=null){
                count++;
                tmpAverageValue+= voteItr.getValue();
            }
        }
        Double tmpValue = tmpAverageValue/count;
        Double averageValue =  Math.round (tmpValue * 100.0) / 100.0;
        System.out.println(averageValue);
        rating.setAverageValue(averageValue);
        rating.setCount(count);
        Integer id = ratingDao.saveOrUpdate(rating);
        rating.setId(id);

        return vote;
    }
}