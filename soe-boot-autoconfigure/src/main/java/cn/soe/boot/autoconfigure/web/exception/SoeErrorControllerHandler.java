package cn.soe.boot.autoconfigure.web.exception;

import cn.soe.boot.core.bean.rest.ReturnT;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * http错误统一处理，404等
 * @see org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
 * @author xiezhenxiang 2023/4/23
 */
@Hidden
@RestController
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class SoeErrorControllerHandler implements ErrorController {

    @RequestMapping
    public Object handleError(HttpServletRequest request){
        Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        return ReturnT.error(code, uri);
    }
}
