package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.dto.AssessmentDto;
import com.example.MyBookShopApp.errs.BookstoreApiWrongParameterException;
import com.example.MyBookShopApp.errs.NotFoundException;
import com.example.MyBookShopApp.model.*;
import com.example.MyBookShopApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.engine.IElementDefinitionsAware;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewLikeRepository bookReviewLikeRepository;
    private final BookRatingRepository bookRatingRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository,
                       BookReviewRepository bookReviewRepository, BookReviewLikeRepository bookReviewLikeRepository,
                       BookRatingRepository bookRatingRepository) {

        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookRatingRepository = bookRatingRepository;
    }

    public List<Book> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        if (title.equals("") || title.length() <= 1){
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        }else {
            List<Book> data = bookRepository.findBooksByTitleContaining(title);
            if (data.size() > 0){
                return data;
            }else {
                throw new BookstoreApiWrongParameterException("No data found with specified parameters...");
            }
        }
    }

    public List<Book> getBooksWithPriceBetween(Integer min, Integer max){
        return bookRepository.findBooksByPriceOldBetween(min,max);
    }

    public List<Book> getBooksWithMaxPrice(){
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers(){
        return bookRepository.getBestsellers();
    }

    public Book findBookBySlug(String slug) throws NotFoundException {
        Optional<Book> optionalBook = bookRepository.findBookBySlug(slug);
        if (optionalBook.isPresent()) {
            return optionalBook.get();
        } else {
            throw new NotFoundException("The book with the criteria '" + slug + "' was not found.");
        }
    }

    public void save(Book bookToUpdate) {
        bookRepository.save(bookToUpdate);
    }

    public Integer getRoundedAverageScore(String bookSlug) {
        Optional<Integer> roundedAverageAssessment = bookRatingRepository.roundedAverageAssessment(bookSlug);
        return roundedAverageAssessment.orElse(0);
    }

    public void saveBookRating(Integer userId, Integer bookId, Integer value) {
        Optional<BookRating> foundEntry = bookRatingRepository.getBookRatingByUserIdAndBookId(userId, bookId);
        BookRating bookRating;
        if (!foundEntry.isPresent()) {
            bookRating = new BookRating();
            bookRating.setUserId(userId);
            bookRating.setBookId(bookId);
        } else {
            bookRating = foundEntry.get();
        }
        bookRating.setAssessment(value);
        bookRatingRepository.save(bookRating);
    }

    public Book getBookById(Integer bookId) throws NotFoundException {
        Optional<Book> optionalBook = bookRepository.findBookById(bookId);
        if (optionalBook.isPresent()) {
            return optionalBook.get();
        } else {
            throw new NotFoundException("The book with the criteria '" + bookId + "' was not found.");
        }
    }

    public void saveBookReview(Integer bookId, String text) throws NotFoundException {
        Optional<Book> optionalBook = bookRepository.findBookById(bookId);
        Optional<User> optionalUser = userRepository.findUserById(1);
        if (optionalBook.isPresent() && optionalUser.isPresent()) {
            Book book = optionalBook.get();
            User user = optionalUser.get();
            BookReview bookReview = new BookReview();
            bookReview.setUser(user);
            bookReview.setBook(book);
            bookReview.setText(text);
            bookReview.setTime(LocalDateTime.now());
            bookReviewRepository.save(bookReview);
        } else {
            throw new NotFoundException("The book with the criteria '" + bookId + "' was not found.");
        }
    }

    public BookReview getBookReviewById(Integer reviewId) {
        Optional<BookReview> optionalBookReview = bookReviewRepository.getBookReviewById(reviewId);
        return optionalBookReview.orElse(null);
    }

    public void saveBookReviewLike(Short value, Integer bookReviewId) throws NotFoundException {
        Optional<BookReview> optionalBookReview = bookReviewRepository.getBookReviewById(bookReviewId);
        Optional<User> optionalUser = userRepository.findUserById(1);
        if (optionalBookReview.isPresent() && optionalUser.isPresent()) {
            BookReviewLike bookReviewLike = new BookReviewLike();
            bookReviewLike.setBookReview(optionalBookReview.get());
            bookReviewLike.setUser(optionalUser.get());
            bookReviewLike.setValue(value);
            bookReviewLike.setTime(LocalDateTime.now());
            bookReviewLikeRepository.save(bookReviewLike);
        } else {
            throw new NotFoundException("Failed to save feedback score. The review with id " + bookReviewId +
                    " was not found in the database.");
        }
    }

    public Integer getNumberOfBookRating(String bookSlug) {
        return bookRatingRepository.getNumberOfBookRating(bookSlug).orElse(0);
    }

    public Map<Integer, Integer> getQuantitativeStatisticsRatingByBookSlug(String bookSlug) {
        return bookRatingRepository.getQuantitativeStatisticsRatingByBookSlug(bookSlug).stream()
                .collect(Collectors.toMap(AssessmentDto::getAssessment, AssessmentDto::getCount));
    }
}
