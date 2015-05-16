package com.zuzya.chat.legacy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zuzya.chat.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatService extends Service {
	private static WebSocket chatWebSocket;
	private static ChatServiceStatus status = ChatServiceStatus.DISCONNECTED;
	final String LOG_TAG = "Zuzya";
	private static List<Message> messages = new ArrayList<Message>();
	private static List<ChatServiceListener> listeners =  new ArrayList<ChatServiceListener>();
	private static Context baseContext;

	public static List<Message> getMessages() {
		return messages;
	}

	static void setStatus(ChatServiceStatus status) {
		ChatService.status = status;
		notifyOnStatusChanged();
		messages.add(0, new Message(baseContext.getString(status.getStringId()), Message.Type.TECH_INFO));
		notifyOnMessagesUpdatedListeners();
	}

	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "onCreate");
		baseContext = getBaseContext();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand_test");
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
		if (status != ChatServiceStatus.DISCONNECTED) return;
		ChatService.setStatus(ChatServiceStatus.CONNECTING);

		new Thread(new Runnable() {  //TODO handle errors
			@Override
			public void run() {
				String ip = download("https://raw.githubusercontent.com/Karloid/SomeInfo/master/doc");
				startWebsocket(ip);
			}
		}).start();
	}

	private void startWebsocket(String ip) {   //TODO rework
		AsyncHttpClient.getDefaultInstance().websocket(Utils.getHostname(ip), null,
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

	String download(String url) {   //TODO use androidAsync
		try {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url(url)
					.build();

			Response response = client.newCall(request).execute();

			return response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
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

	public static ChatServiceStatus getStatus() {
		return status;
	}

	public enum ChatServiceStatus {
		CONNECTED(R.string.connected), CONNECTING(R.string.connecting), DISCONNECTED(R.string.disconnected);

		private int stringId;

		ChatServiceStatus(int stringId) {
			this.stringId = stringId;
		}

		public int getStringId() {
			return stringId;
		}
	}
}
