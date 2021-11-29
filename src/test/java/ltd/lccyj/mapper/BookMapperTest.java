package ltd.lccyj.mapper;

import ltd.lccyj.domain.Book;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * BookMapper测试类
 * @author 刘淳
 */
@SpringBootTest
public class BookMapperTest {
    @Autowired
    private BookMapper bookMapper;

    @Test
    void testInsert(){
        Book book=new Book();
        book.setName("小王子");
        book.setType("童话");
        book.setDescription("启蒙故事，充满道理");
        int result = bookMapper.insert(book);
        System.out.println(result);
    }

    @Test
    void testQuery(){
        List<Book> books = bookMapper.selectList(null);
    }

}
