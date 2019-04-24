package com.hyk.shoppingstreet.controller;

import com.hyk.shoppingstreet.common.ReturnMsg;
import com.hyk.shoppingstreet.common.Status;
import com.hyk.shoppingstreet.common.exception.BizException;
import com.hyk.shoppingstreet.common.model.UserSession;
import com.hyk.shoppingstreet.common.utils.ArgumentCheckUtil;
import com.hyk.shoppingstreet.common.utils.RedisUtil.TokenType;
import com.hyk.shoppingstreet.common.utils.UserSessionThreadLocal;
import com.hyk.shoppingstreet.service.UserService;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ReturnMsg<UserSession> register(
        @RequestParam(value = "phone", required = true) String phone,
        @RequestParam(value = "username", required = true) String username,
        @RequestParam(value = "pwd", required = true) String pwd
    ) {
        ArgumentCheckUtil.checkCellPhone(phone);
        ArgumentCheckUtil.checkNickName(username);
        ArgumentCheckUtil.checkPassword(pwd);

        boolean res = userService.register(phone, username, pwd);

        return ReturnMsg.createWithData(res);
    }

    @PostMapping("/loginStatusCheck")
    public ReturnMsg<UserSession> loginStatusCheck(
        @RequestParam(value = "uid", required = false) String uid,
        @RequestParam(value = "token", required = false) String token
    ) {
        if (uid == null) {
            return ReturnMsg.createWithoutTotalCount(false);
        }

        String redisToken = TokenType.USER_TOKEN.get(uid);
        if (null == token || !token.equals(redisToken)) {
            return ReturnMsg.createWithoutTotalCount(false);
        }
        return ReturnMsg.createWithoutTotalCount(true);
    }


    @PostMapping("/login")
    public ReturnMsg<UserSession> login(
        @RequestParam(value = "phone", required = true) String phone,
        @RequestParam(value = "pwd", required = true) String pwd
    ) {
        ArgumentCheckUtil.checkCellPhone(phone);
        ArgumentCheckUtil.checkPassword(pwd);

        UserSession userSession = userService.login(phone, pwd);
        if (userSession == null) {
            return ReturnMsg.createWithMsg(Status.NO_FOUND_USER_ERROR);
        }
        return ReturnMsg.createWithData(userSession);
    }


    @PostMapping("/logout")
    public ReturnMsg logout() {
        userService.logout(UserSessionThreadLocal.getUserSession().getUid());
        return ReturnMsg.createWithData();
    }


    @PostMapping("/edit")
    public ReturnMsg editInfo(
        @RequestParam(value = "username", required = false) String nickName,
        @RequestParam(value = "oldPwd", required = false) String oldPwd,
        @RequestParam(value = "newPwd", required = false) String newPwd
    ) {

        boolean allArgumentBlank = StringUtils.isBlank(nickName) &&
            (StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd));
        if (allArgumentBlank) {
            throw BizException.create(Status.PARAM_ERROR);
        }

        return ReturnMsg.createWithData(userService.editInfo(nickName, oldPwd, newPwd));
    }

}
