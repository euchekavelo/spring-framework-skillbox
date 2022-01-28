package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    //private final List<Book> repo = new ArrayList<>();
    private ApplicationContext context;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retreiveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs,int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author",book.getAuthor());
        parameterSource.addValue("title",book.getTitle());
        parameterSource.addValue("size",book.getSize());
        jdbcTemplate.update("INSERT INTO books(author,title,size) VALUES(:author, :title, :size)",parameterSource);
        logger.info("store new book: " + book);
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id",bookIdToRemove);
        jdbcTemplate.update("DELETE FROM books WHERE id = :id",parameterSource);
        logger.info("remove book completed");
        return true;
    }

    @Override
    public boolean removeItemByRegex(String regex) {
        String startWithAuthor = "author";
        String startWithTitle = "title";
        String startWithSize = "size";
        int count;

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if (regex.startsWith(startWithAuthor)) {
            String author = regex.replace("author: ", "");
            parameterSource.addValue("author", author);
            count = jdbcTemplate.update("DELETE FROM books WHERE author = :author", parameterSource);
        } else if (regex.startsWith(startWithTitle)) {
            String title = regex.replace("title: ", "");
            parameterSource.addValue("title", title);
            count = jdbcTemplate.update("DELETE FROM books WHERE title = :title", parameterSource);
        } else if (regex.startsWith(startWithSize)) {
            int size = Integer.parseInt(regex.replace("size: ", ""));
            parameterSource.addValue("size", size);
            count = jdbcTemplate.update("DELETE FROM books WHERE size = :size", parameterSource);
        } else {
            logger.info("The search request is set incorrectly.");
            return false;
        }

        logger.info("Number of books removed: " + count);
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public void defaultInit(){
        logger.info("default INIT in book repo bean");
    }

    public void defaultDestroy(){
        logger.info("default DESTROY in book repo bean");
    }
}
