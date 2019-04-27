package com.hyk.shoppingstreet.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;


/**
 * @author huangyongkang
 * @since 2019-04-27
 */

@Data
@ApiModel
public class OrderAddRequest {

  @ApiModelProperty(value = "收货地址id")
  @NotNull(message = "addressId is null")
  private Long addressId;

  @ApiModelProperty(value = "送货方式")
  @NotNull(message = "deliveryWay is null")
  private Long deliveryWay;


  private String memo;


  @NotEmpty(message = "购物车商品 is Empty")
  private List<Long> cartIds;

}