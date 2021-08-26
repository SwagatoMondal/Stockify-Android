package com.aas.stockify.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.aas.stockify.R;
import com.aas.stockify.entity.Stock;
import com.aas.stockify.ui.ItemClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.lang.ref.WeakReference;

public class StockAdapter extends FirestoreRecyclerAdapter<Stock, StockViewHolder> {

    @LayoutRes
    private final int layout;
    private WeakReference<ItemClickListener> listenerRef;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options The FireStore options
     * @param layout The item layout
     */
    public StockAdapter(@NonNull FirestoreRecyclerOptions<Stock> options, @LayoutRes int layout,
                        @NonNull ItemClickListener listener) {
        super(options);
        this.layout = layout;
        this.listenerRef = new WeakReference<>(listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position,
                                 @NonNull Stock model) {
        holder.name.setText(model.getName());
        holder.symbol.setText(model.getSymbol());
        holder.exchange.setText(model.getExchange());

        if (holder instanceof TrendingStockViewHolder) {
            TrendingStockViewHolder trending = (TrendingStockViewHolder) holder;
            trending.price.setText("INR " + model.getPrice());
            trending.targetPrice.setText("INR " + model.getTargetPrice());

            float percent = model.getLtpChangePercentage();
            trending.ltpPercentage.setText(percent + " %");
            trending.ltpPercentage.setTextColor(percent >= 0 ? Color.GREEN : Color.RED);

            float returns = model.getReturns();
            trending.returns.setText(returns + " %");
            trending.returns.setTextColor(returns >= 0 ? Color.GREEN : Color.RED);
        }

        holder.itemView.setOnClickListener(v -> {
            ItemClickListener listener = listenerRef.get();
            if (listener != null) {
                listener.onItemSelected(model);
            }
        });
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        // Create a new instance of the ViewHolder, in this case we are using a custom
        // layout called R.layout.message for each item
        View view = LayoutInflater.from(group.getContext())
                .inflate(layout, group, false);

        return R.layout.home_stock_item == layout ? new StockViewHolder(view) :
                new TrendingStockViewHolder(view);
    }
}
