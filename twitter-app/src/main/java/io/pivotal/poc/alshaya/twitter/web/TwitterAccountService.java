package io.pivotal.poc.alshaya.twitter.web;

import io.pivotal.poc.alshaya.twitter.domain.TwitterAccount;
import io.pivotal.poc.alshaya.twitter.domain.TwitterAccountRepository;
import io.pivotal.poc.alshaya.twitter.service.TwitterAccountLoaderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitterAccount")
public class TwitterAccountService {

	@Autowired
	private TwitterAccountRepository repository;
	
	@Autowired
	private TwitterAccountLoaderService service;
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<TwitterAccount> findAll() {
		return repository.findByRootIsTrue();
	}
	
	@RequestMapping(value="/{id}/detail",method=RequestMethod.GET)
	public @ResponseBody TwitterAccount findOne(@PathVariable("id") Long twitterId) {
		return repository.findByTwitterId(twitterId);
	}
	
	@RequestMapping(value="/root",method=RequestMethod.GET)
	public List<TwitterAccount> findAllMain() {
		return repository.findByRootIsTrue();
	}	
	
	@RequestMapping(method=RequestMethod.PUT)
	public TwitterAccount addAccount(@RequestBody TwitterAccount account) throws Exception {
		account.setRoot(true);//set it as a 'root' account
		//start the load
		service.loadAccount(account);
		//load from the repo
		final TwitterAccount updatedAccount = repository.findByUsername(account.getUsername());
		//load the followers
		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {
				service.loadFollowers(updatedAccount.getTwitterId());	
			}
			
		});
		//return
		return updatedAccount;
	}
	
	@Transactional(readOnly=true)
	@RequestMapping(value="/{id}/followers",method=RequestMethod.GET)
	public List<TwitterAccount> findFollowers(@PathVariable Long id) {
		//load the twitter account
		TwitterAccount account = repository.findByIdAndFetchFollowersEagerly(id);
		//load the followers
		return account.getFollowers();
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	public void deleteAll() {
		repository.deleteAllInBatch();//purge
	}
}
