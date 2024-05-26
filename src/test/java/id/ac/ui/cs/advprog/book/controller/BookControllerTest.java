package id.ac.ui.cs.advprog.book.controller;

import id.ac.ui.cs.advprog.book.model.Book;
import id.ac.ui.cs.advprog.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBook() {
        Book book = new Book();
        CompletableFuture<Book> futureBook = CompletableFuture.completedFuture(book);
        when(bookService.saveBook(book)).thenReturn(futureBook);

        ResponseEntity<Book> responseEntity = bookController.saveBook(book).join();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(book, responseEntity.getBody());
        verify(bookService, times(1)).saveBook(book);
    }

    @Test
    void testGetBookById_WhenBookExists() {
        int idBook = 1;
        Book book = new Book();
        book.setIdBook(idBook);
        CompletableFuture<Optional<Book>> futureOptionalBook = CompletableFuture.completedFuture(Optional.of(book));
        when(bookService.getBookById(idBook)).thenReturn(futureOptionalBook);

        ResponseEntity<Book> responseEntity = bookController.getBookById(idBook).join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(book, responseEntity.getBody());
    }

    @Test
    void testGetBookById_WhenBookNotExists() {
        int idBook = 1;
        CompletableFuture<Optional<Book>> futureEmptyOptional = CompletableFuture.completedFuture(Optional.empty());
        when(bookService.getBookById(idBook)).thenReturn(futureEmptyOptional);

        ResponseEntity<Book> responseEntity = bookController.getBookById(idBook).join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());
        CompletableFuture<List<Book>> futureBooks = CompletableFuture.completedFuture(books);
        when(bookService.getAllBooks()).thenReturn(futureBooks);

        ResponseEntity<List<Book>> responseEntity = bookController.getAllBooks().join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(books, responseEntity.getBody());
    }

    @Test
    void testDeleteBook_WhenBookExists() {
        int idBook = 1;
        CompletableFuture<Void> futureVoid = CompletableFuture.completedFuture(null);
        when(bookService.deleteBook(idBook)).thenReturn(futureVoid);

        ResponseEntity<String> responseEntity = bookController.deleteBook(idBook).join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Book with id " + idBook + " has been deleted", responseEntity.getBody());
        verify(bookService, times(1)).deleteBook(idBook);
    }

    @Test
    void testDeleteBook_WhenBookNotExists() {
        int idBook = 1;
        doThrow(new NoSuchElementException()).when(bookService).deleteBook(idBook);

        ResponseEntity<String> responseEntity = bookController.deleteBook(idBook).join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Book with id " + idBook + " not found", responseEntity.getBody());
        verify(bookService, times(1)).deleteBook(idBook);
    }

    @Test
    void testDeleteBook_WhenExceptionOccurs() {
        int idBook = 1;
        doThrow(new RuntimeException()).when(bookService).deleteBook(idBook);

        ResponseEntity<String> responseEntity = bookController.deleteBook(idBook).join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to delete book with id " + idBook, responseEntity.getBody());
        verify(bookService, times(1)).deleteBook(idBook);
    }

    @Test
    void testGetBooksByAuthor() {
        String author = "Test Author";
        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());
        CompletableFuture<List<Book>> futureBooks = CompletableFuture.completedFuture(books);
        when(bookService.getBooksByAuthor(author)).thenReturn(futureBooks);

        ResponseEntity<List<Book>> responseEntity = bookController.getBooksByAuthor(author).join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(books, responseEntity.getBody());
    }

    @Test
    void testEditBook_WhenBookExists() {
        int idBook = 1;
        Book updatedBook = new Book();
        updatedBook.setIdBook(idBook);
        CompletableFuture<Book> futureEditedBook = CompletableFuture.completedFuture(updatedBook);
        when(bookService.editBook(idBook, updatedBook)).thenReturn(futureEditedBook);

        ResponseEntity<Book> responseEntity = bookController.editBook(idBook, updatedBook).join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedBook, responseEntity.getBody());
        verify(bookService, times(1)).editBook(idBook, updatedBook);
    }

    @Test
    void testEditBook_WhenBookNotExists() {
        int idBook = 1;
        Book updatedBook = new Book();
        updatedBook.setIdBook(idBook);
        CompletableFuture<Book> futureEmpty = CompletableFuture.completedFuture(null);
        when(bookService.editBook(idBook, updatedBook)).thenReturn(futureEmpty);

        ResponseEntity<Book> responseEntity = bookController.editBook(idBook, updatedBook).join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(bookService, times(1)).editBook(idBook, updatedBook);
    }

    @Test
    void testDecreaseStock_WhenBookExists() {
        int idBook = 1;
        int amount = 1;
        CompletableFuture<Void> futureVoid = CompletableFuture.completedFuture(null);
        when(bookService.decreaseStock(idBook, amount)).thenReturn(futureVoid);

        ResponseEntity<String> responseEntity = bookController.decreaseStock(idBook, amount).join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Stock of book with id " + idBook + " has been decreased by " + amount, responseEntity.getBody());
        verify(bookService, times(1)).decreaseStock(idBook, amount);
    }

    @Test
    void testDecreaseStock_WhenBookNotExists() {
        int idBook = 1;
        int amount = 1;
        doThrow(new NoSuchElementException()).when(bookService).decreaseStock(idBook, amount);

        ResponseEntity<String> responseEntity = bookController.decreaseStock(idBook, amount).join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Book with id " + idBook + " not found", responseEntity.getBody());
        verify(bookService, times(1)).decreaseStock(idBook, amount);
    }

    @Test
    void testDecreaseStock_WhenExceptionOccurs() {
        int idBook = 1;
        int amount = 1;
        doThrow(new RuntimeException()).when(bookService).decreaseStock(idBook, amount);

        ResponseEntity<String> responseEntity = bookController.decreaseStock(idBook, amount).join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to decrease stock of book with id " + idBook, responseEntity.getBody());
        verify(bookService, times(1)).decreaseStock(idBook, amount);
    }

    @Test
    void testIncreaseStock_WhenBookExists() {
        int idBook = 1;
        int amount = 1;
        CompletableFuture<Void> futureVoid = CompletableFuture.completedFuture(null);
        when(bookService.increaseStock(idBook, amount)).thenReturn(futureVoid);

        ResponseEntity<String> responseEntity = bookController.increaseStock(idBook, amount).join();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Stock of book with id " + idBook + " has been increased by " + amount, responseEntity.getBody());
        verify(bookService, times(1)).increaseStock(idBook, amount);
    }

    @Test
    void testIncreaseStock_WhenBookNotExists() {
        int idBook = 1;
        int amount = 1;
        doThrow(new NoSuchElementException()).when(bookService).increaseStock(idBook, amount);

        ResponseEntity<String> responseEntity = bookController.increaseStock(idBook, amount).join();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Book with id " + idBook + " not found", responseEntity.getBody());
        verify(bookService, times(1)).increaseStock(idBook, amount);
    }

    @Test
    void testIncreaseStock_WhenExceptionOccurs() {
        int idBook = 1;
        int amount = 1;
        doThrow(new RuntimeException()).when(bookService).increaseStock(idBook, amount);

        ResponseEntity<String> responseEntity = bookController.increaseStock(idBook, amount).join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to increase stock of book with id " + idBook, responseEntity.getBody());
        verify(bookService, times(1)).increaseStock(idBook, amount);
    }
}
