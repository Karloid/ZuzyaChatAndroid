package com.zuzya.chat.test.views;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zuzya.chat.R;
import com.zuzya.chat.legacy.ChatService;
import com.zuzya.chat.legacy.ChatServiceListener;
import com.zuzya.chat.legacy.Message;
import com.zuzya.chat.legacy.MessagesAdapter;
import com.zuzya.chat.test.viewmodels.ChatViewModel;
import com.zuzya.chat.test.viewmodels.HasViewModel;
import com.zuzya.chat.test.viewmodels.ViewModel;

import java.util.List;

import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ChatView extends RelativeLayout implements HasViewModel {
    private ChatViewModel viewModel;

    private EditText inputEditText;
    private Button sendButton;
    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;

    public ChatView(Context context) {
        super(context);
    }

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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

    private void bindViews() {
        inputEditText = (EditText) findViewById(R.id.chat_input_text);
        sendButton = (Button) findViewById(R.id.chat_send_button);

        recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        messagesAdapter = new MessagesAdapter();
        RecyclerView.Adapter adapter = messagesAdapter;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = (ChatViewModel) viewModel;
        subscribeViewModel();
    }

    private void subscribeViewModel() {
        viewModel.getMessages().subscribe(new Action1<List<Message>>() {
            @Override
            public void call(final List<Message> messages) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        messagesAdapter.setMessages(messages);
                    }
                });
            }
        });

        viewModel.getStatus().subscribe(new Action1<String>() {
            @Override
            public void call(final String s) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        showToast(s);
                    }
                });
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
