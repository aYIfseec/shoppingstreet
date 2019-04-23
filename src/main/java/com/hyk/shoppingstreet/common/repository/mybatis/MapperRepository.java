package com.hyk.shoppingstreet.common.repository.mybatis;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hyk.shoppingstreet.common.repository.Repository;
import com.hyk.shoppingstreet.tk.SqlMapper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Id;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

@Slf4j
public class MapperRepository<I, T> implements Repository<I, T> {

    private static final Integer MAX_RECORDS = 1024;

    @Autowired
    protected SqlMapper<T> mapper;

    @Getter
    private Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public MapperRepository() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) pt.getActualTypeArguments()[1];
    }

    public MapperRepository(Class<T> entityClass, SqlMapper<T> mapper) {
        this.entityClass = entityClass;
        this.mapper = mapper;
    }

    @Override
    public int insert(T model) {
        return mapper.insertSelective(model);
    }

    @Override
    public int insert(List<T> models) {
        return mapper.insertList(models);
    }

    @Override
    public int deleteById(I id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByIds(List<I> ids) {
        Example condition = idsCondition(ids);
        if (condition != null) {
            return mapper.deleteByExample(condition);
        }
        return 0;
    }

    @Override
    public int deleteByCondition(Object condition) {
        if (condition != null) {
            return mapper.deleteByExample(condition);
        }
        return 0;
    }

    @Override
    public int update(T model) {
        return mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public int updateByIds(List<I> ids, T model) {
        Example condition = idsCondition(ids);
        if (condition != null) {
            return mapper.updateByExampleSelective(model, condition);
        }
        return 0;
    }

    @Override
    public int updateByCondition(Object condition, T model) {
        if (condition != null) {
            return mapper.updateByExampleSelective(model, condition);
        }
        return 0;
    }

    @Override
    public T findById(I id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> findByIds(List<I> ids) {
        if (!CollectionUtils.isEmpty(ids) && ids.size() > MAX_RECORDS) {
            log.warn("findByIds with huge id set: size = {}", ids.size());
        }
        Example condition = idsCondition(ids);
        if (condition == null) {
            return Lists.newArrayList();
        }
        return findByCondition(condition);
    }

    public T findByIdForUpdate(I id) {
        List<T> results = findByIdsForUpdate(ImmutableList.of(id));
        return CollectionUtils.isEmpty(results) ? null : results.get(0);
    }

    public List<T> findByIdsForUpdate(List<I> ids) {
        if (!CollectionUtils.isEmpty(ids) && ids.size() > MAX_RECORDS) {
            log.warn("findByIds with huge id set: size = {}", ids.size());
        }
        Example condition = idsCondition(ids);
        if (condition == null) {
            return Lists.newArrayList();
        }
        condition.setForUpdate(true);
        return findByCondition(condition);
    }

    @Override
    public List<T> findByCondition(Object condition) {
        List<T> resultSet = mapper.selectByExample(condition);
        if (!CollectionUtils.isEmpty(resultSet) && resultSet.size() > MAX_RECORDS) {
            log.warn("findByCondition returns huge result set: size = {}", resultSet.size());
        }
        return resultSet;
    }

    @Override
    public List<T> findAll() {
        List<T> resultSet = mapper.selectAll();
        if (!CollectionUtils.isEmpty(resultSet) && resultSet.size() > MAX_RECORDS) {
            log.warn("findAll returns huge result set: size = {}", resultSet.size());
        }
        return resultSet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public I getId(T model) {
        Object id = Lists.newArrayList(entityClass.getDeclaredFields()).stream()
            .filter(f -> f.isAnnotationPresent(Id.class)).findFirst().map(f -> {
                try {
                    return entityClass.getMethod("get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1));
                } catch (NoSuchMethodException e2) {
                    return null;
                }
            }).filter(Objects::nonNull).map(m -> {
                try {
                    return m.invoke(model);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return null;
                }
            }).filter(Objects::nonNull).orElse(null);
        return (I)id;
    }

    private Example idsCondition(List<I> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }

        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        EntityColumn pkColumn = columnList.iterator().next();

        Example example = new Example(entityClass);
        example.createCriteria().andIn(pkColumn.getProperty(), ids);
        return example;
    }
}
