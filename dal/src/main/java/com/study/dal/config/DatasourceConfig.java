package com.study.dal.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DatasourceConfig {


    //数据源设定
    @Primary
    @Bean(name = "myDataSource")
    public DataSource myDataSource() {
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/weixun?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false&allowMultiQueries=true");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");
        return druidDataSource;
    }


    //mybatis 事务配置

    private static final String[] mapperPath = {"com.study.dal.mapper"};

    /**
     * mapper目录导入
     * @return
     */
    @Bean(name = "mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("mySqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(StringUtils.join(mapperPath, ","));
        mapperScannerConfigurer.setAnnotationClass(Mapper.class);
        return mapperScannerConfigurer;
    }

    /**
     * 1.xml目录导入
     * 2.分页插件
     * 3.驼峰识别
     *
     * 一般系统中有mybatis-config.xml配置文件，可以将分页插件、驼峰识别等能力配置在SqlSessionFactoryBean中
     * @param myDataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "mySqlSessionFactory")
    public SqlSessionFactory mySqlSessionFactory(@Qualifier("myDataSource") DataSource myDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(myDataSource);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));

        //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");
        pageHelper.setProperties(properties);
        Interceptor[] plugins = new Interceptor[]{pageHelper};
        bean.setPlugins(plugins);
        //驼峰识别
        bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);

        return bean.getObject();
    }

    @Bean(name = "mySessionTemplate")
    public SqlSessionTemplate mySessionTemplate(@Qualifier("mySqlSessionFactory") SqlSessionFactory mySqlSessionFactory) {
        //使用上面配置的Factory
        return new SqlSessionTemplate(mySqlSessionFactory);
    }

    /**
     * 事务管理器，支持注解
     * @param myDataSource
     * @return
     */
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager myDataSourceTransactionManager(@Qualifier("myDataSource") DataSource myDataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(myDataSource);
        return dataSourceTransactionManager;
    }

    /**
     * 事务模板
     * @param transactionManager
     * @return
     */
    @Bean(name = "transactionTemplate")
    public TransactionTemplate transactionTemplate(@Qualifier("transactionManager") DataSourceTransactionManager transactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager);
        return transactionTemplate;
    }

//    @Bean(name = "myJdbcTemplate")
//    public JdbcTemplate myJdbcTemplate(@Qualifier("myDataSource") DataSource myDataSource) {
//        return new JdbcTemplate(myDataSource);
//    }

}
