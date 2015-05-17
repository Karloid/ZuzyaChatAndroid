package com.zuzya.chat.viewmodels;

import android.content.Context;
import android.content.Intent;

import com.zuzya.chat.services.ChatService;
import com.zuzya.chat.services.ChatServiceListener;
import com.zuzya.chat.models.Message;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class ChatViewModel extends BaseViewModel implements ChatServiceListener {
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
