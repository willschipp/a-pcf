package io.pivotal.poc.alshaya.twitter.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import io.pivotal.poc.alshaya.twitter.Bootstrap;
import io.pivotal.poc.alshaya.twitter.domain.TwitterAccount;
import io.pivotal.poc.alshaya.twitter.domain.TwitterAccountRepository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Bootstrap.class)
public class SimpleTwitterAccountLoaderServiceIntegrationTest {

	@Autowired
	private TwitterAccountLoaderService loaderService;
	
	@Autowired
	private TwitterAccountRepository repository;
	
	@Test
	public void testLoadAllFollowers() {
		//create a root account
		TwitterAccount account = new TwitterAccount();
		account.setRoot(true);
		account.setUsername("incomplete_code");
		//save
	}

	@Test
	public void testLoadFollowers() {
	}

	@Test
	public void testLoadAccount() {
		//create a root account
		TwitterAccount account = new TwitterAccount();
		account.setRoot(true);
		account.setUsername("incomplete_code");
		//load
		loaderService.loadAccount(account);
		//now check again
		account = repository.findByUsername(account.getUsername());
		assertNotNull(account);
		assertNotNull(account.getId());
		assertTrue(account.getFollowerCount() > 0);
	}

}
