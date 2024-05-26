package id.ac.ui.cs.advprog.book.controller;

import id.ac.ui.cs.advprog.book.model.Book;
import id.ac.ui.cs.advprog.book.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Book>> saveBook(@RequestBody Book book) {
        return bookService.saveBook(book)
                .thenApply(savedBook -> new ResponseEntity<>(savedBook, HttpStatus.CREATED));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Book>> getBookById(@PathVariable("id") int idBook) {
        return bookService.getBookById(idBook)
                .thenApply(book -> book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Book>>> getAllBooks() {
        return bookService.getAllBooks()
                .thenApply(books -> new ResponseEntity<>(books, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> deleteBook(@PathVariable("id") int idBook) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                bookService.deleteBook(idBook);
                return new ResponseEntity<>("Book with id " + idBook + " has been deleted", HttpStatus.OK);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<>("Book with id " + idBook + " not found", HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to delete book with id " + idBook, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
    }

    @GetMapping("/author/{author}")
    public CompletableFuture<ResponseEntity<List<Book>>> getBooksByAuthor(@PathVariable("author") String author) {
        return bookService.getBooksByAuthor(author)
                .thenApply(books -> new ResponseEntity<>(books, HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Book>> editBook(@PathVariable("id") int idBook, @RequestBody Book updatedBook) {
        return bookService.editBook(idBook, updatedBook)
                .thenApply(editedBook -> editedBook != null ?
                        new ResponseEntity<>(editedBook, HttpStatus.OK) :
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/decreaseStock/{id}/{amount}")
    public CompletableFuture<ResponseEntity<String>> decreaseStock(@PathVariable("id") int idBook, @PathVariable("amount") int amount) {
        try {
            return bookService.decreaseStock(idBook, amount)
                    .thenApply(unused -> new ResponseEntity<>("Stock of book with id " + idBook + " has been decreased by " + amount, HttpStatus.OK));
        } catch (NoSuchElementException e) {
            return CompletableFuture.completedFuture(new ResponseEntity<>("Book with id " + idBook + " not found", HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new ResponseEntity<>("Failed to decrease stock of book with id " + idBook, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PutMapping("/increaseStock/{id}/{amount}")
    public CompletableFuture<ResponseEntity<String>> increaseStock(@PathVariable("id") int idBook, @PathVariable("amount") int amount) {
        try {
            return bookService.increaseStock(idBook, amount)
                    .thenApply(unused -> new ResponseEntity<>("Stock of book with id " + idBook + " has been increased by " + amount, HttpStatus.OK));
        } catch (NoSuchElementException e) {
            return CompletableFuture.completedFuture(new ResponseEntity<>("Book with id " + idBook + " not found", HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new ResponseEntity<>("Failed to increase stock of book with id " + idBook, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
