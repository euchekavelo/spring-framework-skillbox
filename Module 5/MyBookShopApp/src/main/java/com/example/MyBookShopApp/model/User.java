package com.example.MyBookShopApp.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime regTime;

    @Column(columnDefinition = "INT NOT NULL")
    private int balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @ManyToMany
    @JoinTable(name = "book2user",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> bookSet;

    @OneToMany(mappedBy = "user")
    private Set<FileDownload> fileDownloadSet;

    @OneToMany(mappedBy = "user")
    private Set<BalanceTransaction> balanceTransactionSet;

    @OneToMany(mappedBy = "user")
    private Set<Message> messageSet;

    @OneToMany(mappedBy = "user")
    private Set<BookReview> bookReviewSet;

    @OneToOne(mappedBy = "user")
    private UserContact userContact;

    @OneToMany(mappedBy = "user")
    private Set<BookReviewLike> bookReviewLikeSet;

    public Set<BookReviewLike> getBookReviewLikeSet() {
        return new HashSet<>(bookReviewLikeSet);
    }

    public UserContact getUserContact() {
        return userContact;
    }

    public Set<BookReview> getBookReviewSet() {
        return new HashSet<>(bookReviewSet);
    }

    public Set<Message> getMessageSet() {
        return new HashSet<>(messageSet);
    }

    public Set<BalanceTransaction> getBalanceTransactionSet() {
        return new HashSet<>(balanceTransactionSet);
    }

    public Set<FileDownload> getFileDownloadSet() {
        return new HashSet<>(fileDownloadSet);
    }

    public Set<Book> getBookSet() {
        return new HashSet<>(bookSet);
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

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
