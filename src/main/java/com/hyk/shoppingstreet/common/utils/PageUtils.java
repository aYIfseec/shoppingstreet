package com.hyk.shoppingstreet.common.utils;

import com.hyk.shoppingstreet.common.model.DaoContext;
import com.hyk.shoppingstreet.common.model.DaoContextThreadLocals;

public class PageUtils {
    public PageUtils() {
    }

    public static void cacheTotalCount(int totalCount) {
        DaoContext context = DaoContextThreadLocals.getOrInitDalContext();
        context.setTotalCount(totalCount);
        DaoContextThreadLocals.setDalContextTls(context);
    }

    public static int getTotalCount() {
        return DaoContextThreadLocals.getDalContext().getTotalCount();
    }
}
