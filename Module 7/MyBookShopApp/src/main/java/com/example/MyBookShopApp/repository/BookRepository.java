package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByPriceOldBetween(Integer min, Integer max);

    @Query("from Book where isBestseller=1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage);

    List<Book> findBooksByOrderByPubDateDesc(Pageable nextPage);

    List<Book> findBooksByPubDateBetweenOrderByPubDateDesc(Date from, Date to, Pageable nextPage);

    List<Book> findBooksByTagList_Slug(String tagSlug, Pageable nextPage);

    List<Book> findBooksByTagList_Id(Integer tagId, Pageable nextPage);

    @Query(value = "WITH RECURSIVE rec as (\n" +
                        "\tSELECT g.id \n" +
                        "\tFROM genres g \n" +
                        "\tWHERE g.id = :genreId \n" +
                        "\tUNION ALL \n" +
                        "\tSELECT g.id \n" +
                        "\tFROM genres g INNER JOIN rec r ON g.parent_id = r.id \n" +
                    ") \n" +
                    "SELECT b.* \n" +
                    "FROM books b INNER JOIN book2genre bg ON b.id = bg.book_id \n" +
                    "LEFT JOIN genres g ON bg.genre_id = g.id \n" +
                    "WHERE g.id IN (SELECT * FROM rec r)", nativeQuery = true)
    List<Book> getBooksByGenreId(@Param("genreId") Integer genreId, Pageable nextPage);

    List<Book> getBooksByAuthorList_IdOrderByTitle(Integer authorId, Pageable nextPage);

    Optional<Book> findBookBySlug(String slug);

    List<Book> findBooksBySlugIn(String[] slugs);

    Optional<Book> findBookById(Integer id);
}
