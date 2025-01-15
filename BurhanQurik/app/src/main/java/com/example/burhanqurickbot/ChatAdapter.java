package com.example.burhanqurickbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    private Context context;
    private List<ChatMessage> messages;

    public ChatAdapter(@NonNull Context context, @NonNull List<ChatMessage> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage message = getItem(position);

        // Inflate the appropriate layout based on sender type
        if ("User".equals(message.getSender())) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_bot, parent, false);
        }

        // Set the message text
        TextView tvMessage = convertView.findViewById(R.id.tvMessage);
        tvMessage.setText(message.getMessage());

        return convertView;
    }
}
