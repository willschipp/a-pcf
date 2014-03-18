package io.pivotal.poc.alshaya.twitter.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TwitterAccountRepository extends JpaRepository<TwitterAccount, Long> {
	
	List<TwitterAccount> findByRootIsTrue();
	
	TwitterAccount findByUsername(String username);
	
	TwitterAccount findByTwitterId(Long twitterId);
	
	@Query("select t from TwitterAccount t join fetch t.followers where t.twitterId = (:twitterId)")
	TwitterAccount findByTwitterIdAndFetchFollowersEagerly(@Param("twitterId") Long twitterId);
	
	@Query("select t from TwitterAccount t join fetch t.followers where t.id = (:id)")
	TwitterAccount findByIdAndFetchFollowersEagerly(@Param("id") Long id);
}
