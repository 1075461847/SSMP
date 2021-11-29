package ltd.lccyj.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * book表实体类
 *
 * @author 刘淳
 */
@Data
public class Book implements Serializable {
    @TableId("id")
    private Integer id;
    private String type;
    private String name;
    private String description;
}
