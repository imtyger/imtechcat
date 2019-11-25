package com.imtyger.imtygerbed.exception.handle;

import com.imtyger.imtygerbed.common.Result;
import com.imtyger.imtygerbed.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: imtygerx@gmail.com
 * @Date: 2019/11/24
 */

@Component
public class BusinessExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object object, Exception exception) {
        if (exception instanceof BusinessException) {
            return transformMV(((BusinessException) exception).getCode(), exception.getMessage());
        }
        if (exception instanceof IllegalArgumentException) {
            return transformMV(Result.FAIL.getValue(), exception.getMessage());
        }
        return null;
    }

    /**
     * 转换为ModelAndView
     *
     * @param code 异常状态码
     * @param msg  异常信息
     * @return ModelAndView
     */
    @SuppressWarnings("unchecked")
    private ModelAndView transformMV(Integer code, String msg) {
        Map<String, Object> map = new HashMap();
        map.put("code", code);
        map.put("message", msg);
        return new ModelAndView(new MappingJackson2JsonView(), map);
    }
}
