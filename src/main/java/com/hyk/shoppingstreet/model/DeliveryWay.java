package com.hyk.shoppingstreet.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "delivery_way")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryWay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "way_name")
    private String wayName;

    private String desc;

    private BigDecimal price;

    private Date created;

    private Date updated;

    // delivery_way columns
    public static class column {
        public static final String id = "id";

        public static final String wayName = "wayName";

        public static final String desc = "desc";

        public static final String price = "price";

        public static final String created = "created";

        public static final String updated = "updated";
    }
}