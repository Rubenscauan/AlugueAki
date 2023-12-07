package com.example.alugueaki.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.alugueaki.databinding.ItemMessagemDestinatarioBinding;
import com.example.alugueaki.databinding.ItemMessagemRemetenteBinding;
import com.example.alugueaki.Models.Mensagem;


import java.util.ArrayList;

public class MensagemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Mensagem> mensagens;
    private final Context context;

    // Tipos de visualização para remetentes e destinatários
    private static final int VIEW_TYPE_REMETENTE = 0;
    private static final int VIEW_TYPE_DESTINATARIO = 1;

    public MensagemAdapter(ArrayList<Mensagem> mensagens, Context context) {
        this.mensagens = mensagens;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_REMETENTE) {
            // Layout para remetente
            return new RemetenteViewHolder(
                    ItemMessagemRemetenteBinding.inflate(inflater, parent, false)
            );
        } else {
            // Layout para destinatário
            return new DestinatarioViewHolder(
                    ItemMessagemDestinatarioBinding.inflate(inflater, parent, false)
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_REMETENTE) {
            // Lidar com a visualização do remetente
            RemetenteViewHolder remetenteHolder = (RemetenteViewHolder) holder;
            remetenteHolder.binding.RemetenteTextView.setText(mensagem.getRemetenteNome());
            remetenteHolder.binding.mensagemTextView.setText(mensagem.getConteudo());
        } else {
            DestinatarioViewHolder destinatarioHolder = (DestinatarioViewHolder) holder;
            destinatarioHolder.binding.textDestinatario.setText(mensagem.getRemetenteNome());
            destinatarioHolder.binding.textMessagem.setText(mensagem.getConteudo());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Mensagem mensagem = mensagens.get(position);
        if (mensagem.GetIsRemetente()) {
            return VIEW_TYPE_REMETENTE;
        } else {
            return VIEW_TYPE_DESTINATARIO;
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    public static class RemetenteViewHolder extends RecyclerView.ViewHolder {
        ItemMessagemRemetenteBinding binding;

        public RemetenteViewHolder(ItemMessagemRemetenteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class DestinatarioViewHolder extends RecyclerView.ViewHolder {
        ItemMessagemDestinatarioBinding binding;

        public DestinatarioViewHolder(ItemMessagemDestinatarioBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
