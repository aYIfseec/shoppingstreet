package com.hyk.shoppingstreet.common.service;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.hyk.shoppingstreet.common.repository.mybatis.AbstractQuery;
import com.hyk.shoppingstreet.common.repository.mybatis.MapperRepository;
import com.hyk.shoppingstreet.common.utils.PageUtils;
import com.hyk.shoppingstreet.tk.SqlMapper;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

public class AbstractMapperService<I, T> extends AbstractService<I, T> {
    @Autowired
    protected SqlMapper<T> mapper;

    @Autowired
    protected TransactionTemplate transactionTemplate;

    @Override
    @SuppressWarnings("unchecked")
    protected void initInnerRepository() {
        this.innerRepository = new MapperRepository<>((Class<T>) getModelType(), mapper);
    }

    @Override
    public List<T> findByQuery(Object query) {
        return findByQuery((AbstractQuery<T>) query);
    }

    @Override
    public List<T> findByQuery(Object query, String orderByClause) {
        return findByQuery((AbstractQuery<T>) query, orderByClause);
    }

    @Override
    public List<T> findByQueryPage(Object query, String orderByClause, int pageNo, int pageSize) {
        return findByQueryPage((AbstractQuery<T>) query, orderByClause, pageNo, pageSize);
    }

    public List<T> findByQueryForUpdate(AbstractQuery<T> query) {
        return findByQuery(query, true, null);
    }

    public List<T> findByQuery(AbstractQuery<T> query) {
        return findByQuery(query, null);
    }

    public List<T> findByQuery(AbstractQuery<T> query, String orderByClause) {
        return findByQuery(query, false, orderByClause);
    }

    public List<T> findByQuery(AbstractQuery<T> query, boolean forUpdate, String orderByClause) {
        Example condition = query.toExample();
        condition.setForUpdate(forUpdate);
        if (StringUtils.isNotBlank(orderByClause)) {
            condition.setOrderByClause(orderByClause);
        }
        return repository.findByCondition(condition);
    }

    public T findOneByQuery(AbstractQuery<T> query) {
        List<T> itemList = findByQuery(query);
        return CollectionUtils.isEmpty(itemList) ? null : itemList.get(0);
    }

    public T findOneByQueryForUpdate(AbstractQuery<T> query) {
        List<T> itemList = findByQueryForUpdate(query);
        return CollectionUtils.isEmpty(itemList) ? null : itemList.get(0);
    }

    public List<T> findByQueryPage(AbstractQuery<T> query, String orderByClause, int pageNo, int pageSize) {
        query.setPage(pageNo, pageSize);
        List<T> itemList = findByQuery(query, orderByClause);
        PageUtils.cacheTotalCount((int) ((Page) itemList).getTotal());
        return itemList;
    }

    public List<T> findByQueryPage(AbstractQuery<T> query, int pageNo, int pageSize) {
        return findByQueryPage(query, null, pageNo, pageSize);
    }


    public int deleteByQuery(AbstractQuery<T> query) {

        final Object result = transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                List<T> toDeletes = findByQueryForUpdate(query);
                if (!CollectionUtils.isEmpty(toDeletes)) {
                    List<I> deleteIds = Lists.transform(toDeletes, repository::getId);
                    deleteByIds(deleteIds);
                }
            }
        });
        return Objects.isNull(result) ? 0 : 1;
    }

    public int updateByQuery(T model, AbstractQuery<T> query) {

        Object obj = transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                List<T> toUpdates = findByQueryForUpdate(query);
                if (!CollectionUtils.isEmpty(toUpdates)) {
                    updateByIds(model, Lists.transform(toUpdates, repository::getId));
                }
            }
        });
        return Objects.isNull(obj) ? 0 : 1;
    }

    @SuppressWarnings("unchecked")
    public int count() {
        return mapper.selectCountByExample(new Example((Class<T>) getModelType()));
    }

    @Override
    public int countByQuery(Object query) {
        return countByQuery((AbstractQuery<T>) query);
    }

    public int countByQuery(AbstractQuery<T> query) {
        return mapper.selectCountByExample(query.toExample());
    }

    public int countDistinctByQuery(String countProperty, AbstractQuery<T> query) {
        Example example = query.toExample();
        example.setCountProperty(countProperty);
        example.setDistinct(true);

        return mapper.selectCountByExample(example);
    }

    public T getByIdForUpdate(I id) {
        return ((MapperRepository<I, T>)innerRepository).findByIdForUpdate(id);
    }

    public List<T> getListByIdsForUpdate(List<I> ids) {
        return ((MapperRepository<I, T>)innerRepository).findByIdsForUpdate(ids);
    }
}