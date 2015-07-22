package com.libraryrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by superuser on 25.05.15.
 */
@Entity
@Table(name = "books")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookId")
    Integer bookId;

    @Column(name = "name",unique = true)
    String name;

    @Column(name = "description")
    String description;

    @ManyToMany(cascade = {CascadeType.REFRESH,CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "bookId"), inverseJoinColumns = @JoinColumn(name = "authorId"))
    List<Author> authors = new ArrayList<Author>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinTable(name = "catalog", joinColumns = @JoinColumn(name = "bookId"), inverseJoinColumns = @JoinColumn(name = "categoryId"))
    private BookCategory bookCategory;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<Image>();


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", nullable = false)
    private User user;


    @Column(name = "pdfFileUrl")
    private String pdfFileUrl;

    public Book() {
    }

    public Book(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getPdfFileUrl() {
        return pdfFileUrl;
    }

    public void setPdfFileUrl(String pdfFileUrl) {
        this.pdfFileUrl = pdfFileUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Name: " + name + " Description: " + description + " Authors: " + authors + "pdfUrl: " +pdfFileUrl;
    }
}