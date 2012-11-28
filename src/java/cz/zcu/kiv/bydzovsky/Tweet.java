/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.bydzovsky;

import java.util.Date;

/**
 *
 * @author bydga
 */
public class Tweet {

	private int tweetId;
	private Date published;
	private String text;
	private User author;
	private Tweet retweetedFrom;
	private int retweetCount;

	public int getTweetId() {
		return tweetId;
	}

	public void setTweetId(int tweetId) {
		this.tweetId = tweetId;
	}

	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Tweet getRetweetedFrom() {
		return retweetedFrom;
	}

	public void setRetweetedFrom(Tweet retweetedFrom) {
		this.retweetedFrom = retweetedFrom;
	}

	public void setRwetweetCount(int count) {
		this.retweetCount = count;
	}

	public int getRetweetCount() {
		return this.retweetCount;
	}
}
