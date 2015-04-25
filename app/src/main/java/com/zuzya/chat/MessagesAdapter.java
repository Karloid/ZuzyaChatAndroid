package com.zuzya.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

	private final List<Message> messages;

	public MessagesAdapter(List<Message> messages) {
		this.messages = messages;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.li_message, viewGroup, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int i) {
	   viewHolder.textView.setText(messages.get(i).getText());
	}

	@Override
	public int getItemCount() {
		return messages.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		public final TextView textView;

		public ViewHolder(View v) {
			super(v);
			textView = (TextView) v.findViewById(android.R.id.text1);
		}
	}
}
