package com.libraryrest.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="author")
public class Author{

    @Id
    @Column(name="author_id")
    @GeneratedValue
    private Integer author_id;

    @Column(name="first_name")
    private String first_name;

    @Column(name="last_name")
    private String last_name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
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

}