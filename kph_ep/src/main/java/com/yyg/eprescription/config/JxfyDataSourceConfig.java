package com.yyg.eprescription.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;

import tk.mybatis.spring.annotation.MapperScan;

@Configuration    //该注解类似于spring配置文件
@MapperScan(basePackages="com.yyg.eprescription.jxfy.mapper", sqlSessionFactoryRef="jxfySqlSessionFactory")
public class JxfyDataSourceConfig {

	@Autowired
    private Environment env;
	
	@Bean(name="jxfyDS")
    @ConfigurationProperties(prefix = "spring.datasource2")
    public DataSource druidDataSource2() {
		// 将所有前缀为spring.datasource下的配置项都加载DataSource中
		DataSource ds = new DruidDataSource();
		return ds;
    }
	
	@Bean(name="jxfySqlSessionFactory")
    public SqlSessionFactory jxfySqlSessionFactory(@Qualifier("jxfyDS") DataSource jxfyDataSource) throws Exception{
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(jxfyDataSource);//指定数据源(这个必须有，否则报错)
        //设置配置
        org.apache.ibatis.session.Configuration configuration =  new org.apache.ibatis.session.Configuration();
        configuration.setDefaultExecutorType(ExecutorType.REUSE);//开启重用prepare Statement
		fb.setConfiguration(configuration );
        //FIXME:下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        fb.setTypeAliasesPackage(env.getProperty("mybatis.jxfyTypeAliasesPackage"));//指定基包
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.jxfyMapperLocations")));//指定xml文件位置
        //fb.setPlugins(plugins); 设置插件
        return fb.getObject();
    }
}
