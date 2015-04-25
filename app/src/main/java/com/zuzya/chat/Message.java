package com.zuzya.chat;

public class Message {
	private final String text;
	private final Type type;

	public Message(String text) {
		this(text, Type.NORMAL_MESSAGE);
	}

	public Message(String text, Type techInfo) {
		this.text = text;
		type = techInfo;
	}

	public String getText() {
		return text;
	}

	public Type getType() {
		return type;
	}

	public enum Type {NORMAL_MESSAGE, TECH_INFO}
}
