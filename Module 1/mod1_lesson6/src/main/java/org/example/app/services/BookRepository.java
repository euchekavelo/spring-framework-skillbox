package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        if (!book.getAuthor().isEmpty() || !book.getTitle().isEmpty() || book.getSize() != null) {
            book.setId(book.hashCode());
            logger.info("store new book: " + book);
            repo.add(book);
        } else
            logger.info("Saving book with empty parameters is not possible.");
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        logger.info("Removal is not possible. The book with the specified id(" + bookIdToRemove + ") was not found.");
        return false;
    }

    @Override
    public void removeItemByRegex(String regex) {
        String regexAuthor = "author:\\s.+";
        String regexTitle = "title:\\s.+";
        String regexSize = "size:\\s\\p{Digit}+";
        if (regex.matches(regexAuthor)) {
            String author = regex.replace("author: ", "");
            retreiveAll().stream().filter(book -> book.getAuthor().equals(author)).forEach(book -> {
                logger.info("remove book completed: " + book);
                repo.remove(book);
            });
        } else if (regex.matches(regexTitle)) {
            String title = regex.replace("title: ", "");
            retreiveAll().stream().filter(book -> book.getTitle().equals(title)).forEach(book -> {
                logger.info("remove book completed: " + book);
                repo.remove(book);
            });
        } else if (regex.matches(regexSize)) {
            int size = Integer.parseInt(regex.replace("size: ", ""));
            retreiveAll().stream().filter(book -> book.getSize() == size).forEach(book -> {
                logger.info("remove book completed: " + book);
                repo.remove(book);
            });
        } else
            logger.info("The search request is set incorrectly.");
    }
}
