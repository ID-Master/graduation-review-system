package cn.edu.cuhk.mkt.common.handler;

import com.uneed.common.core.exception.unchecked.BusinessException;
import com.uneed.common.support.api.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * @author taokai
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result bizExceptionHandler(HttpServletRequest request, BusinessException ex){
        log.error("发生业务异常！原因：{}", ex.getMessage());
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理空指针异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public Result nullExceptionHandler(HttpServletRequest request, NullPointerException ex){
        log.error("发生空指针异常！原因：{}", ex.getMessage(), ex);
        return Result.fail();
    }

    /**
     * 处理其他异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, Exception ex){
        log.error("未知异常！原因: {}", ex.getMessage(), ex);
        return Result.fail();
    }
}
