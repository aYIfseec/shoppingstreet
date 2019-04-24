package com.hyk.shoppingstreet.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "order_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "commodity_id")
    private Long commodityId;

    private BigDecimal amount;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 购买数量
     */
    @Column(name = "buy_num")
    private Integer buyNum;

    // order_detail columns
    public static class column {
        public static final String id = "id";

        public static final String orderId = "orderId";

        public static final String commodityId = "commodityId";

        public static final String amount = "amount";

        public static final String createTime = "createTime";

        public static final String modifyTime = "modifyTime";

        public static final String buyNum = "buyNum";
    }
}