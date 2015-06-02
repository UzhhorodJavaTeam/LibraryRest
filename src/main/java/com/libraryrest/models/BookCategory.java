package com.libraryrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(name = "categoryTitle")
    private String categoryTitle;


    @JsonIgnore
    @OneToMany(mappedBy = "bookCategory", fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<Book>();

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

    @Override
    public String toString(){
        return categoryTitle;
    }
}