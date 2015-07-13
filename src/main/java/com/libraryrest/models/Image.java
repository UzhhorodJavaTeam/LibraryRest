package com.libraryrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "image")
public class Image implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "url")
    private String url;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "image_gallery", joinColumns = @JoinColumn(name = "imgId"), inverseJoinColumns = @JoinColumn(name = "bookId"))
    private Book book;


    public Image() {

    }

    public Image(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
