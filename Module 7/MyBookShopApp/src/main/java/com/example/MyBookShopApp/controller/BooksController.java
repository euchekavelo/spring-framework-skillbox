package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.errs.NotFoundException;
import com.example.MyBookShopApp.model.Book;
import com.example.MyBookShopApp.model.BookReview;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

@Controller
public class BooksController {

    private final BookService bookService;
    private final ResourceStorage storage;

    @Autowired
    public BooksController(BookService bookService, ResourceStorage storage) {
        this.bookService = bookService;
        this.storage = storage;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/books/{slug}")
    public String bookPage(@PathVariable String slug, Model model) throws NotFoundException {
        Book book = bookService.findBookBySlug(slug);
        model.addAttribute("slugBook", book);
        model.addAttribute("bookRating", bookService.getRoundedAverageScore(slug));
        model.addAttribute("numberOfRating", bookService.getNumberOfBookRating(slug));
        model.addAttribute("ratingMap", bookService.getQuantitativeStatisticsRatingByBookSlug(slug));
        return "/books/slug";
    }

    @PostMapping("/changeBookStatus/bookRating")
    public String saveBookRating(@RequestParam("bookId") Integer bookId, @RequestParam("value") Integer value)
            throws NotFoundException {
        String bookSlug = bookService.getBookById(bookId).getSlug();
        bookService.saveBookRating(1, bookId, value);
        return ("redirect:/books/" + bookSlug);
    }

    @PostMapping("/books/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug)
            throws IOException, NotFoundException {

        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookService.findBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookService.save(bookToUpdate); //save new path in db here

        return ("redirect:/books/" + slug);
    }

    @GetMapping("/books/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {

        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @PostMapping("/books/bookReview")
    public String addBookReview(@RequestParam("bookId") Integer bookId, @RequestParam("text") String text)
            throws NotFoundException {
        bookService.saveBookReview(bookId, text);
        Book currentBook = bookService.getBookById(bookId);
        return ("redirect:/books/" + currentBook.getSlug());
    }

    @PostMapping("/bookReview/rateBookReview")
    public String rateBookReview(@RequestParam("value") Short value, @RequestParam("reviewid") Integer reviewId)
            throws NotFoundException {

        bookService.saveBookReviewLike(value, reviewId);
        BookReview bookReview = bookService.getBookReviewById(reviewId);
        String bookSlug = bookReview.getBook().getSlug();
        return ("redirect:/books/" + bookSlug);
    }

}
