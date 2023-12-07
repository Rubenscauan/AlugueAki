package com.example.alugueaki.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.Models.Chat;
import com.example.alugueaki.databinding.ChatItemBinding;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private ArrayList<Chat> chatList;
    private final Context context;

    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(Chat chat);
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chatList = chats;
        notifyDataSetChanged();
    }


    public ChatAdapter(ArrayList<Chat> chatList, Context context, OnItemClickListener itemClickListener) {
        this.chatList = chatList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        ChatItemBinding binding;

        public ChatViewHolder(ChatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatItemBinding listItem;
        listItem = ChatItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ChatViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        holder.binding.destinatarioNome.setText(chat.getUltimaMensagem().getDestinatarioNome());
        holder.binding.ultimaMensagem.setText(chat.getUltimaMensagem().getConteudo());
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        //holder.binding.horaDaMensagem.setText(chat.getUltimaMensagem().getDataHoraEnvio().format(formatter));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                itemClickListener.onItemClicked(chat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}