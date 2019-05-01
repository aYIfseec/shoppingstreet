package com.hyk.shoppingstreet.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.hyk.shoppingstreet.common.Status;
import com.hyk.shoppingstreet.common.exception.BizException;
import com.hyk.shoppingstreet.common.service.AbstractMapperService;
import com.hyk.shoppingstreet.common.utils.IdGenerator;
import com.hyk.shoppingstreet.dao.TradeOrderMapper;
import com.hyk.shoppingstreet.model.Commodity;
import com.hyk.shoppingstreet.model.OrderDetail;
import com.hyk.shoppingstreet.model.ShoppingCart;
import com.hyk.shoppingstreet.model.TradeOrder;
import com.hyk.shoppingstreet.service.query.OrderQuery;
import com.hyk.shoppingstreet.service.vo.OrderVO;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @author huangyongkang
 * @since 2019-04-27
 */

@Slf4j
@Service
public class OrderService extends AbstractMapperService<Long, TradeOrder> {


  @Resource
  private IdGenerator idGen;
  @Resource
  private TradeOrderMapper orderMapper;
  @Resource
  private OrderDetailService orderDetailService;

  @Resource
  private CartService cartService;
  @Resource
  private CommodityService commodityService;


  public List<OrderVO> myList(Long uid) {
    List<OrderVO> res = Lists.newArrayList();

    OrderQuery query = OrderQuery.builder().uid(uid).build();
    List<TradeOrder> orders = findByQuery(query);

    if (CollectionUtils.isEmpty(orders)) {
      return res;
    }

    List<Long> orderIds = Lists.transform(orders, TradeOrder::getId);
    Multimap<Long, OrderDetail> orderDetailMap = orderDetailService
        .getOrderDetailMapByOrderIds(orderIds);

    orders.forEach(order -> {

      Collection<OrderDetail> value = orderDetailMap.get(order.getId());
      List<OrderDetail> orderDetails =
          CollectionUtils.isEmpty(value) ? Lists.newArrayList() : Lists.newArrayList(value);

      OrderVO orderVO = OrderVO.builder().orderDetailList(orderDetails).build();
      BeanUtils.copyProperties(order, orderVO);

      res.add(orderVO);
    });
    return res;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public Boolean add(TradeOrder order, List<Long> cartIds) {

    List<ShoppingCart> shoppingCarts = preCheckAndClearCart(order.getBuyer(), cartIds);

    if (CollectionUtils.isEmpty(shoppingCarts)) {
      throw BizException.create(Status.PARAM_ERROR);
    }

    Long orderId = idGen.nextId();

    List<Commodity> commodityList = preCheckAndReduceStock(shoppingCarts);

    List<OrderDetail> orderDetails = addBatch(orderId, shoppingCarts, commodityList);

    BigDecimal sum = new BigDecimal("0.00");
    for(OrderDetail in : orderDetails ){
      sum = sum.add(in.getAmount());
    }
    TradeOrder notPayOrder = TradeOrder.builder().build();
    BeanUtils.copyProperties(order, notPayOrder);
    notPayOrder.setId(orderId);
    notPayOrder.setAmount(sum);
    notPayOrder.setState(1);
    notPayOrder.setDeliveryWay(0L);
//    notPayOrder.setMemo("");
//    notPayOrder.setCreateTime(new Date());
//    notPayOrder.setModifyTime(new Date());
    orderMapper.insert(notPayOrder);
    return true;
  }

  private List<OrderDetail> addBatch(Long orderId, List<ShoppingCart> shoppingCarts,
      List<Commodity> commodityList) {
    Map<Long, Commodity> map = commodityList.stream()
        .collect(Collectors.toMap(Commodity::getId, Function.identity(), (v1, v2) -> v1));

    List<OrderDetail> orderDetails = Lists.newArrayList();
    shoppingCarts.forEach(in -> {
      Commodity commodity = map.get(in.getCommodityId());

      OrderDetail orderDetail = OrderDetail.builder()
          .orderId(orderId)
          .buyNum(in.getBuyNum())
          .commodityId(commodity.getId())
          .amount(commodity.getCurrentPrice().multiply(new BigDecimal(in.getBuyNum())))
          .build();

      orderDetails.add(orderDetail);
    });

    orderDetailService.insertList(orderDetails);
    return orderDetails;
  }


  public Boolean cancel(Long uid, Long orderId) {
    TradeOrder order = getById(orderId);
    if (order == null || order.getBuyer().equals(uid) == false) {
      throw BizException.create(Status.PARAM_ERROR);
    }

    return updateById(TradeOrder.builder().id(orderId).state(-1).modifyTime(new Date()).build()) > 0;
  }

  private List<ShoppingCart> preCheckAndClearCart(Long uid, List<Long> cartIds) {
    List<ShoppingCart> shoppingCarts = cartService.query(Lists.newArrayList(uid), cartIds);
    if (CollectionUtils.isEmpty(shoppingCarts)) {
      return Lists.newArrayList();
    }
    cartService.updateByIds(ShoppingCart.builder().state(2)
        .modifyTime(new Date()).build(), Lists.transform(shoppingCarts, ShoppingCart::getId));
    return shoppingCarts;
  }

  private List<Commodity> preCheckAndReduceStock(List<ShoppingCart> shoppingCarts) {
    List<Commodity> commodityList = commodityService
        .getListByIds(Lists.transform(shoppingCarts, ShoppingCart::getCommodityId));

    Map<Long, Commodity> map = commodityList.stream()
        .collect(Collectors.toMap(Commodity::getId, Function
            .identity(), (v1, v2) -> v1));

    List<String> noStock = Lists.newArrayList();

    shoppingCarts.forEach(in -> {
      Commodity commodity = map.get(in.getCommodityId());
      if (commodity.getStock() < in.getBuyNum()) {
        noStock.add(commodity.getName());
      } else {
        commodity.setStock(commodity.getStock() - in.getBuyNum());
      }
    });

    if (CollectionUtils.isEmpty(noStock) == false) {
      throw new BizException(-1, String.format("商品:[%s]库存不足!", noStock.toString()));
    }

    commodityService.updateStockBatch(commodityList);

    return commodityList;
  }
}
