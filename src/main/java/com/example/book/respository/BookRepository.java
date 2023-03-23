package com.example.book.respository;

import com.example.book.entity.Author;
import com.example.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * find book by author
     * @param author author
     * @return books
     */
    List<Book> findByAuthor(Author author);
}
