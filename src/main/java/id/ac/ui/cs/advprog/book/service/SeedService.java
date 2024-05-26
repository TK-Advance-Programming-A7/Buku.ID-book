 package id.ac.ui.cs.advprog.book.service;

 import java.util.Locale;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 import net.datafaker.Faker;

 import id.ac.ui.cs.advprog.book.model.BookBuilder;
 import id.ac.ui.cs.advprog.book.model.Book;
 import id.ac.ui.cs.advprog.book.repository.BookRepository;

 @Service
 public class SeedService {
     @Autowired
     private BookRepository bookRepository;
     private static final int NUMBER_OF_BOOK = 100;

     public void seed() {
         @SuppressWarnings("deprecation")
         Faker faker = new Faker(new Locale("id_ID"));

         for (int i = 0; i < NUMBER_OF_BOOK; i++) {
             String title = faker.book().title();
             String author = faker.book().author();
             String publisher = faker.book().publisher();
             float price = faker.random().nextFloat() * (100000 - 10000) + 10000;
             int stock = faker.number().numberBetween(1, 100);
             String isbn = faker.number().digits(13);
             String bookPict = faker.internet().url();
             String category = faker.book().genre();
             int page = faker.number().numberBetween(100, 1000);
             String desc = faker.lorem().paragraph();

             // Potong data jika lebih dari 255 karakter
             if (desc.length() > 255) {
                 desc = desc.substring(0, 255);
             }

             Book book = new BookBuilder()
                     .setTitle(title)
                     .setAuthor(author)
                     .setPublisher(publisher)
                     .setPrice(price)
                     .setStock(stock)
                     .setIsbn(isbn)
                     .setBookPict(bookPict)
                     .setCategory(category)
                     .setPage(page)
                     .setDesc(desc)
                     .build();

             bookRepository.save(book);
         }
     }
 }
