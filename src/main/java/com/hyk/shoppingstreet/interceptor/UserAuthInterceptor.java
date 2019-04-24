package com.hyk.shoppingstreet.interceptor;

import com.alibaba.fastjson.JSON;
import com.hyk.shoppingstreet.common.ReturnMsg;
import com.hyk.shoppingstreet.common.Status;
import com.hyk.shoppingstreet.common.model.UserSession;
import com.hyk.shoppingstreet.common.utils.RedisUtil.TokenType;
import com.hyk.shoppingstreet.model.UserAccount;
import com.hyk.shoppingstreet.common.utils.UserSessionThreadLocal;
import com.hyk.shoppingstreet.service.UserService;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Slf4j
@Component
public class UserAuthInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserSessionThreadLocal userSessionThreadLocal;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        Object o
    )  throws Exception {

        // 虽然在InterceptorConfig中按后缀放过了静态资源，但不知道为什么还有一部分js会进来
        // 只好这里return true
        if (o instanceof ResourceHttpRequestHandler) {
            return true;
        }

        String token = httpServletRequest.getParameter("token");
        String uidStr = httpServletRequest.getParameter("uid");
        Long uid;

        try {
            uid = Long.parseLong(uidStr);
        } catch (NumberFormatException e) {
            log.warn("uid {} NumberFormatException {} ", uidStr, e.getMessage());
            genTokenFailReturnResult(httpServletResponse);
            return false;
        }

        log.info(String.format("\n\n uid %s   token %s   QueryString %s \n\n", uidStr, token, httpServletRequest.getQueryString()));

        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uidStr)) {
            genTokenFailReturnResult(httpServletResponse);
            return false;
        }

        String redisToken = TokenType.USER_TOKEN.get(uidStr);
        if (null == redisToken || !redisToken.equals(token)) {
            genTokenFailReturnResult(httpServletResponse);
            return false;
        }
        UserAccount userAccount = userService.getById(uid);
        userSessionThreadLocal.putUserSession(
            UserSession.builder().token(token).uid(userAccount.getUid())
                .username(userAccount.getUsername()).build());
        return true;
    }

    private void genTokenFailReturnResult(HttpServletResponse httpServletResponse)
        throws IOException {
        ReturnMsg resultMsg = new ReturnMsg();
        resultMsg.setResCode(Status.TOKEN_ERROR.getCode());
        resultMsg.setResMsg(Status.TOKEN_ERROR.getDesc());
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        httpServletResponse.getWriter().print(JSON.toJSONString(resultMsg));
    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        userSessionThreadLocal.clear();
        log.info(String.format("\n\nresponse %s\n\n", httpServletResponse.toString()));
    }
}
