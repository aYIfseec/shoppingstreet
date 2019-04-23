package com.hyk.shoppingstreet.common;

import com.hyk.shoppingstreet.common.model.DaoContext;
import com.hyk.shoppingstreet.common.model.DaoContextThreadLocals;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnMsg<T> {

    private int resCode;
    private String resMsg;
    private T resData;
    private Integer totalCount;


    public static ReturnMsg createWithData(Object data) {
        ReturnMsg res = new ReturnMsg();
        res.setResCode(Status.SUCCESS.getCode());
        res.setResMsg(Status.SUCCESS.getDesc());
        res.setResData(data);
        DaoContext dalContext = DaoContextThreadLocals.getDalContext();
        if (dalContext != null) {
            res.setTotalCount(dalContext.getTotalCount());
        }
        return res;
    }

    public static ReturnMsg createWithoutTotalCount(Object data) {
        ReturnMsg res = new ReturnMsg();
        res.setResCode(Status.SUCCESS.getCode());
        res.setResMsg(Status.SUCCESS.getDesc());
        res.setResData(data);
        return res;
    }

    public static ReturnMsg createWithMsg(Status status) {
        ReturnMsg res = new ReturnMsg();
        res.setResCode(status.getCode());
        res.setResMsg(status.getDesc());
        return res;
    }


    public static ReturnMsg createWithData() {
        return createWithData(null);
    }

}
