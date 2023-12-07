package com.example.alugueaki.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alugueaki.Models.Casa;
import com.example.alugueaki.databinding.MinhasCasasItemBinding;

import java.util.ArrayList;

public class MinhasCasasAdapter extends RecyclerView.Adapter<MinhasCasasAdapter.CasaViewHolder>{


    private ArrayList<Casa> casaList;
    private final Context context;

    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(Casa casa);
    }

    public void setCasas(ArrayList<Casa> casas) {
        this.casaList = casas;
        notifyDataSetChanged();
    }

    public MinhasCasasAdapter(ArrayList<Casa> casaList, Context context, MinhasCasasAdapter.OnItemClickListener itemClickListener) {
        this.casaList = casaList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public static class CasaViewHolder extends RecyclerView.ViewHolder{

        MinhasCasasItemBinding binding;



        public CasaViewHolder(MinhasCasasItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    @NonNull
    @Override
    public CasaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       MinhasCasasItemBinding listItem = MinhasCasasItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CasaViewHolder(listItem);
    }

    @Override //Qualquer coisa alterar aki
    public void onBindViewHolder(@NonNull CasaViewHolder holder, int position) {
        Casa casa = casaList.get(position);

        // Carregue a imagem usando o Glide
        holder.binding.ImagemCasa.setImageResource(casa.getImagem());


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
