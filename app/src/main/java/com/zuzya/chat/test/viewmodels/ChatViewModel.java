package com.zuzya.chat.test.viewmodels;

import android.content.Context;
import android.content.Intent;

import com.zuzya.chat.legacy.ChatService;
import com.zuzya.chat.legacy.ChatServiceListener;
import com.zuzya.chat.legacy.Message;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class ChatViewModel implements ViewModel, ChatServiceListener {
    private final BehaviorSubject<String> status;
    private final BehaviorSubject<List<Message>> messages;
    private Context context;

    public ChatViewModel(Context context) {
        this.context = context;
        messages = BehaviorSubject.create((List<Message>)new ArrayList<Message>());
        status = BehaviorSubject.create("");

        startService();
        ChatService.addChatServiceListener(this);

    }

    private void startService() {
        context.startService(new Intent(context, ChatService.class));
    }

    @Override
    public void onMessagesUpdates() {
        messages.onNext(ChatService.getMessages());
    }

    @Override
    public void onStatusChanged(final ChatService.ChatServiceStatus newStatus) {
        this.status.onNext(context.getString(newStatus.getStringId()));
    }

    public Observable<List<Message>> getMessages() {
        return messages;
    }

    public Observable<String> getStatus() {
        return status;
    }
}
