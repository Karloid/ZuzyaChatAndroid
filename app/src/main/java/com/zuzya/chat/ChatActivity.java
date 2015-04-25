package com.zuzya.chat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ChatActivity extends Activity implements ChatServiceListener {

	private EditText inputEditText;
	private Button sendButton;
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		startService(new Intent(this, ChatService.class));
		bindViews();

		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String inputString = inputEditText.getText().toString();
				if (!inputString.isEmpty())
					ChatService.sendMessage(inputString);
				inputEditText.setText("");
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		ChatService.addChatServiceListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		ChatService.removeChatServiceListener(this);
	}

	private void bindViews() {
		inputEditText = (EditText) findViewById(R.id.chat_input_text);
		sendButton = (Button) findViewById(R.id.chat_send_button);

		recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setReverseLayout(true);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);

		RecyclerView.Adapter adapter = new MessagesAdapter(ChatService.getMessages());
		recyclerView.setItemAnimator(new DefaultItemAnimator());
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

	@Override
	public void onMessagesUpdates() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				recyclerView.getAdapter().notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onStatusChanged(final ChatService.ChatServiceStatus status) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (status == null) return;
				String newTitle = getString(R.string.app_name) + " - ";
				switch (status) {
					case CONNECTED:
						newTitle += getString(R.string.connected);
						break;
					case CONNECTING:
						newTitle += getString(R.string.connecting);
						break;
					case DISCONNECTED:
						newTitle += getString(R.string.disconnected);
						break;
				}
				ActionBar actionBar = getActionBar();
				if (actionBar != null)
					actionBar.setTitle(newTitle);
			}
		});
	}
}
