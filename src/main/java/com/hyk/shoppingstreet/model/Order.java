package com.hyk.shoppingstreet.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 买家
     */
    private Long buyer;

  /**
   * 收货地址id
   */
  @Column(name = "address_id")
  private Long addressId;

  /**
   * 送货方式
   */
  @Column(name = "delivery_way")
  private Long deliveryWay;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 订单状态：-1失效，1未支付，2已支付
     */
    private Integer state;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

  private String memo;

  // order columns
    public static class column {
        public static final String id = "id";

        public static final String buyer = "buyer";

    public static final String addressId = "addressId";

    public static final String deliveryWay = "deliveryWay";

    public static final String amount = "amount";

        public static final String state = "state";

        public static final String createTime = "createTime";

        public static final String modifyTime = "modifyTime";

    public static final String memo = "memo";
  }
}