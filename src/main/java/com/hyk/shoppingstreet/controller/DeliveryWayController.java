package com.hyk.shoppingstreet.controller;

import com.hyk.shoppingstreet.common.ReturnMsg;
import com.hyk.shoppingstreet.model.DeliveryWay;
import com.hyk.shoppingstreet.service.DeliveryWayService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangyongkang
 * @since 2019-04-27
 */


@RestController
@RequestMapping(value = "/delivery")
public class DeliveryWayController {

  @Resource
  private DeliveryWayService deliveryWayService;

  @GetMapping()
  public ReturnMsg<List<DeliveryWay>> getAllWay() {
    return ReturnMsg.createWithData(deliveryWayService.getAll());
  }

}
