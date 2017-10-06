package com.restaurant.twitter.listener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

import com.restaurant.dao.DBConnector;
import com.restaurant.twitter.TwitterConnector;

import twitter4j.DirectMessage;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusUpdate;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

public class TwitterListener implements UserStreamListener {
	
	private DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	private TwitterConnector connector;
	private DBConnector dbConnector;
	Random rnd = new Random();
	
	public TwitterListener(TwitterConnector connector, DBConnector dbConnector){
		this.connector = connector;
		this.dbConnector = dbConnector;
	}
	
    public void onStatus(Status status) {
    	String orderMessage = status.getText();
    	String senderScreenName = status.getUser().getScreenName();
    	String currentUserScreenName = connector.getUser().getScreenName();
    	String hashTags = "";
        System.out.println("onStatus @" + senderScreenName + " - " + orderMessage);
        for(HashtagEntity hashTag : status.getHashtagEntities()){
        	if(hashTags.equals(""))
        		hashTags = hashTag.getText();
        	else
        		hashTags = hashTags + ", " + hashTag.getText();
        }
        long messageId = Long.valueOf(df.format(status.getCreatedAt()));
        orderMessage = orderMessage.replace("@"+currentUserScreenName, "").trim();
		if(!senderScreenName.equalsIgnoreCase(currentUserScreenName)){
			StatusUpdate stat = new StatusUpdate("@" + status.getUser().getScreenName() + ": Thankyou for your order \n " + 
												"Your order #" + hashTags + " should be ready in " + rnd.nextInt(30) + " minutes\n");
			stat.inReplyToStatusId(status.getId());
			connector.postMessage(stat);
			dbConnector.addOrder(messageId, orderMessage, "#"+hashTags, senderScreenName, status.getCreatedAt());
		}
    }

   
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
    }

   
    public void onDeletionNotice(long directMessageId, long userId) {
        System.out.println("Got a direct message deletion notice id:" + directMessageId);
    }

   
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        System.out.println("Got a track limitation notice:" + numberOfLimitedStatuses);
    }

   
    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
    }

   
    public void onStallWarning(StallWarning warning) {
        System.out.println("Got stall warning:" + warning);
    }

   
    public void onFriendList(long[] friendIds) {
        System.out.print("onFriendList");
        for (long friendId : friendIds) {
            System.out.print(" " + friendId);
        }
        System.out.println();
    }

   
    public void onFavorite(User source, User target, Status favoritedStatus) {
        System.out.println("onFavorite source:@"
            + source.getScreenName() + " target:@"
            + target.getScreenName() + " @"
            + favoritedStatus.getUser().getScreenName() + " - "
            + favoritedStatus.getText());
    }

   
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        System.out.println("onUnFavorite source:@"
            + source.getScreenName() + " target:@"
            + target.getScreenName() + " @"
            + unfavoritedStatus.getUser().getScreenName()
            + " - " + unfavoritedStatus.getText());
    }

   
    public void onFollow(User source, User followedUser) {
        System.out.println("onFollow source:@"
            + source.getScreenName() + " target:@"
            + followedUser.getScreenName());
    }

   
    public void onUnfollow(User source, User followedUser) {
        System.out.println("onFollow source:@"
            + source.getScreenName() + " target:@"
            + followedUser.getScreenName());
    }

   
    public void onDirectMessage(DirectMessage directMessage) {
        System.out.println("onDirectMessage text:"
            + directMessage.getText());
    }

   
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
        System.out.println("onUserListMemberAddition added member:@"
            + addedMember.getScreenName()
            + " listOwner:@" + listOwner.getScreenName()
            + " list:" + list.getName());
    }

   
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
        System.out.println("onUserListMemberDeleted deleted member:@"
            + deletedMember.getScreenName()
            + " listOwner:@" + listOwner.getScreenName()
            + " list:" + list.getName());
    }

   
    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
        System.out.println("onUserListSubscribed subscriber:@"
            + subscriber.getScreenName()
            + " listOwner:@" + listOwner.getScreenName()
            + " list:" + list.getName());
    }

   
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
        System.out.println("onUserListUnsubscribed subscriber:@"
            + subscriber.getScreenName()
            + " listOwner:@" + listOwner.getScreenName()
            + " list:" + list.getName());
    }

   
    public void onUserListCreation(User listOwner, UserList list) {
        System.out.println("onUserListCreated  listOwner:@"
            + listOwner.getScreenName()
            + " list:" + list.getName());
    }

   
    public void onUserListUpdate(User listOwner, UserList list) {
        System.out.println("onUserListUpdated  listOwner:@"
            + listOwner.getScreenName()
            + " list:" + list.getName());
    }

   
    public void onUserListDeletion(User listOwner, UserList list) {
        System.out.println("onUserListDestroyed  listOwner:@"
            + listOwner.getScreenName()
            + " list:" + list.getName());
    }

   
    public void onUserProfileUpdate(User updatedUser) {
        System.out.println("onUserProfileUpdated user:@" + updatedUser.getScreenName());
    }

   
    public void onUserDeletion(long deletedUser) {
        System.out.println("onUserDeletion user:@" + deletedUser);
    }

   
    public void onUserSuspension(long suspendedUser) {
        System.out.println("onUserSuspension user:@" + suspendedUser);
    }

   
    public void onBlock(User source, User blockedUser) {
        System.out.println("onBlock source:@" + source.getScreenName()
            + " target:@" + blockedUser.getScreenName());
    }

   
    public void onUnblock(User source, User unblockedUser) {
        System.out.println("onUnblock source:@" + source.getScreenName()
            + " target:@" + unblockedUser.getScreenName());
    }

   
    public void onRetweetedRetweet(User source, User target, Status retweetedStatus) {
        System.out.println("onRetweetedRetweet source:@" + source.getScreenName()
            + " target:@" + target.getScreenName()
            + retweetedStatus.getUser().getScreenName()
            + " - " + retweetedStatus.getText());
    }

   
    public void onFavoritedRetweet(User source, User target, Status favoritedRetweet) {
        System.out.println("onFavroitedRetweet source:@" + source.getScreenName()
            + " target:@" + target.getScreenName()
            + favoritedRetweet.getUser().getScreenName()
            + " - " + favoritedRetweet.getText());
    }

   
    public void onQuotedTweet(User source, User target, Status quotingTweet) {
        System.out.println("onQuotedTweet" + source.getScreenName()
            + " target:@" + target.getScreenName()
            + quotingTweet.getUser().getScreenName()
            + " - " + quotingTweet.getText());
    }

   
    public void onException(Exception ex) {
        ex.printStackTrace();
        System.out.println("onException:" + ex.getMessage());
    }
    
}
