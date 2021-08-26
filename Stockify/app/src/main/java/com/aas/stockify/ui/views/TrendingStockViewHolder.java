package com.aas.stockify.ui.views;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aas.stockify.R;

public class TrendingStockViewHolder extends StockViewHolder {

    TextView price;
    TextView ltpPercentage;
    TextView targetPrice;
    TextView returns;

    public TrendingStockViewHolder(@NonNull View itemView) {
        super(itemView);

        price = itemView.findViewById(R.id.price);
        ltpPercentage = itemView.findViewById(R.id.ltp_percentage);
        targetPrice = itemView.findViewById(R.id.target_price);
        returns = itemView.findViewById(R.id.returns);
    }
}
