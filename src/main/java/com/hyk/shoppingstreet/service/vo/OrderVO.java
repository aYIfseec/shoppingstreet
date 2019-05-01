package com.hyk.shoppingstreet.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.hyk.shoppingstreet.model.OrderDetail;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huangyongkang
 * @since 2019-04-27
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {

  @JsonFormat(shape = Shape.STRING)
  private Long id;

  /**
   * 买家
   */
  @JsonFormat(shape = Shape.STRING)
  private Long buyer;

  /**
   * 收货地址id
   */
  @JsonFormat(shape = Shape.STRING)
  private Long addressId;

  /**
   * 送货方式
   */
  @JsonFormat(shape = Shape.STRING)
  private Long deliveryWay;

  /**
   * 交易金额
   */
  private BigDecimal amount;

  /**
   * 订单状态：-1失效，1未支付，2已支付
   */
  private Integer state;

  private Date createTime;

  private Date modifyTime;

  private String memo;


  private List<OrderDetail> orderDetailList;

}
