package io.pivotal.poc.alshaya.twitter;

import javax.sql.DataSource;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

public class CloudConfig extends AbstractCloudConfig {

	@Bean
	@Profile("cloud")
	public DataSource dataSource() {
		return connectionFactory().dataSource("alshaya-pgsql");
	}
	
}
