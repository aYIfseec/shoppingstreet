package com.hyk.shoppingstreet.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.hyk.shoppingstreet.tk.DaoPageInterceptor;
import javax.sql.DataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@MapperScan(basePackages = {"com.hyk.shoppingstreet.model"}, sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement(proxyTargetClass = true)
public class MybatisConfig {

    @Value("${mysql.url}")
    private String url;

    @Value("${mysql.user}")
    private String user;

    @Value("${mysql.password}")
    private String password;

    @Value("${mysql.pool.initSize}")
    private int initSize;

    @Value("${mysql.pool.minIdle}")
    private int minIdle;

    @Value("${mysql.pool.maxActive}")
    private int maxActive;

    @Value("${mysql.slave.lag:3}")
    private int slaveLag;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        // 配置初始化大小、最小、最大
        dataSource.setInitialSize(initSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);

        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestOnBorrow(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnReturn(false);
//        dataSource.setFilters("wall");

        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(60);
        dataSource.setLogAbandoned(true);

        return dataSource;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "transactionTemplate")
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(transactionManager());
    }
//
//    @Autowired
//    private ShardedJedisPool shardedJedisPool;


    @Bean
    public DaoPageInterceptor daoPageInterceptor() {
        DaoPageInterceptor dalPageInterceptor = new DaoPageInterceptor();
//        Properties props = new Properties();
//        props.setProperty("sqlIdByPageRegex", ".+ByPage$");
//        dalPageInterceptor.setProperties(props);
        dalPageInterceptor.setSqlIdByPageRegex(".+ByPage$");
//        dalPageInterceptor.setSlaveLagMark(slaveLagMark());
        return dalPageInterceptor;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        // 设置查找器
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 自动扫描mybatis文件
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:com/hyk/shoppingstreet/mapper/*.xml"));
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{ daoPageInterceptor() });
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        sqlSessionFactory.getConfiguration().setJdbcTypeForNull(JdbcType.NULL);
        return sqlSessionFactory;
    }
}
