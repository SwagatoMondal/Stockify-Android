package com.aas.stockify.ui.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.aas.stockify.R;
import com.aas.stockify.entity.Stock;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;

public class AdapterUtil {

    public static SnapshotParser<Stock> getParser() {
        return snapshot -> {
            Stock stock = new Stock();
            stock.setName(snapshot.getString("Name"));
            stock.setSymbol(snapshot.getString("Symbol"));

            Double price = snapshot.getDouble("Price");
            if (price == null) {
                price = snapshot.getDouble("CurrentPrice");
            }
            stock.setPrice(price == null ? 0.0f : price.floatValue());

            stock.setExchange(snapshot.getString("Exchange"));

            Double percent = snapshot.getDouble("LTPChangePerc");
            stock.setLtpChangePercentage(percent == null ? 0.0f : percent.floatValue());

            Double returns = snapshot.getDouble("ExpectedReturnsPerc");
            stock.setReturns(returns == null ? 0.0f : returns.floatValue());

            Double target = snapshot.getDouble("TargetPrice");
            stock.setTargetPrice(target == null ? 0.0f : target.floatValue());

            return stock;
        };
    }

    public static FirestoreRecyclerAdapter<Stock, StockViewHolder> getAdapter(
            FirestoreRecyclerOptions<Stock> options,
            @LayoutRes final int layout) {
        return new StockAdapter(options, layout);
    }
}
