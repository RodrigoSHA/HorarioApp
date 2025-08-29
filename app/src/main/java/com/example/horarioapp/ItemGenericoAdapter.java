package com.example.horarioapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class ItemGenericoAdapter extends RecyclerView.Adapter<ItemGenericoAdapter.ItemViewHolder> {

    private Context context;
    private List<Map<String, String>> items;
    private String tipo;

    public ItemGenericoAdapter(Context context, List<Map<String, String>> items, String tipo) {
        this.context = context;
        this.items = items;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_generico, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Map<String, String> item = items.get(position);

        holder.textPrincipal.setText(item.getOrDefault("principal", ""));
        holder.textSecundario.setText(item.getOrDefault("secundario", ""));
        holder.textTerciario.setText(item.getOrDefault("terciario", ""));

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, tipo + ": " + item.getOrDefault("principal", ""), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textPrincipal, textSecundario, textTerciario;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textPrincipal = itemView.findViewById(R.id.text_principal);
            textSecundario = itemView.findViewById(R.id.text_secundario);
            textTerciario = itemView.findViewById(R.id.text_terciario);
        }
    }
}
