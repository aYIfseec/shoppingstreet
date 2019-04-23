package com.hyk.shoppingstreet.common.utils;

import com.hyk.shoppingstreet.common.model.UserSession;
import org.springframework.stereotype.Component;

@Component
public class UserSessionThreadLocal {

    private static ThreadLocal<UserSession> userSessionThreadLocal = new ThreadLocal<>();

    public void putUserSession(UserSession userSession) {
        userSessionThreadLocal.set(userSession);
    }

    public void clear() {
        userSessionThreadLocal.remove();
    }

    public static UserSession getUserSession() {
        return userSessionThreadLocal.get();
    }
}
