package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.model.Book;
import com.example.MyBookShopApp.model.Tag;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainPageService {

    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final BooksRatingAndPopularityService booksRatingAndPopularityService;

    @Autowired
    public MainPageService(BookRepository bookRepository, TagRepository tagRepository,
                           BooksRatingAndPopularityService booksRatingAndPopularityService) {
        this.bookRepository = bookRepository;
        this.tagRepository = tagRepository;
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
    }

    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findAll(nextPage);
    }

    public List<Book> getNewBooksOrderByPubDateDesc(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByOrderByPubDateDesc(nextPage);
    }

    public List<Book> getSortedBooksByPopularRating(Integer offset, Integer limit) {
        return booksRatingAndPopularityService.getSortedBooksByPopularRating(offset, limit);
    }

    public List<Tag> getTagList() {
        return tagRepository.findAll();
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findBookByTitleContaining(searchWord,nextPage);
    }

}
