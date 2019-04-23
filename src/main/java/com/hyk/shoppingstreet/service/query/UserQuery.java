package com.hyk.shoppingstreet.service.query;

import com.hyk.shoppingstreet.common.repository.mybatis.AbstractQuery;
import com.hyk.shoppingstreet.model.UserAccount;
import com.hyk.shoppingstreet.model.UserAccount.column;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example.Criteria;

@Data
@Builder
public class UserQuery extends AbstractQuery<UserAccount> {

    private String phone;

    @Override
    protected void addCondition(Criteria condition) {

        if (StringUtils.isNotBlank(phone)) {
            condition.andEqualTo(column.phone, phone);
        }

    }
}
