package com.hyk.shoppingstreet.controller.request;

import com.hyk.shoppingstreet.common.Status;
import com.hyk.shoppingstreet.common.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.util.CollectionUtils;


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

//  @ApiModelProperty(value = "送货方式")
//  @NotNull(message = "deliveryWay is null")
//  private Long deliveryWay;


  private String memo;


  @NotEmpty(message = "购物车商品 is Empty")
  private List<Long> cartIds;

  public void check(){
    if (addressId == null) {
      throw new BizException(-1, "收货地址不能为空，请选择收货地址，没有请创建");
    }

    if (CollectionUtils.isEmpty(cartIds)) {
      throw new BizException(-1, "没有选购商品，无法创建订单");
    }
  }

}