package com.zuzya.chat.legacy;

public interface ChatServiceListener {
	void onMessagesUpdates();

	void onStatusChanged(ChatService.ChatServiceStatus status);
}
