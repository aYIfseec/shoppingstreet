package com.hyk.shoppingstreet.dao;

import com.hyk.shoppingstreet.model.Commodity;
import com.hyk.shoppingstreet.tk.SqlMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CommodityMapper extends SqlMapper<Commodity> {

  void updateStockBatch(@Param(value = "commodityList") List<Commodity> commodityList);
}