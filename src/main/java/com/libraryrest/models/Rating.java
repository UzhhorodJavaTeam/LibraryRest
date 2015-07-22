package com.libraryrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rating")
public class Rating implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "count")
    private Integer count;

    @Column(name = "averageValue")
    private Double averageValue;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bookId", unique = true)
    private Book book;

    public Rating() {
    }

    public Rating(Integer count, Double averageValue, Book book) {
        this.count = count;
        this.averageValue = averageValue;
        this.book = book;
    }

    public Double getAverageValue() {
        return averageValue;
    }

    public void setAverageValue(Double averageValue) {
        this.averageValue = averageValue;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
