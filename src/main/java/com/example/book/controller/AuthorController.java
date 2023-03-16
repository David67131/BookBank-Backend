package com.example.book.controller;

import com.example.book.entity.Author;
import com.example.book.entity.Book;
import com.example.book.respository.AuthorRepository;
import com.example.book.respository.BookRepository;
import com.example.book.utils.BeanUtils;
import com.example.book.utils.R;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Transactional
@RestController
@RequestMapping("author")
public class AuthorController {

    @Resource
    private AuthorRepository authorRepository;

    @Resource
    private BookRepository bookRepository;

    @PostMapping("save")
    public R save(@RequestBody Author author) {
        Long id = author.getId();
        if (id != null) {
            Optional<Author> authorOptional = authorRepository.findById(id);
            if (authorOptional.isEmpty()) {
                return R.failed("Edit error. This author is not exists.");
            }
            Author dbData = authorOptional.get();
            BeanUtils.copyPropertiesIgnoreNull(author, dbData);
            authorRepository.save(dbData);
            return R.success("Edit successfully.");
        }
        authorRepository.save(author);
        return R.success("Save successfully!");
    }

    @GetMapping("page")
    public R page(@RequestParam(defaultValue = "0") int start,
                  @RequestParam(defaultValue = "10") int length) {
        PageRequest request = PageRequest.of(start, length);
        Page<Author> page = authorRepository.findAll(request);
        return R.success("", page);
    }

    @GetMapping("delete/{id}")
    public R delete(@PathVariable Long id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if (authorOptional.isPresent()) {
            List<Book> books = bookRepository.findByAuthor(authorOptional.get());
            if (!CollectionUtils.isEmpty(books)) {
                return R.failed("This author has at least one book! Can't delete!");
            }
            authorRepository.delete(authorOptional.get());
        }
        return R.success("Delete successfully!");
    }
}
