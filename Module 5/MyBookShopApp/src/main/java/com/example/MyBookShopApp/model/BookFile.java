package com.example.MyBookShopApp.model;

import javax.persistence.*;

@Entity
@Table(name = "book_file")
public class BookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private String path;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private BookFileType bookFileType;

    public BookFileType getBookFileType() {
        return bookFileType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
