package com.hyk.shoppingstreet.common.model;

public class DaoContextThreadLocals {
    private static ThreadLocal<DaoContext> dalContextTls = new ThreadLocal();

    public DaoContextThreadLocals() {
    }

    public static DaoContext getOrInitDalContext() {
        DaoContext val = (DaoContext)dalContextTls.get();
        if (val == null) {
            val = new DaoContext();
            dalContextTls.set(val);
        }

        return val;
    }

    public static DaoContext getDalContext() {
        return (DaoContext)dalContextTls.get();
    }

    public static void setDalContextTls(DaoContext dalContext) {
        dalContextTls.set(dalContext);
    }
}
