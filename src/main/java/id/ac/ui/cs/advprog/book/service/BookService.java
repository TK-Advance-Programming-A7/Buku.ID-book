package id.ac.ui.cs.advprog.book.service;

import id.ac.ui.cs.advprog.book.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public interface BookService {
    CompletableFuture<Book> saveBook(Book book);
    CompletableFuture<Optional<Book>> getBookById(int idBook);
    CompletableFuture<List<Book>> getAllBooks();
    CompletableFuture<Void> deleteBook(int idBook);
    CompletableFuture<List<Book>> getBooksByAuthor(String author);
    CompletableFuture<Book> editBook(int idBook, Book updatedBook);
    CompletableFuture<Void> decreaseStock(int idBook, int amount);
    CompletableFuture<Void> increaseStock(int idBook, int amount);
}