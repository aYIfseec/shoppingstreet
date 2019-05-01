package com.hyk.shoppingstreet.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.util.Date;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressVO {
    @JsonFormat(shape = Shape.STRING)
    private Long id;

    /**
     * 地址绑定的对象
     */
    @JsonFormat(shape = Shape.STRING)
    private Long bindObject;

    /**
     * 联系人
     */
    private String kpName;

    /**
     * 联系电话
     */
    @Column(name = "kp_phone")
    private String kpPhone;

    private Integer province;

    private Integer city;

    private Integer area;

    private String detail;

    private String postCode;

    /**
     * -1删除，1正常使用地址，2默认使用地址
     */
    private Integer state;

    private Date createTime;

    private Date modifyTime;

    // address columns
    public static class column {
        public static final String id = "id";

        public static final String bindObject = "bindObject";

        public static final String kpName = "kpName";

        public static final String kpPhone = "kpPhone";

        public static final String province = "province";

        public static final String city = "city";

        public static final String area = "area";

        public static final String detail = "detail";

        public static final String postCode = "postCode";

        public static final String state = "state";

        public static final String createTime = "createTime";

        public static final String modifyTime = "modifyTime";
    }
}