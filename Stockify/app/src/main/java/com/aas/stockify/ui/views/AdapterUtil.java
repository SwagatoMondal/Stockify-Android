package com.aas.stockify.ui.views;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aas.stockify.R;
import com.aas.stockify.entity.Stock;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;

public class AdapterUtil {

    public static SnapshotParser<Stock> getParser() {
        return snapshot -> {
            // {instrument_token=277876486, name=EURINR, exchange_token=1085455, tradingsymbol=EURAPR22JUL22, last_price=0}
            Stock stock = new Stock();
            stock.setName(snapshot.getString("name"));
            stock.setSymbol(snapshot.getString("tradingsymbol"));
            stock.setPrice(snapshot.getDouble("last_price").floatValue());
            stock.setExchange(snapshot.getString("exchange"));
            return stock;
        };
    }

    public static FirestoreRecyclerAdapter<Stock, StockViewHolder> getAdapter(
            FirestoreRecyclerOptions<Stock> options) {
        return new FirestoreRecyclerAdapter<Stock, StockViewHolder>(options) {
            @Override
            public void onBindViewHolder(@NonNull StockViewHolder holder, int position,
                                         @NonNull Stock model) {
                holder.name.setText(model.getName());
                holder.symbol.setText(model.getSymbol());
                holder.exchange.setText(model.getExchange());
            }

            @NonNull
            @Override
            public StockViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.home_stock_item, group, false);

                return new StockViewHolder(view);
            }
        };
    }
}
