package com.yyg.eprescription.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
public class DruidConfig {

	@Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
		// 将所有前缀为spring.datasource下的配置项都加载DataSource中
		DataSource ds = new DruidDataSource();
		return ds;
    }
	
	
	/**
	 * 配置druid的监控
	 * @return
	 */
	@Bean
	public ServletRegistrationBean<StatViewServlet> statViewServlet() {
		ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<StatViewServlet>(
				new StatViewServlet(), "/druid/*");
		// 白名单
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
		// IP黑名单（共同存在时，deny优先于allow）
		servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
		// 登陆查看新的是账户密码
		servletRegistrationBean.addInitParameter("loginUsername", "druid");
		servletRegistrationBean.addInitParameter("loginPassword", "123456");
		// 是否能够重置数据
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<WebStatFilter> statFilter() {
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>(
				new WebStatFilter());
		// 添加过滤规则
		filterRegistrationBean.addUrlPatterns("/*");
		// 添加不需要忽略的格式信息
		filterRegistrationBean.addInitParameter("exclusions", "*.js.*.gif,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}
}
