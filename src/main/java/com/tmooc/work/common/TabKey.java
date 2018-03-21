package com.tmooc.work.common;

public class TabKey extends BasePrefix {

	public TabKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static TabKey tabListKey= new TabKey(0, "tb");

}
