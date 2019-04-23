package com.hyk.shoppingstreet.common.service;

import com.hyk.shoppingstreet.common.repository.Repository;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

@Slf4j
public abstract class AbstractService<I, T> {
    @Autowired(required = false)
    protected Repository<I, T> innerRepository;

    protected Repository<I, T> repository;

    @Getter
    private Type keyType;

    @Getter
    private Type modelType;

    public AbstractService() {
        Class subClass = getClass();
        while (!(subClass.getGenericSuperclass() instanceof ParameterizedType)
            && subClass.getSuperclass() != AbstractService.class) {
            subClass = subClass.getSuperclass();
        }

        keyType = ((ParameterizedType) subClass.getGenericSuperclass()).getActualTypeArguments()[0];
        modelType = ((ParameterizedType) subClass.getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected void initInnerRepository() {
    }

    @PostConstruct
    private void initRepository() {
        if (innerRepository == null) {
            initInnerRepository();
        }

        Objects.requireNonNull(innerRepository, "Initialize inner repository failed");

        repository = innerRepository;
    }

    public abstract List<T> findByQuery(Object query);

    public abstract List<T> findByQuery(Object query, String orderByClause);

    public T findOneByQuery(Object query) {
        List<T> itemList = findByQuery(query);
        return CollectionUtils.isEmpty(itemList) ? null : itemList.get(0);
    }

    public abstract List<T> findByQueryPage(Object query, String orderByClause, int pageNo, int pageSize);

    public List<T> findByQueryPage(Object query, int pageNo, int pageSize) {
        return findByQueryPage(query, null, pageNo, pageSize);
    }

    public int deleteById(I id) {
        return repository.deleteById(id);
    }

    public int deleteByIds(List<I> ids) {
        return repository.deleteByIds(ids);
    }

    public int deleteByQuery(Object query) {
        throw new UnsupportedOperationException();
    }

    public int updateById(T model) {
        return repository.update(model);
    }

    public int updateByIds(T model, List<I> ids) {
        return repository.updateByIds(ids, model);
    }

    public int updateByQuery(T model, Object query) {
        throw new UnsupportedOperationException();
    }

    public abstract int count();

    public abstract int countByQuery(Object query);

    public T getById(I id) {
        return repository.findById(id);
    }

    public List<T> getListByIds(List<I> ids) {
        return repository.findByIds(ids);
    }

    public int insert(T model) {
        return repository.insert(model);
    }

    public int insertList(List<T> models) {
        return repository.insert(models);
    }

    public Object insertIfNotExist(List<T> modelList) {
        throw new UnsupportedOperationException();
    }

    public Object insertIfNotExist(List<T> modelList, String... fieldName) {
        throw new UnsupportedOperationException();
    }
}