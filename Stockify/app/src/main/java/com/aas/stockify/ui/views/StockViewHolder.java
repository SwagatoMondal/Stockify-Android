package com.aas.stockify.ui.views;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aas.stockify.R;

public class StockViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView symbol;
    TextView exchange;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        symbol = itemView.findViewById(R.id.symbol);
        exchange = itemView.findViewById(R.id.exchange);
    }
}
