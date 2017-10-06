package com.restaurant.twitter;

import com.restaurant.dao.DBConnector;
import com.restaurant.twitter.listener.TwitterListener;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserStreamListener;
import twitter4j.auth.AccessToken;

public class TwitterConnector {

	private Twitter twitter;

	public TwitterConnector(DBConnector dbConnector) {
		String consumerKeyStr = "TQRIASQDijLviiLS96ZkVmYCT";
		String consumerSecretStr = "uzfLXguH3acAnTO2hhZQBIvfUvOOZjGxUMAYgzDJo5HS6AjUzG";
		String accessTokenStr = "3434460779-kO0batPtbrCMGs820UdCnfRVsUffqvqprCPQNwu";
		String accessTokenSecretStr = "FSDi8lhZ9QE2pz90xztgqmHLuZOpY0VCNA9zMLK9vobNh";

		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
		AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);
		twitter.setOAuthAccessToken(accessToken);

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
		twitterStream.setOAuthAccessToken(accessToken);

		UserStreamListener listener = new TwitterListener(this, dbConnector);
		twitterStream.addListener(listener);

		// user() method internally creates a thread which manipulates
		// TwitterStream and calls these adequate listener methods continuously.
		twitterStream.user();
	}

	public void postMessage(String coupon) {
		try {
			twitter.updateStatus(coupon);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public void postMessage(StatusUpdate status) {
		try {
			twitter.updateStatus(status);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public User getUser() {
		User user = null;
		try {
			user = twitter.verifyCredentials();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return user;
	}

}
