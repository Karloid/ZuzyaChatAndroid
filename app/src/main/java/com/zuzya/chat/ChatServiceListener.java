package com.zuzya.chat;

public interface ChatServiceListener {
	void onMessagesUpdates();

	void onStatusChanged(ChatService.ChatServiceStatus status);
}
