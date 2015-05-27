package com.libraryrest.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by superuser on 25.05.15.
 */
@Entity
@Table(name = "books")
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    Integer book_id;

    @Column(name = "name")
    String name;


    @Column(name = "description")
    String description;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "book_author", joinColumns = {
            @JoinColumn(name = "book_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "author_id",
                    nullable = false, updatable = false) })
    Set<Author> authors = new HashSet<Author>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "catalog", joinColumns = @JoinColumn(name = "id_book"), inverseJoinColumns = @JoinColumn(name = "id_category"))
    private BookCategory bookCategory;



    public Book(){
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

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Integer getBook_id() {
        return book_id;
    }

    public void setBook_id(Integer book_id) {
        this.book_id = book_id;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }
}
