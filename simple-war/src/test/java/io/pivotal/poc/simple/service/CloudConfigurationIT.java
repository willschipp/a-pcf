package io.pivotal.poc.simple.service;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CloudConfigurationIT {

	private CloudConfiguration cloudConfiguration;
	
	@Test
	public void test() {
		cloudConfiguration = new CloudConfiguration();
		assertFalse(cloudConfiguration.isInitialized());
	}

}
