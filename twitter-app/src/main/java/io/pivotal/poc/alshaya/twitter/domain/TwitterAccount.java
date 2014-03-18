package io.pivotal.poc.alshaya.twitter.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.social.twitter.api.TwitterProfile;

@Entity
public class TwitterAccount implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long twitterId;
	
	@Column
	private String username;
	
	@Column
	private String name;
	
	@Column
	private boolean root;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<TwitterAccount> followers;
	
	@Column
	private Integer followerCount;
	
	public TwitterAccount() {
		followers = new ArrayList<TwitterAccount>();
	}
	
	public TwitterAccount(Long id) {
		this();
		this.id = id;
	}
	
	public TwitterAccount(Long id,Long twitterId) {
		this();
		this.id = id;
		this.twitterId = twitterId;
	}	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public List<TwitterAccount> getFollowers() {
		return followers;
	}

	public void setFollowers(List<TwitterAccount> followers) {
		this.followers = followers;
	}

	public Long getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(Long twitterId) {
		this.twitterId = twitterId;
	}

	public Integer getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(Integer followerCount) {
		this.followerCount = followerCount;
	}

	public static TwitterAccount load(TwitterProfile twitterProfile) {
		TwitterAccount account = new TwitterAccount(null,twitterProfile.getId());
		account.setName(twitterProfile.getName());
		account.setUsername(twitterProfile.getScreenName());
		account.setFollowerCount(twitterProfile.getFollowersCount());
		//return
		return account;
	}

	@Override
	public String toString() {
		return "TwitterAccount [id=" + id + ", twitterId=" + twitterId
				+ ", username=" + username + ", name=" + name + ", root="
				+ root + ", followerCount=" + followerCount + "]";
	}

}
