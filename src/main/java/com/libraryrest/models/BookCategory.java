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
    @Column(name = "category_id")
    @GeneratedValue
    private Integer category_id;

    @Column(name = "category_title")
    private String category_name;

    @JsonIgnore
    @OneToMany(mappedBy = "bookCategory", fetch = FetchType.EAGER)
    private List<Book> book = new ArrayList<Book>();

    public BookCategory() {
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public BookCategory(String category_name) {
        this.category_name = category_name;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }

    @Override
    public String toString(){
        return "category_id="+category_id+", category_name="+category_name+", book="+book;
    }
}