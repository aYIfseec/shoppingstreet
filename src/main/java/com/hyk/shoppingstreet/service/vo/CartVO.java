package com.hyk.shoppingstreet.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huangyongkang
 * @since 2019-04-24
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartVO {

  @JsonFormat(shape = Shape.STRING)
  private Long id;

  @JsonFormat(shape = Shape.STRING)
  private Long uid;

  @JsonFormat(shape = Shape.STRING)
  private Long commodityId;

  private Integer buyNum;


  /**
   * -1移除购物车，1正常展示，2已购买
   */
  private Integer state;

  private Date createTime;

  private Date modifyTime;

  /*******************/

  /**
   * 商品名称
   */
  private String name;

  /**
   * 库存
   */
  private Integer stock;

  /**
   * 类目ID
   */
  @JsonFormat(shape = Shape.STRING)
  private Long categoryId;

  /**
   * 缩略图链接
   */
  private String thumbsUrl;

  private BigDecimal currentPrice;

}
