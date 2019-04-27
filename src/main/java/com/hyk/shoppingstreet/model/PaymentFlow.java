package com.hyk.shoppingstreet.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "payment_flow")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    private BigDecimal amount;

    @Column(name = "pay_way")
    private Integer payWay;

    private Long payer;

    @Column(name = "payer_name")
    private String payerName;

    private Long payee;

    @Column(name = "payee_name")
    private String payeeName;

    private Integer type;

    private Date created;

    private Date updated;

    // payment_flow columns
    public static class column {
        public static final String id = "id";

        public static final String orderId = "orderId";

        public static final String amount = "amount";

        public static final String payWay = "payWay";

        public static final String payer = "payer";

        public static final String payerName = "payerName";

        public static final String payee = "payee";

        public static final String payeeName = "payeeName";

        public static final String type = "type";

        public static final String created = "created";

        public static final String updated = "updated";
    }
}