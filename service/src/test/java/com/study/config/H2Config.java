package com.study.config;

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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class H2Config {

    @Bean(name = "h2DataSource")
    public DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:set-mysql-mode.sql")
                .addScript("classpath:init.sql")
                .addScript("classpath:alias.sql")
                .build();
    }

    private static final String[] mapperPath = {"com.study.dal.mapper"};


    /**
     * 1.xml目录导入
     * 2.分页插件
     * 3.驼峰识别
     *
     * 一般系统中有mybatis-config.xml配置文件，可以将分页插件、驼峰识别等能力配置在SqlSessionFactoryBean中
     * @param h2DataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "h2SqlSessionFactory")
    public SqlSessionFactory h2SqlSessionFactory(@Qualifier("h2DataSource") DataSource h2DataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(h2DataSource);
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


    /**
     * mapper目录导入
     * @return
     */
    @Bean(name = "mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("h2SqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(StringUtils.join(mapperPath, ","));
        mapperScannerConfigurer.setAnnotationClass(Mapper.class);
        return mapperScannerConfigurer;
    }

    @Bean(name = "h2SessionTemplate")
    public SqlSessionTemplate h2SessionTemplate(@Qualifier("h2SqlSessionFactory") SqlSessionFactory h2SqlSessionFactory) {
        //使用上面配置的Factory
        return new SqlSessionTemplate(h2SqlSessionFactory);
    }

    /**
     * 事务管理器，支持注解
     * @param h2DataSource
     * @return
     */
    @Bean(name = "h2TransactionManager")
    public DataSourceTransactionManager h2TransactionManager(@Qualifier("h2DataSource") DataSource h2DataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(h2DataSource);
        return dataSourceTransactionManager;
    }

    /**
     * 事务模板
     * @param h2TransactionManager
     * @return
     */
    @Bean(name = "h2TransactionTemplate")
    public TransactionTemplate transactionTemplate(@Qualifier("h2TransactionManager") DataSourceTransactionManager h2TransactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(h2TransactionManager);
        return transactionTemplate;
    }
}
