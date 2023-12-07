package com.example.alugueaki.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.R;
import com.example.alugueaki.databinding.CasaItemBinding;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class CasaAdapter extends RecyclerView.Adapter<CasaAdapter.CasaViewHolder> {
    private final ArrayList<Casa> casaList;
    private final Context context;

    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(Casa casa);
    }


    public CasaAdapter(ArrayList<Casa> casaList, Context context, OnItemClickListener itemClickListener) {
        this.casaList = casaList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public static class CasaViewHolder extends RecyclerView.ViewHolder{

        CasaItemBinding binding;

        public CasaViewHolder(CasaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    @NonNull
    @Override
    public CasaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CasaItemBinding listItem;
        listItem = CasaItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new CasaViewHolder(listItem);
    }

    @Override //Qualquer coisa alterar aki
    public void onBindViewHolder(@NonNull CasaViewHolder holder, int position) {
        Casa casa = casaList.get(position);

        // Carregue a imagem usando o Glide

        Glide.with(context)
                .load(casa.getImagemURL())  // Substitua getImagemUrl() pelo método correto em seu modelo Casa que fornece a URL ou o recurso da imagem
                .centerCrop()
                .placeholder(R.drawable.casa1)  // Substitua R.drawable.placeholder pelo seu recurso de placeholder
                .into(holder.binding.ImagemCasa);


        holder.binding.txtDescricao.setText(casa.getDescricao());
        holder.binding.txtLocalizacao.setText(casa.getEndereco());
        holder.binding.txtPrice.setText(casa.getAluguel());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                itemClickListener.onItemClicked(casa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return casaList.size();
    }
}
