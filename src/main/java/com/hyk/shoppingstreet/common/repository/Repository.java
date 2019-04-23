package com.hyk.shoppingstreet.common.repository;

import java.util.List;

public interface Repository<I, T> {

    int insert(T var1);

    int insert(List<T> var1);

    int deleteById(I var1);

    int deleteByIds(List<I> var1);

    int deleteByCondition(Object var1);

    int update(T var1);

    int updateByIds(List<I> var1, T var2);

    int updateByCondition(Object var1, T var2);

    T findById(I var1);

    List<T> findByIds(List<I> var1);

    List<T> findByCondition(Object var1);

    List<T> findAll();

    I getId(T var1);
}
