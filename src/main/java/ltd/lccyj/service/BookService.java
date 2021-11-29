package ltd.lccyj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import ltd.lccyj.domain.Book;

/**
 * Book实体类业务层接口
 *
 * @author 刘淳
 */
public interface BookService extends IService<Book> {

    /**
     * 分页查询
     *
     * @param currentPage 当前页码
     * @param pageSize    每页显示数量
     * @return 包含所有分页相关数据的Page对象
     */
    IPage<Book> getByPage(int currentPage, int pageSize);


    /**
     * 条件分页查询
     *
     * @param currentPage 当前页码
     * @param pageSize 每页显示数量
     * @param book 查询条件
     * @return 包含所有分页相关数据的Page对象
     */
    IPage<Book> getByPage(int currentPage, int pageSize,Book book);
}
