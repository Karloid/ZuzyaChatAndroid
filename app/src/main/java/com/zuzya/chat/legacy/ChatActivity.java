package com.zuzya.chat.legacy;

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
import android.widget.Toast;

import com.zuzya.chat.R;


public class ChatActivity extends Activity implements ChatServiceListener {

	private EditText inputEditText;
	private Button sendButton;
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		startService();
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

	private void startService() {
		startService(new Intent(this, ChatService.class));
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

		RecyclerView.Adapter adapter = new MessagesAdapter();
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
		switch (item.getItemId()) {
			case R.id.menu_reconnect:
				startService();
				return true;
			case R.id.menu_settings:
				//TODO
				return true;
			default:
				break;
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
				String newStatus = getString(status.getStringId());
				showToast(newStatus);
				ActionBar actionBar = getActionBar();
				if (actionBar != null)
					actionBar.setTitle(getString(R.string.app_name) + " - " + newStatus);
			}
		});
	}

	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
