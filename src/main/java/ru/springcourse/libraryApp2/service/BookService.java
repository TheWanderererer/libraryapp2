package ru.springcourse.libraryApp2.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springcourse.libraryApp2.dto.BookDTO;
import ru.springcourse.libraryApp2.model.Book;
import ru.springcourse.libraryApp2.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> findAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book findBookById(int id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElseThrow(() -> new EntityNotFoundException("Книга с таким id" + id + " не найдена"));
    }

    public Page<Book> findBookByPhrase(String phrase, Pageable pageable) {
        return bookRepository.findByDescriptionContainingIgnoreCase(phrase, pageable);
    }

    @Transactional
    public void updateBook(int id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void saveNewBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public boolean deleteBookByTitle(String title) {
        Optional<Book> book = bookRepository.findByTitle(title);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            return true;
        }
        return false;
    }

    @Transactional
    public void setReadAlready(int id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга с таким id " + id + " не найдена"));
        book.setReadAlready(true);
        bookRepository.save(book);
    }


}
