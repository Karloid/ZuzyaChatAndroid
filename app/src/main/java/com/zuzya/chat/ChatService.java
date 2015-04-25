package com.zuzya.chat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.util.ArrayList;
import java.util.List;

public class ChatService extends Service {
	private static WebSocket chatWebSocket;
	private static ChatServiceStatus status;
	final String LOG_TAG = "Zuzya";
	private static List<Message> messages;
	private static List<ChatServiceListener> listeners;

	public static List<Message> getMessages() {
		if (messages == null) {
			messages = new ArrayList<Message>();
			listeners = new ArrayList<ChatServiceListener>();
		}
		return messages;
	}

	static void setStatus(ChatServiceStatus status) {
		ChatService.status = status;
		notifyOnStatusChanged();
	}

	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "onCreate");
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand");
		someTask();
		return super.onStartCommand(intent, flags, startId);
	}

	public void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, "onDestroy");
	}

	public IBinder onBind(Intent intent) {
		Log.d(LOG_TAG, "onBind");
		return null;
	}

	void someTask() {
		if (chatWebSocket != null && chatWebSocket.isOpen())  return;
		ChatService.setStatus(ChatServiceStatus.CONNECTING);
		AsyncHttpClient.getDefaultInstance().websocket(Utils.getHostname(), null,
				new AsyncHttpClient.WebSocketConnectCallback() {
					@Override
					public void onCompleted(Exception ex, final WebSocket webSocket) {
						ChatService.chatWebSocket = webSocket;
						if (ex != null) {
							ex.printStackTrace(System.err);
							ChatService.setStatus(ChatServiceStatus.DISCONNECTED);
							return;
						}
						webSocket.setClosedCallback(new CompletedCallback() {
							@Override
							public void onCompleted(Exception ex) {
								if (ex != null) {
									ex.printStackTrace(System.err);
								}
								ChatService.setStatus(ChatServiceStatus.DISCONNECTED);
							}
						});
						ChatService.setStatus(ChatServiceStatus.CONNECTED);
						webSocket.setStringCallback(new WebSocket.StringCallback() {
							@Override
							public void onStringAvailable(final String s) {
								messages.add(0, new Message(s));
								ChatService.notifyOnMessagesUpdatedListeners();
							}
						});
					}
				});
	}

	private static void notifyOnMessagesUpdatedListeners() {
		for (ChatServiceListener listener : listeners) {
			listener.onMessagesUpdates();
		}
	}

	private static void notifyOnStatusChanged() {
		for (ChatServiceListener listener : listeners) {
			listener.onStatusChanged(status);
		}
	}

	public static void sendMessage(String inputString) {
		chatWebSocket.send(inputString);
	}

	public static void addChatServiceListener(ChatServiceListener listener) {
		listeners.add(listener);
		listener.onMessagesUpdates();
		listener.onStatusChanged(status);
	}

	public static void removeChatServiceListener(ChatServiceListener listener) {
		listeners.remove(listener);
	}

	public enum ChatServiceStatus {
		CONNECTED, CONNECTING, DISCONNECTED
	}
}
