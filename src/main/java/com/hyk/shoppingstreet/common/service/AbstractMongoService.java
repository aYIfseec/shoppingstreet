package com.hyk.shoppingstreet.common.service;

import com.hyk.shoppingstreet.common.repository.mongo.MongoDatastoreRegistry;
import com.hyk.shoppingstreet.common.repository.mongo.MongoRepository;
import com.hyk.shoppingstreet.common.utils.PageUtils;
import com.mongodb.BulkWriteResult;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

public class AbstractMongoService<I, T> extends AbstractService<I, T> {
    @Autowired
    protected Datastore datastore;

    @Autowired
    private MongoDatastoreRegistry datastoreRegistry;

    public Boolean useObjectId() {
        return false;
    }

    public String datastoreKey() {
        return "default";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void initInnerRepository() {

        datastore = datastoreRegistry.getDatastore(datastoreKey());

        this.innerRepository = new MongoRepository<>(
            (Class<I>) getKeyType(), (Class<T>) getModelType(), datastore, useObjectId());
    }

    public UpdateOperations<T> initUpdateOps() {
        return ((MongoRepository<I, T>) innerRepository).initUpdateOps();
    }

    @Override
    public List<T> findByQuery(Object query) {
        return findByQuery((Query<T>) query);
    }

    @Override
    public List<T> findByQuery(Object query, String orderByClause) {
        return findByQuery((Query<T>) query, orderByClause);
    }

    @Override
    public List<T> findByQueryPage(Object query, String orderByClause, int pageNo, int pageSize) {
        return findByQuery((Query<T>) query, orderByClause, pageNo, pageSize);
    }

    @SuppressWarnings("unchecked")
    public Query<T> query() {
        return datastore.createQuery((Class<T>) getModelType());
    }

    public List<T> findByQuery(Query<T> query) {
        return findByQuery(query, null);
    }

    public List<T> findByQuery(Query<T> query, String orderByClause) {
        return findByQuery(query, orderByClause, 0, 0);
    }

    public List<T> findByQuery(Query<T> query, String orderByClause, int offset, int limit) {
        FindOptions options = null;
        if (StringUtils.isNotBlank(orderByClause)) {
            query = query.order(orderByClause);
        }
        if (limit > 0) {
            options = new FindOptions().skip(offset).limit(limit);
        }
        return ((MongoRepository<I, T>) innerRepository).findByConditionWithOption(query, options);
    }

    public T findOneByQuery(Query<T> query) {
        List<T> itemList = findByQuery(query);
        return CollectionUtils.isEmpty(itemList) ? null : itemList.get(0);
    }

    public List<T> findByQueryPage(Query<T> query, String orderByClause, int pageNo, int pageSize) {
        int totalCount = countByQuery(query);
        List<T> itemList = findByQuery(query, orderByClause, pageNo * pageSize, pageSize);
        PageUtils.cacheTotalCount(totalCount);
        return itemList;
    }

    public List<T> findByQueryPage(Query<T> query, int pageNo, int pageSize) {
        return findByQueryPage(query, null, pageNo, pageSize);
    }

    protected int update(I id, Map<String, Object> ups, boolean createIfMissing) {
        Class<T> modelClass = (Class<T>) getModelType();
        UpdateOperations<T> ops = datastore
            .createUpdateOperations(modelClass);

        for (Map.Entry<String, Object> up : ups.entrySet()) {
            ops.set(up.getKey(), up.getValue());
        }

        UpdateResults updateResults = datastore.update(datastore.createQuery(modelClass).field("_id").equal(id), ops, createIfMissing);

        return updateResults.getUpdatedCount();
    }

    public int updateByIds(List<I> ids, Map<String, Object> ups, boolean createIfMissing) {
        Class<T> modelClass = (Class<T>) getModelType();
        UpdateOperations<T> ops = datastore
            .createUpdateOperations(modelClass);

        for (Map.Entry<String, Object> up : ups.entrySet()) {
            ops.set(up.getKey(), up.getValue());
        }
        UpdateResults results = datastore.update(datastore.createQuery(modelClass)
            .field("_id").in(ids), ops, createIfMissing);

        return results.getUpdatedCount();
    }

    public int updateByIds(List<I> ids, UpdateOperations<T> ops) {
        int result = ((MongoRepository<I, T>) innerRepository).updateByIds(ids, ops);

        return result;
    }

    public int updateByQuery(Query<T> query, UpdateOperations<T> ops) {

            return ((MongoRepository<I, T>) innerRepository).updateByCondition(query, ops);
    }

    public int deleteByQuery(Query<T> query) {
        return repository.deleteByCondition(query);
    }

    public int updateByQuery(T model, Query<T> query) {
        return repository.updateByCondition(query, model);
    }

    @SuppressWarnings("unchecked")
    public int count() {
        return ((MongoRepository<I, T>) innerRepository).count().intValue();
    }

    @Override
    public int countByQuery(Object query) {
        return countByQuery((Query<T>) query);
    }

    public int countByQuery(Query<T> query) {
        return ((MongoRepository<I, T>) innerRepository).countByCondition(query).intValue();
    }

    @Override
    public BulkWriteResult insertIfNotExist(List<T> modelList) {
        return ((MongoRepository<I, T>) innerRepository).insertIfNotExist(modelList);
    }

    @Override
    public BulkWriteResult insertIfNotExist(List<T> modelList, String... fieldName) {
        return ((MongoRepository<I, T>) innerRepository).insertIfNotExist(modelList, fieldName);
    }

    public BulkWriteResult insertIfNotExist(T model, Map clue) {
        return ((MongoRepository<I, T>) innerRepository).insertIfNotExist(model, clue);
    }
}
