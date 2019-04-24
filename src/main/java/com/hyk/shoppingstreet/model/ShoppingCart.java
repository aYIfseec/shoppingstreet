package com.hyk.shoppingstreet.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "shopping_cart")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long uid;

    @Column(name = "commodity_id")
    private Long commodityId;

    @Column(name = "buy_num")
    private Integer buyNum;

    private BigDecimal amount;

    /**
     * -1移除购物车，1正常展示，2已购买
     */
    private Integer state;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    // shopping_cart columns
    public static class column {
        public static final String id = "id";

        public static final String uid = "uid";

        public static final String commodityId = "commodityId";

        public static final String buyNum = "buyNum";

        public static final String amount = "amount";

        public static final String state = "state";

        public static final String createTime = "createTime";

        public static final String modifyTime = "modifyTime";
    }
}