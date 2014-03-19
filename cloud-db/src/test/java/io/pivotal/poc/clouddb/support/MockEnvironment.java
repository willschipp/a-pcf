package io.pivotal.poc.clouddb.support;

import java.util.HashMap;
import java.util.Map;

public class MockEnvironment implements Environment {

	private Map<String,String> map = new HashMap<String,String>();
	
	public MockEnvironment() {
		map = new HashMap<String,String>();
	}
	
	public void addEnvironmentVariable(String name,String value) {
		map.put(name,value);
	}

	@Override
	public String getEvnironmentVariable(String name) {
		return map.get(name);
	}
}
