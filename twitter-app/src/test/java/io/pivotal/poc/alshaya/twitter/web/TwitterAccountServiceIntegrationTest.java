package io.pivotal.poc.alshaya.twitter.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import io.pivotal.poc.alshaya.twitter.Bootstrap;
import io.pivotal.poc.alshaya.twitter.domain.TwitterAccount;
import io.pivotal.poc.alshaya.twitter.domain.TwitterAccountRepository;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes=Bootstrap.class)
public class TwitterAccountServiceIntegrationTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private TwitterAccountRepository repository;
	
	private MockMvc mockMvc;
	
	private Long id;
	
	@Before
	public void before() {
		mockMvc = webAppContextSetup(context).build();
		//add an account
		TwitterAccount account = new TwitterAccount();
		account.setName("test account");
		account.setRoot(true);
		//save
		repository.save(account);
		//set
		id = account.getId();
		//add a follower
		TwitterAccount follower = new TwitterAccount();
		follower.setName("a follower");
		account.getFollowers().add(follower);
		//save
		repository.save(account);
	}
	
	@After
	public void after() {
		//clean up
		repository.deleteAllInBatch();
	}
	
	@Test
	public void test() throws Exception {
		MvcResult result = mockMvc.perform(get("/twitterAccount")).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn();
	}
	
	@Test
	public void testRoot() throws Exception {
		MvcResult result = mockMvc.perform(get("/twitterAccount/root")).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn();
	}
	
	@Test
	public void testFollowers() throws Exception {
		MvcResult result = mockMvc.perform(get("/twitterAccount/{id}/followers",id.toString())).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn();
		//show
		String response = result.getResponse().getContentAsString();
		List<?> accounts = new ObjectMapper().readValue(response, List.class);
		System.out.println(accounts);
	}	

}
