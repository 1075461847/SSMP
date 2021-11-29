package ltd.lccyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ltd.lccyj.domain.Book;
import ltd.lccyj.mapper.BookMapper;
import ltd.lccyj.service.BookService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Book实体类业务层实现类
 *
 * @author 刘淳
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    /**
     * 分页查询
     *
     * @param currentPage 当前页码
     * @param pageSize    每页显示数量
     * @return 包含所有分页相关数据的Page对象
     */
    @Override
    public IPage<Book> getByPage(int currentPage, int pageSize) {
        IPage<Book> page = new Page<>(currentPage, pageSize);
        bookMapper.selectPage(page, null);
        return page;
    }

    /**
     * 条件分页查询
     *
     * @param currentPage 当前页码
     * @param pageSize    每页显示数量
     * @param book        查询条件
     * @return 包含所有分页相关数据的Page对象
     */
    @Override
    public IPage<Book> getByPage(int currentPage, int pageSize, Book book) {
        IPage<Book> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Book> wrapper=new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(book.getType()),Book::getType,book.getType());
        wrapper.like(Strings.isNotEmpty(book.getName()),Book::getName,book.getName());
        wrapper.like(Strings.isNotEmpty(book.getDescription()),Book::getDescription,book.getDescription());
        bookMapper.selectPage(page,wrapper);
        return page;
    }
}
