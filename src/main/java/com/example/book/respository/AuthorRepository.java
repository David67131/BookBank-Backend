package com.example.book.respository;

import com.example.book.entity.Author;
import com.example.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
