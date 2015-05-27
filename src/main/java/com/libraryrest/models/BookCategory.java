package com.libraryrest.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "category")
public class BookCategory implements Serializable {

    @Id
    @Column(name = "category_id")
    @GeneratedValue
    private Long category_id;

    @Column(name = "category_title")
    private String category_name;

    @OneToMany(mappedBy = "bookCategory", fetch = FetchType.LAZY)
    private List<Book> book = new ArrayList<Book>();

    public BookCategory() {
    }

    public Long getCategory_id() {
        return category_id;
    }

    public BookCategory(String category_name) {
        this.category_name = category_name;
    }

    public void setCategory_id(Long category_id) {
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
}