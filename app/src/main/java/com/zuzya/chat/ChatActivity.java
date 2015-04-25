package com.zuzya.chat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends Activity {

	private EditText inputEditText;
	private Button sendButton;
	private RecyclerView recyclerView;
	private List<Message> messages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		bindViews();

		AsyncHttpClient.getDefaultInstance().websocket(Utils.getHostname(), null,
				new AsyncHttpClient.WebSocketConnectCallback() {
					@Override
					public void onCompleted(Exception ex, final WebSocket webSocket) {
						if (ex != null) {
							ex.printStackTrace();
							return;
						}
						webSocket.setStringCallback(new WebSocket.StringCallback() {
							@Override
							public void onStringAvailable(final String s) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										messages.add(0,new Message(s));
										recyclerView.getAdapter().notifyDataSetChanged();
									}
								});

							}
						});

						sendButton.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								String inputString = inputEditText.getText().toString();
								if (!inputString.isEmpty())
									webSocket.send(inputString);
								inputEditText.setText("");
							}
						});
					}
				});
	}

	private void bindViews() {
		inputEditText = (EditText) findViewById(R.id.chat_input_text);
		sendButton = (Button) findViewById(R.id.chat_send_button);

		recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setReverseLayout(true);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);

		messages = new ArrayList<Message>();
		RecyclerView.Adapter adapter = new MessagesAdapter(messages);
		recyclerView.setItemAnimator( new DefaultItemAnimator());
		recyclerView.setAdapter(adapter);
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
