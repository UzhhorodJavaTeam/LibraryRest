package com.libraryrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @Column(name = "authorId")
    @GeneratedValue
    private Integer authorId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "book_author", joinColumns = {
            @JoinColumn(name = "authorId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "bookId",
                    nullable = false, updatable = false)})
    private List<Book> books = new ArrayList<Book>();

    public Author() {
    }

    public Author(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> book) {
        this.books = book;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    @Override
    public String toString(){
        return "Name: "+ firstName + " " + lastName;
    }
}