package com.hyk.shoppingstreet.service.query;

import com.hyk.shoppingstreet.common.repository.mybatis.AbstractQuery;
import com.hyk.shoppingstreet.model.OrderDetail;
import com.hyk.shoppingstreet.model.OrderDetail.column;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author huangyongkang
 * @since 2019-04-27
 */
@Data
@Builder
public class OrderDetailQuery extends AbstractQuery<OrderDetail> {

  private Long orderId;
  private List<Long> orderIdIn;

  @Override
  protected void addCondition(Criteria condition) {

    if (orderId != null) {
      condition.andEqualTo(column.orderId, orderId);
    }

    if (!CollectionUtils.isEmpty(orderIdIn)) {
      condition.andIn(column.orderId, orderIdIn);
    }


  }
}
