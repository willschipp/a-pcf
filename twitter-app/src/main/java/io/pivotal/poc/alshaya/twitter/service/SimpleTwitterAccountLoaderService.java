package io.pivotal.poc.alshaya.twitter.service;

import io.pivotal.poc.alshaya.twitter.domain.TwitterAccount;
import io.pivotal.poc.alshaya.twitter.domain.TwitterAccountRepository;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SimpleTwitterAccountLoaderService implements TwitterAccountLoaderService {

	private static final Logger logger = Logger.getLogger(SimpleTwitterAccountLoaderService.class);
	
	@Autowired
	private TwitterTemplate twitterTemplate;
	
	@Autowired
	private TwitterAccountRepository repository;
	
	@Autowired(required=false)
	private int cursorCount = 80;//default cursor size
	
	@Autowired(required=false)
	private long wait = 0;//default - no wait
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@Override
	public void loadAllFollowers() {
		//get all the 'root' followers
		List<TwitterAccount> rootAccounts = repository.findByRootIsTrue();
		for (TwitterAccount account : rootAccounts) {
			loadFollowers(account.getTwitterId());
		}//end for
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void loadFollowers(final Long twitterId) {
		//load the ids
		try {
			final Queue<Long> followerIds = loadFollowerIds(twitterId);
			//now load
			if (followerIds.size() > cursorCount) {
				//need to load async
				taskExecutor.execute(new Runnable() {

					@Override
					public void run() {
						try {
							//build a list
							List<Long> userIds = new ArrayList<Long>();
							while (!followerIds.isEmpty()) {
								Long id = followerIds.poll();
								if (id != null) {
									userIds.add(id);
								}//end if
								if (userIds.size() >= cursorCount
										|| followerIds.isEmpty()) {
									//execute on the list --> 
									loadAccounts(userIds,twitterId);
									//reset
									userIds = new ArrayList<Long>();
								}//end if
							}//end while
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				});
			} else {
				//load synchronously
				loadAccounts(new ArrayList<Long>(followerIds),twitterId);
			}//end if
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	@Override
	public void loadAccount(TwitterAccount twitterAccount) {
		loadAccount(twitterAccount, null);
	}	
	
	@Override
	public void loadAccount(TwitterAccount twitterAccount,Long parentId) {
		//init
		TwitterProfile profile = null;
		//load an individual account
		if (twitterAccount.getTwitterId() != null) {
			profile = twitterTemplate.userOperations().getUserProfile(twitterAccount.getTwitterId());
		} else if (twitterAccount.getUsername() != null) {
			//no id but the username - so use that instead
			profile = twitterTemplate.userOperations().getUserProfile(twitterAccount.getUsername());
		}//end if
		//save
		if (profile != null) {
			boolean root = false;
			root = twitterAccount.isRoot();
			twitterAccount = TwitterAccount.load(profile);
			//update
			twitterAccount.setRoot(root);
			//save
			repository.save(twitterAccount);
			//check for parentid
			if (parentId != null) {
				//retrieve
				TwitterAccount parentAccount = repository.findByTwitterIdAndFetchFollowersEagerly(parentId);
				if (parentAccount == null) {
					//no followers so it returns null, so retrieve normally
					parentAccount = repository.findByTwitterId(parentId);
				}//end if
				//add it to the parent account
				parentAccount.getFollowers().add(twitterAccount);
				//save
				repository.save(parentAccount);
			}//end if
		} else {
			logger.warn("profile not loaded " + twitterAccount);
		}//end if
	}
	
	private void loadAccounts(List<Long> userIds,Long parentId) throws Exception {
		for (Long userId : userIds) {
			//load
			loadAccount(new TwitterAccount(null,userId),parentId);
			//wait
			Thread.sleep(wait);
		}//end for
	}

	private Queue<Long> loadFollowerIds(Long parentId) throws Exception {
		Queue<Long> followerId = new ArrayDeque<Long>();
		//loop and add
		CursoredList<Long> cursor = twitterTemplate.friendOperations().getFollowerIdsInCursor(parentId, -1);//first page
		followerId.addAll(cursor);
		//go
		while (cursor.hasNext()) {
			cursor = twitterTemplate.friendOperations().getFollowerIdsInCursor(parentId,cursor.getNextCursor());
			followerId.addAll(cursor);
			Thread.sleep(wait);
		}//end for
		//return
		return followerId;
	}

	public void setTwitterTemplate(TwitterTemplate twitterTemplate) {
		this.twitterTemplate = twitterTemplate;
	}

	public void setRepository(TwitterAccountRepository repository) {
		this.repository = repository;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setCursorCount(int cursorCount) {
		this.cursorCount = cursorCount;
	}

	
}
