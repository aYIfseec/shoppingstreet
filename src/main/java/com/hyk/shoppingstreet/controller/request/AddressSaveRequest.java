package com.hyk.shoppingstreet.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@ApiModel
public class AddressSaveRequest {

    @ApiModelProperty(value = "编辑时需要")
    private Long id;

    @ApiModelProperty(value = "联系人", required = true)
    @NotBlank(message = "kpName is Blank")
    private String kpName;

    @Length(min = 11, max = 11, message = "kpPhone error")
    private String kpPhone;

    private Integer province;

    private Integer city;

    private Integer area;

    @Length(min = 2, max = 128, message = "kpPhone error")
    private String detail;

    @NotNull(message = "postCode is null")
    private String postCode;

    @ApiModelProperty(value = "1正常使用地址，2默认使用地址", required = true)
    @Min(value = 0, message = "1正常使用地址，2默认使用地址")
    private Integer state;

}