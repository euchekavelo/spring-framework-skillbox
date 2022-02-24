package com.example.MyBookShopApp.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "DATE NOT NULL")
    private Date pubDate;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private int isBestseller;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false)
    private String title;

    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(columnDefinition = "SMALLINT NOT NULL DEFAULT 0")
    private int discount;

    @ManyToMany(mappedBy = "bookSet")
    private Set<Author> authorSet;

    @ManyToMany
    @JoinTable(name = "book2genre",
    joinColumns = @JoinColumn(name = "book_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genreSet;

    @ManyToMany(mappedBy = "bookSet")
    private Set<User> userSet;

    @OneToMany(mappedBy = "book")
    private Set<FileDownload> fileDownloadSet;

    @OneToMany(mappedBy = "book")
    private Set<BalanceTransaction> balanceTransactionSet;

    @OneToMany(mappedBy = "book")
    private Set<BookReview> bookReviewSet;

    public Set<BookReview> getBookReviewSet() {
        return new HashSet<>(bookReviewSet);
    }

    public Set<BalanceTransaction> getBalanceTransactionSet() {
        return new HashSet<>(balanceTransactionSet);
    }

    public Set<FileDownload> getFileDownloadSet() {
        return new HashSet<>(fileDownloadSet);
    }

    public Set<User> getUserSet() {
        return new HashSet<>(userSet);
    }

    public Set<Genre> getGenreSet() {
        return new HashSet<>(genreSet);
    }

    public String getAuthors() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Author author : authorSet) {
            stringBuilder.append(stringBuilder.length() == 0 ? "" : ", ")
                    .append(author.getName());
        }

        return stringBuilder.toString();
    }

    public Set<Author> getAuthorSet() {
        return new HashSet<>(authorSet);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public int getIsBestseller() {
        return isBestseller;
    }

    public void setIsBestseller(int isBestseller) {
        this.isBestseller = isBestseller;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
