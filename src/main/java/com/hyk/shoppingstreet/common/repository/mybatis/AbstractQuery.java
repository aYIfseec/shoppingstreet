package com.hyk.shoppingstreet.common.repository.mybatis;


import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example;

@Data
public abstract class AbstractQuery<T> {
    private Integer pageNo;
    private Integer pageSize;

    private Class<T> modelClass;

    public AbstractQuery() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public AbstractQuery(Class<T> modelClass) {
        if (modelClass == null) {
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            this.modelClass = (Class<T>) pt.getActualTypeArguments()[0];
        } else {
            this.modelClass = modelClass;
        }
    }

    public Example toExample() {
        if (!checkFields()) {
            throw new RuntimeException("required field not provieded in query");
        }
        Example example = new Example(modelClass);
        Example.Criteria condition = example.createCriteria();
        addCondition(condition);
        if (StringUtils.isNotBlank(this.defaultOrder())) {
            example.setOrderByClause(this.defaultOrder());
        }
        if (null != pageNo && null != pageSize) {
            PageHelper.startPage(pageNo + 1, pageSize);
        }
        return example;
    }

    protected abstract void addCondition(Example.Criteria condition);

    protected String defaultOrder() {
        return "";
    }

    public void setPage(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public List<String> requiredFields() {
        return Lists.newArrayList();
    }

    private Boolean checkFields() {
        for (String field : requiredFields()) {
            try {
                Method method = getClass().getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
                Object value = method.invoke(this);
                if (value == null) {
                    return false;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}