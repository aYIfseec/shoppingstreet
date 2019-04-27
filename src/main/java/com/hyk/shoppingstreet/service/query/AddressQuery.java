package com.hyk.shoppingstreet.service.query;

import com.hyk.shoppingstreet.common.repository.mybatis.AbstractQuery;
import com.hyk.shoppingstreet.model.Address;
import com.hyk.shoppingstreet.model.Address.column;
import lombok.Builder;
import lombok.Data;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @author huangyongkang
 * @since 2019-04-27
 */
@Data
@Builder
public class AddressQuery extends AbstractQuery<Address> {

  private Long addressId;

  private Long uid;
  private Integer state;
  private Integer stateGte;

  @Override
  protected void addCondition(Criteria condition) {

    if (addressId != null) {
      condition.andEqualTo(column.id, addressId);
    }

    if (uid != null) {
      condition.andEqualTo(column.bindObject, uid);
    }

    if (state != null) {
      condition.andEqualTo(column.state, state);
    }

    if (stateGte != null) {
      condition.andGreaterThanOrEqualTo(column.state, stateGte);
    }

  }
}
