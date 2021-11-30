package ltd.lccyj.controller.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前后端数据协议
 *
 * @author 刘淳
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    /**
     * 结果标志
     */
    private boolean flag;
    /**
     * 数据
     */
    private Object data;
    /**
     * 消息
     */
    private String message;
}
