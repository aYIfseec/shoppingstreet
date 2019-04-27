package com.hyk.shoppingstreet.service;

import com.hyk.shoppingstreet.common.service.AbstractMapperService;
import com.hyk.shoppingstreet.common.utils.IdGenerator;
import com.hyk.shoppingstreet.dao.CommodityMapper;
import com.hyk.shoppingstreet.model.Commodity;
import com.hyk.shoppingstreet.service.query.CommodityQuery;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommodityService extends AbstractMapperService<Long, Commodity> {

    @Resource
    private IdGenerator idGenerator;
    @Resource
    private CommodityMapper commodityMapper;


    public List<Commodity> queryOnePage(String name, Integer pageNo, Integer pageSize) {
        return findByQueryPage(CommodityQuery.builder().name(name).build(), pageNo, pageSize);
//        return Lists.newArrayList();
    }

  public void updateStockBatch(List<Commodity> commodityList) {
    commodityMapper.updateStockBatch(commodityList);
  }
}
