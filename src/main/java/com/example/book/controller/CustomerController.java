package com.example.book.controller;

import com.example.book.entity.Author;
import com.example.book.entity.Book;
import com.example.book.entity.Customer;
import com.example.book.respository.BookRepository;
import com.example.book.respository.CustomerRepository;
import com.example.book.transfer.CustomerDto;
import com.example.book.utils.BeanUtils;
import com.example.book.utils.R;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Transactional
@RestController
@RequestMapping("customer")
public class CustomerController {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private BookRepository bookRepository;

    @PostMapping("save")
    public R save(@RequestBody Customer customer) {
        Long id = customer.getId();
        if (id != null) {
            Optional<Customer> customerOptional = customerRepository.findById(id);
            if (customerOptional.isEmpty()) {
                return R.failed("Edit error. This customer is not exists.");
            }
            Customer dbData = customerOptional.get();
            BeanUtils.copyPropertiesIgnoreNull(customer, dbData);
            customerRepository.save(dbData);
            return R.success("Edit successfully.");
        }
        customerRepository.save(customer);
        return R.success("Save successfully!");
    }

    @PostMapping("buy")
    public R buy(@RequestBody CustomerDto customerDto) {
        Long customerId = customerDto.getCustomerId();
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            return R.failed("Customer is not existed!");
        }
        Long bookId = customerDto.getBookId();
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            return R.failed("Book is not existed!");
        }
        Book book = bookOptional.get();
        Customer customer = customerOptional.get();
        Set<Book> books = customer.getBooks();
        boolean present = books.stream().anyMatch(b -> b.getId().equals(book.getId()));
        if (present) {
            return R.failed("Your repository had this book!");
        }
        customer.getBooks().add(book);
        customerRepository.save(customer);
        return R.success("Save successfully!");
    }

    @GetMapping("info/{id}")
    public R info(@PathVariable Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        boolean empty = customerOptional.isEmpty();
        if (empty) {
            return R.failed("Customer is not existed!");
        }
        return R.success("", customerOptional.get());
    }

    @GetMapping("page")
    public R page(@RequestParam(defaultValue = "0") int start,
                  @RequestParam(defaultValue = "10") int length) {
        PageRequest request = PageRequest.of(start, length);
        Page<Customer> page = customerRepository.findAll(request);
        return R.success("", page);
    }

    @GetMapping("delete/{id}")
    public R delete(@PathVariable Long id){
        Optional<Customer> customerOptional = customerRepository.findById(id);
        customerOptional.ifPresent(customerRepository::delete);
        return R.success("Delete successfully!");
    }

    @GetMapping("deleteBook")
    public R deleteBook(Long id, Long bookId){
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            return R.failed("Delete error. This customer is not exists.");
        }
        Customer dbCustomer = customerOptional.get();
        dbCustomer.getBooks().removeIf(b -> b.getId().equals(bookId));
        customerRepository.save(dbCustomer);
        return R.success("Delete successfully!");
    }

}
