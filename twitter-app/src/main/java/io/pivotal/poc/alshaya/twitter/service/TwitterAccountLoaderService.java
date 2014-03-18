package io.pivotal.poc.alshaya.twitter.service;

import io.pivotal.poc.alshaya.twitter.domain.TwitterAccount;

public interface TwitterAccountLoaderService {

	void loadAllFollowers();
	
	void loadFollowers(Long twitterId);

	void loadAccount(TwitterAccount twitterAccount);
	
	void loadAccount(TwitterAccount twitterAccount,Long parentId);
	
}