package com.libraryrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @Column(name = "author_id")
    @GeneratedValue
    private Integer author_id;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "book_author", joinColumns = {
            @JoinColumn(name = "author_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "book_id",
                    nullable = false, updatable = false)})
    private List<Book> book = new ArrayList<Book>();

    public Author() {
    }

    public Author(String last_name, String first_name) {
        this.last_name = last_name;
        this.first_name = first_name;
    }

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Integer getAuthorId() {
        return author_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }


    @Override
    public String toString(){
        return "Name: "+ first_name + " " +  last_name;
    }
}