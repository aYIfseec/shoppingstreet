package com.hyk.shoppingstreet.common.model;

public class DaoContext {
    private int totalCount;

    public DaoContext() {
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DaoContext)) {
            return false;
        } else {
            DaoContext other = (DaoContext)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                return this.getTotalCount() == other.getTotalCount();
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof DaoContext;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getTotalCount();
        return result;
    }

    public String toString() {
        return "DaoContext(totalCount=" + this.getTotalCount() + ")";
    }
}
