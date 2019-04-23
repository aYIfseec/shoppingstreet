package com.hyk.shoppingstreet.common.repository.mongo;

import com.google.common.collect.Sets;
import com.hyk.shoppingstreet.common.repository.Repository;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class MongoRepository<I, T> implements Repository<I, T> {
    private Class<I> idClass;
    private Class<T> entityClass;

    @Autowired
    private Datastore dataStore;

    private final Boolean useObjectId;

    @SuppressWarnings("unchecked")
    public MongoRepository() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.idClass = (Class<I>) pt.getActualTypeArguments()[0];
        this.entityClass = (Class<T>) pt.getActualTypeArguments()[1];
        this.useObjectId = false;
    }

    public MongoRepository(Class<T> entityClass, Datastore dataStore) {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.idClass = (Class<I>) pt.getActualTypeArguments()[0];
        this.entityClass = entityClass;
        this.dataStore = dataStore;
        this.useObjectId = false;
    }

    public MongoRepository(Class<I> idClass, Class<T> entityClass, Datastore dataStore, Boolean useObjectId) {
        this.idClass = idClass;
        this.entityClass = entityClass;
        this.dataStore = dataStore;
        this.useObjectId = useObjectId;

        if (useObjectId) {
            Assert.isTrue(idClass == String.class, "id type should only be String when useObjectId is true");
        }
    }

    @Override
    public int insert(T model) {
        if (this.idClass == ObjectId.class || useObjectId) {
            if (getId(model) == null) {
                setId(model, new ObjectId(), ObjectId.class);
            }
        } else {
            Assert
                .notNull(this.getId(model), "Non-ObjectId type id has to be provided during insert");
        }
        Key<T> result = this.dataStore.save(model);
        return Objects.isNull(result) ? 0 : 1;
    }

    @Override
    public int insert(List<T> models) {
        if (this.idClass == ObjectId.class || useObjectId) {
            models.forEach((model) -> {
                if (getId(model) == null) {
                    setId(model, new ObjectId(), ObjectId.class);
                }
            });
        } else {
            models.forEach((model) -> Assert
                .notNull(this.getId(model), "Non-ObjectId type id has to be provided during insert"));
        }

        Iterable<Key<T>> result = this.dataStore.save(models);
        return Objects.isNull(result) ? 0 : 1;
    }

    public BulkWriteResult insertIfNotExist(List<T> models) {
        BulkWriteOperation bulk = dataStore.getCollection(entityClass).initializeUnorderedBulkOperation();
        for (T model : models) {
            DBObject setOnInsert = new BasicDBObject();
            Arrays.stream(entityClass.getDeclaredFields()).filter(f -> f.getAnnotation(Id.class) == null).forEach(field -> {
                try {
                    Method getter = entityClass.getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                    Object fieldValue = getter.invoke(model);
                    if (fieldValue != null) {
                        setOnInsert.put(field.getName(), fieldValue);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
            bulk.find(new BasicDBObject(Mapper.ID_KEY, getId(model))).upsert()
                .updateOne(new BasicDBObject("$setOnInsert", setOnInsert));
        }
        return bulk.execute();
    }

    public BulkWriteResult insertIfNotExist(List<T> models, String... fieldName) {
        if (this.idClass == ObjectId.class || useObjectId) {
            models.forEach((model) -> {
                if (getId(model) == null) {
                    setId(model, new ObjectId(), ObjectId.class);
                }
            });
        } else {
            models.forEach((model) -> Assert
                .notNull(this.getId(model), "Non-ObjectId type id has to be provided during insert"));
        }

        Set<String> checkFields = Sets.newHashSet(fieldName);

        BulkWriteOperation bulk = dataStore.getCollection(entityClass).initializeUnorderedBulkOperation();
        for (T model : models) {
            DBObject setOnInsert = new BasicDBObject();
            Arrays.stream(entityClass.getDeclaredFields()).filter(f -> !checkFields.contains(f.getName())).forEach(field -> {
                try {
                    Method getter = entityClass.getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                    Object fieldValue = getter.invoke(model);
                    if (fieldValue != null) {
                        String fieldNameNow = field.getName();
                        if ("id".equals(fieldNameNow)) {
                            fieldNameNow = Mapper.ID_KEY;
                        }
                        setOnInsert.put(fieldNameNow, fieldValue);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });

            BasicDBObject checkQuery = new BasicDBObject();
            checkQuery.putAll(checkFields.stream().collect(
                Collectors.toMap(f -> f, f -> getFieldValue(model, f))));


            bulk.find(checkQuery).upsert()
                .updateOne(new BasicDBObject("$setOnInsert", setOnInsert));
        }
        return bulk.execute();
    }

    public BulkWriteResult insertIfNotExist(T model, Map clue) {
        if (this.idClass == ObjectId.class || useObjectId) {
            if (getId(model) == null) {
                setId(model, new ObjectId(), ObjectId.class);
            }
        } else {
            Assert
                .notNull(this.getId(model), "Non-ObjectId type id has to be provided during insert");
        }

        Set<String> checkFields = (Set<String>) clue.keySet().stream().map(k -> k.toString()).collect(Collectors.toSet());

        BulkWriteOperation bulk = dataStore.getCollection(entityClass).initializeUnorderedBulkOperation();
        DBObject setOnInsert = new BasicDBObject();
        Arrays.stream(entityClass.getDeclaredFields()).filter(f -> !checkFields.contains(f.getName())).forEach(field -> {
            try {
                Method getter = entityClass.getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                Object fieldValue = getter.invoke(model);
                if (fieldValue != null) {
                    String fieldNameNow = field.getName();
                    if ("id".equals(fieldNameNow)) {
                        fieldNameNow = Mapper.ID_KEY;
                    }
                    setOnInsert.put(fieldNameNow, fieldValue);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        BasicDBObject checkQuery = new BasicDBObject();
        checkQuery.putAll(clue);

        bulk.find(checkQuery).upsert()
            .updateOne(new BasicDBObject("$setOnInsert", setOnInsert));
        return bulk.execute();
    }

    @Override
    public int deleteById(I id) {
        if (useObjectId) {
            return dataStore.delete(entityClass, new ObjectId((String) id)).getN();
        } else {
            return dataStore.delete(entityClass, id).getN();
        }
    }

    @Override
    public int deleteByIds(List<I> ids) {
        if (useObjectId) {
            return dataStore.delete(entityClass, ids.stream()
                .map(objId -> new ObjectId((String) objId)).collect(Collectors.toList())).getN();
        } else {
            return dataStore.delete(entityClass, ids).getN();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public int deleteByCondition(Object condition) {
        return dataStore.delete((Query<T>) condition).getN();
    }

    @Override
    public int update(T model) {
        UpdateOperations<T> ops = genUpdateOps(model);
        return dataStore.update(model, ops).getUpdatedCount();
    }

    @Override
    public int updateByIds(List<I> ids, T model) {
        UpdateOperations<T> ops = genUpdateOps(model);
        return updateByIds(ids, ops);
    }

    public int updateByIds(List<I> ids, UpdateOperations<T> ops) {
        if (useObjectId) {
            return dataStore.update(dataStore.createQuery(entityClass)
                .field(Mapper.ID_KEY).in(ids.stream().map(objId -> new ObjectId((String) objId)).collect(Collectors.toList())), ops).getUpdatedCount();
        } else {
            return dataStore.update(dataStore.createQuery(entityClass)
                .field(Mapper.ID_KEY).in(ids), ops).getUpdatedCount();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateByCondition(Object condition, T model) {
        UpdateOperations<T> ops = genUpdateOps(model);
        return updateByCondition(condition, ops);
    }

    public int updateByCondition(Object condition, UpdateOperations<T> ops) {
        return dataStore.update((Query<T>) condition, ops).getUpdatedCount();
    }

    @Override
    public T findById(I id) {
        if (useObjectId) {
            return dataStore.get(entityClass, new ObjectId((String) id));
        } else {
            return dataStore.get(entityClass, id);
        }
    }

    @Override
    public List<T> findByIds(List<I> ids) {
        if (useObjectId) {
            return dataStore.get(entityClass, ids.stream().map(objId -> new ObjectId((String) objId)).collect(Collectors.toList())).asList();
        } else {
            return dataStore.get(entityClass, ids).asList();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByCondition(Object condition) {
        return findByConditionWithOption((Query<T>) condition, null);
    }

    public List<T> findByConditionWithOption(Query<T> query, FindOptions options) {
        if (options != null) {
            return query.asList(options);
        }
        return query.asList();
    }

    @Override
    public List<T> findAll() {
        return dataStore.createQuery(entityClass).asList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public I getId(T model) {
        Field idField = Arrays.stream(entityClass.getDeclaredFields()).filter(f -> f.getAnnotation(Id.class) != null).findAny().orElse(null);
        if (idField == null) {
            return null;
        }
        try {
            Method getter = entityClass.getMethod("get" + idField.getName().substring(0, 1).toUpperCase() + idField.getName().substring(1));
            Object id = getter.invoke(model);
            if (useObjectId && id != null) {
                Assert.isTrue(id instanceof ObjectId, "Id can only be of ObjectId when useObjectId is true");
                return (I) ((ObjectId) id).toHexString();
            }
            return (I) id;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void setId(T model, Object nid, Class<?> keyClass) {
        Field idField = Arrays.stream(entityClass.getDeclaredFields()).filter(f -> f.getAnnotation(Id.class) != null).findAny().orElse(null);
        Assert.notNull(idField != null, "Id field not found");
        try {
            Method setter = entityClass.getMethod(
                "set" + idField.getName().substring(0, 1).toUpperCase() + idField.getName().substring(1),
                keyClass != null ? keyClass : idClass);
            setter.invoke(model, nid);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public Object getFieldValue(T model, String fieldName) {
        Field idField = Arrays.stream(entityClass.getDeclaredFields()).filter(f -> f.getName().equals(fieldName)).findAny().orElse(null);
        if (idField == null) {
            return null;
        }
        try {
            Method getter = entityClass.getMethod("get" + idField.getName().substring(0, 1).toUpperCase() + idField.getName().substring(1));
            return getter.invoke(model);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UpdateOperations<T> initUpdateOps() {
        UpdateOperations<T> ops = dataStore
            .createUpdateOperations(entityClass);
        return ops;
    }

    private UpdateOperations<T> genUpdateOps(T model) {
        UpdateOperations<T> ops = dataStore
            .createUpdateOperations(entityClass);

        Arrays.stream(entityClass.getDeclaredFields()).forEach(field -> {
            if (field.getAnnotation(Id.class) == null) {
                try {
//                    Assert.state("id".equals(field.getName()), "The field as pk has to be 'id'");
                    Method getter = entityClass.getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                    Object fieldValue = getter.invoke(model);
                    if (fieldValue != null) {
                        ops.set(field.getName(), fieldValue);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        return ops;
    }

    public Long count() {
        return dataStore.getCount(entityClass);
    }

    public Long countByCondition(Object condition) {
        return dataStore.getCount((Query<T>) condition);
    }
}
