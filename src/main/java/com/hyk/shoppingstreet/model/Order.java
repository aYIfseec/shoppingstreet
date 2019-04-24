package com.hyk.shoppingstreet.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
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

    // order columns
    public static class column {
        public static final String id = "id";

        public static final String buyer = "buyer";

        public static final String amount = "amount";

        public static final String state = "state";

        public static final String createTime = "createTime";

        public static final String modifyTime = "modifyTime";
    }
}