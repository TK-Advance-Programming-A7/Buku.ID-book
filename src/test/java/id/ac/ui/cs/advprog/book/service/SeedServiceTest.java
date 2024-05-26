package id.ac.ui.cs.advprog.book.service;

import id.ac.ui.cs.advprog.book.model.Book;
import id.ac.ui.cs.advprog.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SeedServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private SeedService seedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSeed() {

        int numberOfBooks = 100;
        seedService.seed();
        verify(bookRepository, times(numberOfBooks)).save(any(Book.class));
    }
}
