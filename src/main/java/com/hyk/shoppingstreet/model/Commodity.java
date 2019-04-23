package com.hyk.shoppingstreet.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "commodity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Commodity {
    /**
     * ID
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 类目ID
     */
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 缩略图链接
     */
    @Column(name = "thumbs_url")
    private Long thumbsUrl;

    /**
     * 原价格
     */
    @Column(name = "origin_price")
    private BigDecimal originPrice;

    /**
     * 当前价格
     */
    @Column(name = "current_price")
    private BigDecimal currentPrice;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date updated;

    /**
     * 详情
     */
    private String detail;

    // commodity columns
    public static class column {
        public static final String id = "id";

        public static final String name = "name";

        public static final String categoryId = "categoryId";

        public static final String thumbsUrl = "thumbsUrl";

        public static final String originPrice = "originPrice";

        public static final String currentPrice = "currentPrice";

        public static final String created = "created";

        public static final String updated = "updated";

        public static final String detail = "detail";
    }
}