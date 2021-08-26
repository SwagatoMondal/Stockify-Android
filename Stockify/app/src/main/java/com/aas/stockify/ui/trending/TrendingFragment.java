package com.aas.stockify.ui.trending;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aas.stockify.R;
import com.aas.stockify.databinding.FragmentTrendingBinding;
import com.aas.stockify.entity.Stock;
import com.aas.stockify.ui.ItemClickListener;
import com.aas.stockify.ui.views.AdapterUtil;
import com.aas.stockify.ui.views.StockAdapter;
import com.aas.stockify.ui.views.StockLayoutManager;
import com.aas.stockify.ui.views.StockViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TrendingFragment extends Fragment implements ItemClickListener {

    private static final String TAG = TrendingFragment.class.getSimpleName();

    private FragmentTrendingBinding binding;

    private FirestoreRecyclerAdapter<Stock, StockViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTrendingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prepareRecyclerView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    /**
     * Method to load user selected (interested in) stock information
     */
    private void prepareRecyclerView() {
        Log.d(TAG, "Preparing Trending RecyclerView");
        binding.trending.setLayoutManager(new StockLayoutManager(getContext()));
        Query query = FirebaseFirestore.getInstance().collection("trending")
                .orderBy("ExpectedReturnsPerc", Query.Direction.DESCENDING)
                .limit(20);
        FirestoreRecyclerOptions<Stock> options = new FirestoreRecyclerOptions.Builder<Stock>()
                .setQuery(query, AdapterUtil.getParser())
                .build();
        adapter = new StockAdapter(options, R.layout.trending_stock_item, this);
        binding.trending.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(Stock stock) {

    }
}