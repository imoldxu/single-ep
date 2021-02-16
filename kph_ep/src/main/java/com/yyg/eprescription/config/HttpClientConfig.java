package com.yyg.eprescription.config;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.HttpResponse;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

	@Bean
	public CloseableHttpClient httpClient() throws Exception {
			
		// 解决keepAlive设置的问题
		ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
	
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				long keepAlive = super.getKeepAliveDuration(response, context);
				if (keepAlive == -1) {
					keepAlive = 3600 * 1000;
				}
				return keepAlive;
			}
		};
	
		// 解决https的问题，信任所有的证书
		SSLContextBuilder sslbuilder = new SSLContextBuilder();
		sslbuilder.loadTrustMaterial(null, new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				return true;
			}
		});
		SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslbuilder.build(),
				new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
				RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslFactory)
	            .build());
	    cm.setMaxTotal(128);
	    
		CloseableHttpClient client = HttpClients.custom()
				.setConnectionManager(cm)
				.setSSLSocketFactory(sslFactory).setKeepAliveStrategy(keepAliveStrategy).build();
		return client;
		
	}
	
}
