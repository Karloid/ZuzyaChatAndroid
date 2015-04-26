package com.zuzya.chat;

public class Utils {
	public static String getHostname(String ip) {
		return "http://" + ip +":8081" + "/chat/";
	}
}
