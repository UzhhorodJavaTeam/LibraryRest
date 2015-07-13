package com.libraryrest.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class BookCategory implements Serializable {

    @Id
    @Column(name = "categoryId")
    @GeneratedValue
    private Integer categoryId;

    @Column(name = "categoryTitle" ,unique = true)
    private String categoryTitle;

    @OneToMany(mappedBy = "bookCategory", fetch = FetchType.EAGER,cascade = {CascadeType.REFRESH,CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Book> books = new ArrayList<Book>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", nullable = false)
    private User user;


    public BookCategory() {
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public BookCategory(String categoryName) {
        this.categoryTitle = categoryName;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryName) {
        this.categoryTitle = categoryName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return categoryTitle;
    }
}