package com.hyk.shoppingstreet.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hyk.shoppingstreet.common.service.AbstractMapperService;
import com.hyk.shoppingstreet.common.utils.IdGenerator;
import com.hyk.shoppingstreet.dao.OrderDetailMapper;
import com.hyk.shoppingstreet.model.OrderDetail;
import com.hyk.shoppingstreet.service.query.OrderDetailQuery;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author huangyongkang
 * @since 2019-04-27
 */
@Slf4j
@Service
public class OrderDetailService extends AbstractMapperService<Long, OrderDetail> {

  @Resource
  private IdGenerator idGenerator;
  @Resource
  private OrderDetailMapper orderDetailMapper;

  public Multimap<Long, OrderDetail> getOrderDetailMapByOrderIds(List<Long> orders) {
    List<OrderDetail> orderDetails = findByQuery(
        OrderDetailQuery.builder().orderIdIn(orders).build());

    Multimap<Long, OrderDetail> map = HashMultimap.create();
    orderDetails.forEach(orderDetail -> {
      map.put(orderDetail.getOrderId(), orderDetail);
    });

    return map;
  }

}
