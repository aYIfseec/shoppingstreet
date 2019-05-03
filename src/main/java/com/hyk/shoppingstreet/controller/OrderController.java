package com.hyk.shoppingstreet.controller;

import com.hyk.shoppingstreet.common.ReturnMsg;
import com.hyk.shoppingstreet.common.utils.UserSessionThreadLocal;
import com.hyk.shoppingstreet.controller.request.OrderAddRequest;
import com.hyk.shoppingstreet.model.TradeOrder;
import com.hyk.shoppingstreet.service.OrderService;
import com.hyk.shoppingstreet.service.vo.OrderVO;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author huangyongkang
 * @since 2019-04-27
 */

@RestController
@RequestMapping(value = "/order")
public class OrderController {

  @Resource
  private OrderService orderService;

  @PostMapping("/list")
  public ReturnMsg<List<OrderVO>> myList(
  ) {

    Long uid = UserSessionThreadLocal.getUserSession().getUid();

    List<OrderVO> res = orderService.myList(uid);

    return ReturnMsg.createWithData(res);
  }


  @PostMapping("/add")
  public ReturnMsg<String> add(
      @RequestBody OrderAddRequest request
  ) {
    request.check();

    Long uid = UserSessionThreadLocal.getUserSession().getUid();
    TradeOrder order = TradeOrder.builder().buyer(uid).build();
    BeanUtils.copyProperties(request, order);
    Long res = orderService.add(order, request.getCartIds());

    return ReturnMsg.createWithoutTotalCount(res.toString());
  }


  @PostMapping("/cancel")
  public ReturnMsg<Boolean> cancel(
      @RequestParam(value = "orderId", required = true) Long orderId
  ) {
    Long uid = UserSessionThreadLocal.getUserSession().getUid();
    Boolean res = orderService.cancel(uid, orderId);

    return ReturnMsg.createWithoutTotalCount(res);
  }

}
