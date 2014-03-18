package io.pivotal.poc.alshaya.twitter.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.pivotal.poc.alshaya.twitter.domain.TwitterAccount;
import io.pivotal.poc.alshaya.twitter.domain.TwitterAccountRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.FriendOperations;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

public class SimpleTwitterAccountLoaderServiceTest {

	private TwitterAccountLoaderService service;
	
	private TwitterAccountRepository repository;
	
	private TwitterTemplate template;
	
	private FriendOperations friendOperations;
	
	private UserOperations userOperations;
	
	private TaskExecutor taskExecutor;
	
	@Before
	public void before() {
		service = new SimpleTwitterAccountLoaderService();
		repository = mock(TwitterAccountRepository.class);
		template = mock(TwitterTemplate.class);
		friendOperations = mock(FriendOperations.class);
		userOperations = mock(UserOperations.class);
		taskExecutor = mock(SimpleAsyncTaskExecutor.class);
		//set
		((SimpleTwitterAccountLoaderService)service).setRepository(repository);
		((SimpleTwitterAccountLoaderService)service).setTwitterTemplate(template);
		((SimpleTwitterAccountLoaderService)service).setTaskExecutor(taskExecutor);
	}
	
	@Test
	public void testLoadAllFollowers() {
		//set behavior
		when(repository.findByRootIsTrue()).thenReturn(Arrays.asList(new TwitterAccount(1l,1l)));
		when(template.friendOperations()).thenReturn(friendOperations);
		when(template.userOperations()).thenReturn(userOperations);
		CursoredList<Long> ids = new CursoredList<Long>(Arrays.asList(1l), -1, 0);
		when(friendOperations.getFollowerIdsInCursor(anyLong(), anyLong())).thenReturn(ids);
		//execute
		service.loadAllFollowers();
		//verify
		verify(repository,atLeastOnce()).findByRootIsTrue();
		verify(template,atLeastOnce()).friendOperations();
	}

	@Test
	public void testLoadAllFollowersAsync() {
		//set behavior
		when(repository.findByRootIsTrue()).thenReturn(Arrays.asList(new TwitterAccount(1l,1l)));
		when(template.friendOperations()).thenReturn(friendOperations);
		CursoredList<Long> ids = new CursoredList<Long>(Arrays.asList(1l), -1, 0);
		when(friendOperations.getFollowerIdsInCursor(anyLong(), anyLong())).thenReturn(ids);
		((SimpleTwitterAccountLoaderService)service).setCursorCount(0);
		//execute
		service.loadAllFollowers();
		//verify
		verify(repository,atLeastOnce()).findByRootIsTrue();
		verify(template,atLeastOnce()).friendOperations();
		verify(taskExecutor,atLeastOnce()).execute((Runnable) any());
	}


}
