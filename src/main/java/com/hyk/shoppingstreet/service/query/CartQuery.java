package com.hyk.shoppingstreet.service.query;

import com.hyk.shoppingstreet.common.repository.mybatis.AbstractQuery;
import com.hyk.shoppingstreet.model.ShoppingCart;
import com.hyk.shoppingstreet.model.ShoppingCart.column;
import lombok.Builder;
import lombok.Data;
import tk.mybatis.mapper.entity.Example.Criteria;

@Data
@Builder
public class CartQuery extends AbstractQuery<ShoppingCart> {

    private Long uid;
    private Long commodityId;
    private Integer state;

    @Override
    protected void addCondition(Criteria condition) {

        if (uid != null) {
            condition.andEqualTo(column.uid, uid);
        }

        if (state != null) {
            condition.andEqualTo(column.state, state);
        }

        if (commodityId != null) {
            condition.andEqualTo(column.commodityId, commodityId);
        }

    }
}
