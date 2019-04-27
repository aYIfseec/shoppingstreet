package com.hyk.shoppingstreet.service.query;

import com.hyk.shoppingstreet.common.repository.mybatis.AbstractQuery;
import com.hyk.shoppingstreet.model.ShoppingCart;
import com.hyk.shoppingstreet.model.ShoppingCart.column;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example.Criteria;

@Data
@Builder
public class CartQuery extends AbstractQuery<ShoppingCart> {

  private List<Long> ids;

    private Long uid;
  private List<Long> uids;

    private Long commodityId;
    private Integer state;
  private List<Integer> stateList;

    @Override
    protected void addCondition(Criteria condition) {

      if (!CollectionUtils.isEmpty(ids)) {
        condition.andIn(column.id, ids);
      }

      if (!CollectionUtils.isEmpty(uids)) {
        condition.andIn(column.uid, uids);
      }

        if (uid != null) {
            condition.andEqualTo(column.uid, uid);
        }

      if (!CollectionUtils.isEmpty(stateList)) {
        condition.andIn(column.state, stateList);
      }

        if (state != null) {
            condition.andEqualTo(column.state, state);
        }

        if (commodityId != null) {
            condition.andEqualTo(column.commodityId, commodityId);
        }

    }
}
