package com.hyk.shoppingstreet.service.query;

import com.hyk.shoppingstreet.common.repository.mybatis.AbstractQuery;
import com.hyk.shoppingstreet.model.ShoppingCart.column;
import com.hyk.shoppingstreet.model.TradeOrder;
import lombok.Builder;
import lombok.Data;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author huangyongkang
 * @since 2019-04-27
 */
@Data
@Builder
public class OrderQuery extends AbstractQuery<TradeOrder> {

  private Long uid;
  private Integer state;

  @Override
  protected void addCondition(Criteria condition) {

    if (uid != null) {
      condition.andEqualTo(column.uid, uid);
    }

    if (state != null) {
      condition.andEqualTo(column.state, state);
    }

  }
}
