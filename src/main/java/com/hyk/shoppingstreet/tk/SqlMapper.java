package com.hyk.shoppingstreet.tk;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface SqlMapper<T> extends Mapper<T>, InsertListMapper<T> {
//    , SumMapper<T>
}

