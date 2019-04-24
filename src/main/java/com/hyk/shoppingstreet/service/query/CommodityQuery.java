package com.hyk.shoppingstreet.service.query;

import com.hyk.shoppingstreet.common.repository.mybatis.AbstractQuery;
import com.hyk.shoppingstreet.common.utils.StringUtil;
import com.hyk.shoppingstreet.model.Commodity;
import com.hyk.shoppingstreet.model.Commodity.column;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example.Criteria;

@Data
@Builder
public class CommodityQuery extends AbstractQuery<Commodity> {

    private String name;

    @Override
    protected void addCondition(Criteria condition) {

        if (StringUtils.isNotBlank(name)) {
            condition.andLike(column.name, StringUtil.like(name));
        }

    }
}
