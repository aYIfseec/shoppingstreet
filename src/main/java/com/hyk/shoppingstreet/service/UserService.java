package com.hyk.shoppingstreet.service;

import com.hyk.shoppingstreet.common.Status;
import com.hyk.shoppingstreet.common.exception.BizException;
import com.hyk.shoppingstreet.common.model.UserSession;
import com.hyk.shoppingstreet.common.service.AbstractMapperService;
import com.hyk.shoppingstreet.common.utils.IdGenerator;
import com.hyk.shoppingstreet.common.utils.PasswordHash;
import com.hyk.shoppingstreet.common.utils.RedisUtil.TokenType;
import com.hyk.shoppingstreet.common.utils.UUIDUtil;
import com.hyk.shoppingstreet.common.utils.UserSessionThreadLocal;
import com.hyk.shoppingstreet.dao.UserAccountMapper;
import com.hyk.shoppingstreet.model.UserAccount;
import com.hyk.shoppingstreet.service.query.UserQuery;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService extends AbstractMapperService<Long, UserAccount> {

    @Resource
    private IdGenerator idGenerator;
    @Resource
    private UserAccountMapper userAccountMapper;

    public boolean register(String phone, String nickName, String pwd) {
        Date now = new Date();
        String salt = UUIDUtil.uuid();
        pwd = PasswordHash.hash256(pwd + salt);

        try {
            return insert(UserAccount
                .builder()
                .uid(idGenerator.nextId())
                .phone(phone)
                .username(nickName)
                .pwd(pwd)
                .salt(salt)
                .createTime(now)
                .modifyTime(now)
                .build()) > 0;
        } catch (DuplicateKeyException duplicateKeyException) {
            throw BizException.create(Status.REGISTER_ERROR);
        }
    }

    public UserSession login(String phone, String pwd) {
        UserAccount user = findOneByQuery(UserQuery.builder().phone(phone).build());
        if (user == null) {
            throw BizException.create(Status.NO_FOUND_USER_ERROR);
        }
        pwd = PasswordHash.hash256(pwd + user.getSalt());
        if (user != null && user.getPwd().equals(pwd)) {
            return generateUserSession(user);
        }
        return null;
    }

    private UserSession generateUserSession(UserAccount userAccount) {
        String token = UUIDUtil.uuid();
        TokenType.USER_TOKEN.set(userAccount.getUid().toString(), token); // 保持登录状态
        return UserSession
            .builder()
            .userAccount(userSecurityInfoClean(userAccount))
            .token(token)
            .build();
    }

    private UserAccount userSecurityInfoClean(UserAccount userAccount) {
        userAccount.setSalt(null);
        userAccount.setPwd(null);
        userAccount.setPhone(null);
        userAccount.setCreateTime(null);
        userAccount.setModifyTime(null);
        return userAccount;
    }

    public void logout(Long uid) {
        TokenType.USER_TOKEN.del(uid.toString());
    }

    public boolean editInfo(String nickName, String oldPwd, String newPwd) {
        UserAccount userAccount = UserSessionThreadLocal.getUserSession().getUserAccount();
        UserAccount newUserInfo = UserAccount.builder().uid(userAccount.getUid()).username(nickName).build();

        if (StringUtils.isNotBlank(oldPwd) && StringUtils.isNotBlank(newPwd)) {
            boolean oldPwdCheck = userAccount.getPwd()
                .equals(PasswordHash.hash256(oldPwd + userAccount.getSalt()));
            if (oldPwdCheck == false) {
                return false;
            }


            String salt = UUIDUtil.uuid();
            newPwd = PasswordHash.hash256(newPwd + salt);

            newUserInfo.setSalt(salt);
            newUserInfo.setPwd(newPwd);
        }

        updateById(newUserInfo);
        return true;
    }

}
