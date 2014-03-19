package io.pivotal.poc.clouddb.support;

public class SystemEnvironment implements Environment {

	@Override
	public String getEvnironmentVariable(String name) {
		return System.getenv(name);
	}

}
