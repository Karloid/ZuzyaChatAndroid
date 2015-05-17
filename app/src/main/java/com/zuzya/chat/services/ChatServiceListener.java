package com.zuzya.chat.services;

public interface ChatServiceListener {
	void onMessagesUpdates();

	void onStatusChanged(ChatService.ChatServiceStatus status);
}
