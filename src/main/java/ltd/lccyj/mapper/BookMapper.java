package ltd.lccyj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ltd.lccyj.domain.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * Book实体类Mapper接口
 *
 * @author 刘淳
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
