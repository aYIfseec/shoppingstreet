package com.hyk.shoppingstreet.service;

import com.hyk.shoppingstreet.common.service.AbstractMapperService;
import com.hyk.shoppingstreet.dao.DeliveryWayMapper;
import com.hyk.shoppingstreet.model.DeliveryWay;
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
public class DeliveryWayService extends AbstractMapperService<Long, DeliveryWay> {

  @Resource
  private DeliveryWayMapper deliveryWayMapper;

  public List<DeliveryWay> getAll() {
    return deliveryWayMapper.selectAll();
  }
}
