package ru.springcourse.libraryApp2.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.springcourse.libraryApp2.dto.BookDTO;
import ru.springcourse.libraryApp2.model.Book;
import ru.springcourse.libraryApp2.service.BookService;

@RestController
/**
 * Review: api/v1/library - для того чтобы обеспечить версионность нашего API.
 */
@RequestMapping("/library")
public class BookController {

    private final BookService bookService;
    private final ModelMapper modelMapper;

    @Autowired
    public BookController(BookService bookService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public Page<BookDTO> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookService.findAllBooks(pageable).map(book -> modelMapper.map(book, BookDTO.class));
    }

    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable("id") int id) {
        return convertToBookDTO(bookService.findBookById(id));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> addNewBook(@RequestBody BookDTO bookDTO) {
        bookService.saveNewBook(bookDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/search")
    public Page<BookDTO> searchBookByPhrase(@RequestParam String phrase,
                                            @PageableDefault(size = 10, page = 0)
                                            Pageable pageable) {
        return bookService.findBookByPhrase(phrase, pageable).map(this::convertToBookDTO);
    }

    /**
     * Review: @PatchMapping - для обновления частично
     */
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateBook(@PathVariable("id") int id, @RequestBody BookDTO bookDTO) {
        bookService.updateBook(id, convertToBook(bookDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<HttpStatus> deleteBookByTitle(@PathVariable("title") String title) {
        boolean deleted = bookService.deleteBookByTitle(title);
        if (deleted) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/read/{id}")
    public ResponseEntity<HttpStatus> setReadAlready(@PathVariable("id") int id) {
        bookService.setReadAlready(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Book convertToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    private BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }
}
