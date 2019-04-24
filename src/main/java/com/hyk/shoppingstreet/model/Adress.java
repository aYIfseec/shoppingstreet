package com.hyk.shoppingstreet.model;

import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "adress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Adress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 地址绑定的对象
     */
    @Column(name = "bind_object")
    private Long bindObject;

    private Integer province;

    private Integer city;

    private Integer area;

    private String detail;

    /**
     * -1删除，1正常使用地址，2默认使用地址
     */
    private Integer state;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    // adress columns
    public static class column {
        public static final String id = "id";

        public static final String bindObject = "bindObject";

        public static final String province = "province";

        public static final String city = "city";

        public static final String area = "area";

        public static final String detail = "detail";

        public static final String state = "state";

        public static final String createTime = "createTime";

        public static final String modifyTime = "modifyTime";
    }
}