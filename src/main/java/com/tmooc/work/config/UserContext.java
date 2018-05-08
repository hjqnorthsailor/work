package com.tmooc.work.config;


import com.tmooc.work.entity.User;

/**
 * User上下文
 */
public class UserContext {
	
	private static ThreadLocal<User> userHolder = new ThreadLocal<User>();
	
	public static void setUser(User user) {
		userHolder.set(user);
	}
	
	public static User getUser() {
		return userHolder.get();
	}

}
