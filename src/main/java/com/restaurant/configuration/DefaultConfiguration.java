package com.restaurant.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.restaurant.dao.DBConnector;
import com.restaurant.twitter.TwitterConnector;

@Configuration
public class DefaultConfiguration {
		
	@Bean 
	public DBConnector getDBConnection(){
		return new DBConnector();
	}

	@Bean
	public TwitterConnector twitterConnector(DBConnector dbConnector) {
		return new TwitterConnector(dbConnector);
	}

}
