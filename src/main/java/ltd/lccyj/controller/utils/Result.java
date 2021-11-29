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
    private boolean flag;  // 结果标志
    private Object data;  //数据
    private String message; //消息
}
