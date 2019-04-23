package com.hyk.shoppingstreet.tk;

import com.hyk.shoppingstreet.common.utils.PageUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Slf4j
public class DaoPageInterceptor implements Interceptor {
    @Setter
    private String sqlIdByPageRegex = "";// 这则表达式用了筛选所有分页的sql语句

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // 通过拦截器得到被拦截的对象,就是上面配置的注解的对象
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        // 为了获取以及设置某些对象的属性值（某些对象的属性是没有getter/setter的），mybatis提供的快捷的通过反射设置获取属性只的工具类，当然也可以通过自己写反射完成
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
            SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        // 得到当前的mapper对象信息,即为各种select，update，delete，insert语句的映射配置信息，通过上面的工具类获取属性对象
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // 对映射语句进行选择过滤，如果是以ByPage结尾就拦截，否则不拦截
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = statementHandler.getBoundSql();
        String innerSql = boundSql.getSql().trim();

        if (sqlId.matches(sqlIdByPageRegex)) {

            // sql语句在对象BoundSql对象中，这个对象有get方法可以直接获取
            Map<?, ?> parameterMap = (Map<?, ?>) boundSql.getParameterObject();
            int pageSize = 10;
            int pageNo = 0;
            int offset = 0;

            if (parameterMap.containsKey("pageSize") && ((Integer) parameterMap.get("pageSize")) > 0) {
                pageSize = (Integer) parameterMap.get("pageSize");
            }
            if (parameterMap.containsKey("pageNo") && ((Integer) parameterMap.get("pageNo")) > 0) {
                pageNo = (Integer) parameterMap.get("pageNo");
                offset = ((pageNo - 1) < 0 ? 0 : pageNo) * pageSize;
            }

            if (pageSize != 999) {
                // 获取原始sql，该sql是预处理的，有参数还没有被设置，被问好代替了
                String sql = boundSql.getSql().trim();
                String countSql = getCountSql(sql);
                Connection connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
                PreparedStatement countStmt = connection.prepareStatement(countSql);
                BoundSql countBS = copyFromBoundSql(mappedStatement, boundSql, countSql);
                Object parameterObject = boundSql.getParameterObject();
                DefaultParameterHandler
                    parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, countBS);
                parameterHandler.setParameters(countStmt);
                ResultSet rs = countStmt.executeQuery();
                int totalCount = 0;
                if (rs.next()) {
                    totalCount = rs.getInt(1);
                }
                rs.close();
                countStmt.close();
                connection.close();
                PageUtils.cacheTotalCount(totalCount);
                innerSql = "select * from (" + sql + ") as sa limit " + offset + "," + pageSize;
            }
        }

        metaObject.setValue("delegate.boundSql.sql", innerSql);
        return invocation.proceed();
    }

    private String getCountSql(String sql) {
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        return "SELECT COUNT(1) FROM (" + sql + ") aliasForPage";
    }

    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
