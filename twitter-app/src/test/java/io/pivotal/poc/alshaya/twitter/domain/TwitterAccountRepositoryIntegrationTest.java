package io.pivotal.poc.alshaya.twitter.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import io.pivotal.poc.alshaya.twitter.Bootstrap;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringApplicationConfiguration(classes=Bootstrap.class)
public class TwitterAccountRepositoryIntegrationTest {

	@Autowired
	private TwitterAccountRepository repository;
	
	private TwitterAccount account;
	
	@Before
	public void before() {
		account = new TwitterAccount();
	}
	
	@After
	public void after() {
		repository.deleteAllInBatch();
	}
	
	
	@Test
	public void test() {
		//create a twitter account
		account.setName("test 1");
		//save
		repository.save(account);
		//check
		assertNotNull(account.getId());
	}
	
	@Test
	public void testHierarchy() {
		//create a twitter account
		account.setName("test 1");
		//save
		repository.save(account);
		//check
		assertNotNull(account.getId());
		
		//create another and add
		TwitterAccount child = new TwitterAccount();
		child.setName("test 1");
		
		//add
		account.getFollowers().add(child);
		//save
		repository.save(account);
		//check
		account = repository.findOne(account.getId());
		assertFalse(account.getFollowers().isEmpty());
		assertNotNull(account.getFollowers().get(0).getId());
	}	

	@Test
	public void testTrue() {
		//create a twitter account
		account.setName("test 1");
		account.setRoot(true);
		//save
		repository.save(account);
		//check
		assertNotNull(repository.findByRootIsTrue());
		assertFalse(repository.findByRootIsTrue().isEmpty());
		assertNotNull(account.getId());		
	}
}
