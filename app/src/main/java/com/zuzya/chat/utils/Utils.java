package com.zuzya.chat.utils;

public class Utils {
	public static String getHostname(String ip) {
		return "http://" + ip +":8081" + "/chat/";
	}
}
