package ltd.lccyj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ltd.lccyj.controller.utils.Result;
import ltd.lccyj.domain.Book;
import ltd.lccyj.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Book实体类表现层控制器
 *
 * @author 刘淳
 */
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * 获取所有书籍数据
     */
    @GetMapping
    public Result getAll() {
        return new Result(true, bookService.list(),null);
    }

    /**
     * 通过id查找书籍
     *
     * @param id 要查找书籍的id
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return new Result(true, bookService.getById(id),null);
    }


    /**
     * 分页条件查询
     *
     * @param currentPage 当前页码
     * @param pageSize  每页显示数量
     * @param book 查询条件
     */
    @GetMapping("/{currentPage}/{pageSize}")
    public Result getByPage(@PathVariable int currentPage, @PathVariable int pageSize, Book book) {
        IPage<Book> page = bookService.getByPage(currentPage, pageSize,book);
        while (page.getCurrent()>page.getPages()){
            //当前页码数大于总页码数时，使用总页码数再执行一次查询
            page = bookService.getByPage((int)page.getPages(), pageSize);
        }
        return new Result(true,page,null);
    }

    /**
     * 添加书籍
     *
     * @param book 要添加的书籍的数据
     */
    @PostMapping
    public Result save(@RequestBody Book book) {
        boolean flag = bookService.save(book);
        return new Result(flag, null,flag?"添加成功":"添加失败");
    }

    /**
     * 修改书籍
     *
     * @param book 要修改的书籍的数据
     */
    @PutMapping
    public Result update(@RequestBody Book book) {
        boolean flag = bookService.updateById(book);
        return new Result(flag, null,flag?"修改成功":"修改失败");
    }

    /**
     * 根据id删除书籍
     *
     * @param id 要删除的书籍的id
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean flag = bookService.removeById(id);
        return new Result(flag, null,flag?"删除成功":"删除失败");
    }


}
