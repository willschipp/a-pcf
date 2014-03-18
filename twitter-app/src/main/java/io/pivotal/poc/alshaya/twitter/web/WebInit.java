package io.pivotal.poc.alshaya.twitter.web;

import io.pivotal.poc.alshaya.twitter.Bootstrap;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

public class WebInit extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Bootstrap.class);
	}

}
