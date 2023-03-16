package com.example.book.controller;

import com.example.book.entity.Book;
import com.example.book.respository.BookRepository;
import com.example.book.utils.BeanUtils;
import com.example.book.utils.R;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Transactional
@RestController
@RequestMapping("book")
public class BookController {

    @Resource
    private BookRepository bookRepository;

    @PostMapping("save")
    public R save(@RequestBody Book book) {
        Long id = book.getId();
        if (id != null) {
            Optional<Book> bookOptional = bookRepository.findById(id);
            if (bookOptional.isEmpty()) {
                return R.failed("Edit error. This book is not exists.");
            }
            Book dbData = bookOptional.get();
            BeanUtils.copyPropertiesIgnoreNull(book, dbData);
            bookRepository.save(dbData);
            return R.success("Edit successfully.");
        }
        bookRepository.save(book);
        return R.success("Save successfully!");
    }

    @GetMapping("page")
    public R page(@RequestParam(defaultValue = "0") int start,
                  @RequestParam(defaultValue = "10") int length) {
        PageRequest request = PageRequest.of(start, length);
        Page<Book> page = bookRepository.findAll(request);
        return R.success("", page);
    }

    @GetMapping("delete/{id}")
    public R delete(@PathVariable Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        bookOptional.ifPresent(bookRepository::delete);
        return R.success("Delete successfully!");
    }
}
