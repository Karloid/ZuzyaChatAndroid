package com.zuzya.chat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zuzya.chat.R;
import com.zuzya.chat.models.Message;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private static final int NORMAL_MESSAGE_VIEW = 0;
    private static final int TECH_INFO_VIEW = 1;

    private List<Message> messages;

    public MessagesAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == TECH_INFO_VIEW)  //TODO enum
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.li_message_tech_info, viewGroup, false);
        else
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.li_message, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(messages.get(i).getText());
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType() == Message.Type.NORMAL_MESSAGE ? NORMAL_MESSAGE_VIEW : TECH_INFO_VIEW;
    }

    @Override
    public int getItemCount() {
        if (messages == null) return 0;
        return messages.size();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(android.R.id.text1);
        }
    }
}
