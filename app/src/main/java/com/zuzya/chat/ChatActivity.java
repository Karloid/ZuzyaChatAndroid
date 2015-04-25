package com.zuzya.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;


public class ChatActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		AsyncHttpClient.getDefaultInstance().websocket(Utils.getHostname(), null,
				new AsyncHttpClient.WebSocketConnectCallback() {
					@Override
					public void onCompleted(Exception ex, WebSocket webSocket) {
						if (ex != null) {
							ex.printStackTrace();
							return;
						}
						webSocket.send("Lol");
						webSocket.setStringCallback(new WebSocket.StringCallback() {
							@Override
							public void onStringAvailable(String s) {
								System.out.println("new string: " + s);
							}
						});
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
