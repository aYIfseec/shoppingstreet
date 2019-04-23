package com.hyk.shoppingstreet.dao;

import com.hyk.shoppingstreet.model.UserAccount;
import com.hyk.shoppingstreet.tk.SqlMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserAccountMapper extends SqlMapper<UserAccount> {

    List<UserAccount> selectByPage(
        @Param(value = "offset") Integer offset,
        @Param(value = "pageSize") Integer pageSize);
}