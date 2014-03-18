package io.pivotal.poc.alshaya.twitter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
@PropertySources({@PropertySource("classpath:oauth.properties")})
public class TwitterConfiguration {

	@Value("${twitter.oauth.consumerKey}")
	private String consumerKey;
	
    @Value("${twitter.oauth.consumerSecret}")
    private String consumerSecret;

//    @Value("${twitter.oauth.accessToken}")
//    private String accessToken;
//
//    @Value("${twitter.oauth.accessTokenSecret}")
//    private String accessTokenSecret;
	
	@Bean
	public TwitterTemplate twitterTemplate() {
		return new TwitterTemplate(consumerKey, consumerSecret);
	}
	
	@Bean
	public static PropertyPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertyPlaceholderConfigurer();
	}
}
