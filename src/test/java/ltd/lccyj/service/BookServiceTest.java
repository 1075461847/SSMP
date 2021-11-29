package ltd.lccyj.service;

import ltd.lccyj.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * BookService测试类
 * @author 刘淳
 */
@SpringBootTest
public class BookServiceTest {
    @Autowired
    private BookService bookService;
    @Test
    public void testList(){
        List<Book> books = bookService.list();
        for (Book book : books) {
            System.out.println(book);
        }
    }
}
