package kr.co.mindpro.ipms.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfig {

    /**
     * MyBatis 공통 설정 (CamelCase, Null 처리 등)
     */
    private static org.apache.ibatis.session.Configuration getMybatisConfig() {
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        config.setMapUnderscoreToCamelCase(true);
        config.setLogImpl(org.apache.ibatis.logging.log4j2.Log4j2Impl.class);
        config.setCallSettersOnNulls(true);
        return config;
    }

    // =========================================================================
    // DB 1 (Primary - MP_IPMS_PA)
    // =========================================================================
    @Configuration
    @MapperScan(basePackages = "kr.co.mindpro.ipms.domain.**.repository.db1", sqlSessionFactoryRef = "db1SqlSessionFactory")    
    public static class Db1Config {
        @Primary
        @Bean(name = "db1DataSource")
        @ConfigurationProperties(prefix = "spring.datasource.datasource1")
        public DataSource db1DataSource() {
            return DataSourceBuilder.create().type(HikariDataSource.class).build();
        }

        @Primary
        @Bean(name = "db1SqlSessionFactory")
        public SqlSessionFactory db1SqlSessionFactory(@Qualifier("db1DataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            bean.setMapperLocations(applicationContext.getResources("classpath:mapper/db1/**/*.xml"));
            bean.setTypeAliasesPackage("kr.co.mindpro.ipms");
            bean.setTypeHandlersPackage("kr.co.mindpro.ipms.domain");
            bean.setConfiguration(getMybatisConfig());
            return bean.getObject();
        }

        @Primary
        @Bean(name = "db1TransactionManager")
        public DataSourceTransactionManager db1TransactionManager(@Qualifier("db1DataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    // =========================================================================
    // DB 2 (MP_IPMS_CORP)
    // =========================================================================
    @Configuration
    @MapperScan(basePackages = "kr.co.mindpro.ipms.domain.**.repository.db2", sqlSessionFactoryRef = "db2SqlSessionFactory")
    public static class Db2Config {
        @Bean(name = "db2DataSource")
        @ConfigurationProperties(prefix = "spring.datasource.datasource2")
        public DataSource db2DataSource() {
            return DataSourceBuilder.create().type(HikariDataSource.class).build();
        }

        @Bean(name = "db2SqlSessionFactory")
        public SqlSessionFactory db2SqlSessionFactory(@Qualifier("db2DataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            bean.setMapperLocations(applicationContext.getResources("classpath:mapper/db2/**/*.xml"));
            bean.setTypeAliasesPackage("kr.co.mindpro.ipms");
            bean.setTypeHandlersPackage("kr.co.mindpro.ipms.domain");
            bean.setConfiguration(getMybatisConfig());
            return bean.getObject();
        }

        @Bean(name = "db2TransactionManager")
        public DataSourceTransactionManager db2TransactionManager(@Qualifier("db2DataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    // =========================================================================
    // DB 3 (MP_IPMS)
    // =========================================================================
    @Configuration
    @MapperScan(basePackages = "kr.co.mindpro.ipms.domain.**.repository.db3", sqlSessionFactoryRef = "db3SqlSessionFactory")
    public static class Db3Config {
        @Bean(name = "db3DataSource")
        @ConfigurationProperties(prefix = "spring.datasource.datasource3")
        public DataSource db3DataSource() {
            return DataSourceBuilder.create().type(HikariDataSource.class).build();
        }

        @Bean(name = "db3SqlSessionFactory")
        public SqlSessionFactory db3SqlSessionFactory(@Qualifier("db3DataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            bean.setMapperLocations(applicationContext.getResources("classpath:mapper/db3/**/*.xml"));
            bean.setTypeAliasesPackage("kr.co.mindpro.ipms");
            bean.setTypeHandlersPackage("kr.co.mindpro.ipms.domain");
            bean.setConfiguration(getMybatisConfig());
            return bean.getObject();
        }

        @Bean(name = "db3TransactionManager")
        public DataSourceTransactionManager db3TransactionManager(@Qualifier("db3DataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }
}