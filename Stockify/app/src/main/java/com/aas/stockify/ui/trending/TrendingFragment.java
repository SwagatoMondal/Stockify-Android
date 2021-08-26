package com.aas.stockify.ui.trending;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aas.stockify.R;
import com.aas.stockify.databinding.FragmentTrendingBinding;
import com.aas.stockify.entity.Stock;
import com.aas.stockify.ui.views.AdapterUtil;
import com.aas.stockify.ui.views.StockViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TrendingFragment extends Fragment {

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
        binding.trending.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = FirebaseFirestore.getInstance().collection("trending");
        FirestoreRecyclerOptions<Stock> options = new FirestoreRecyclerOptions.Builder<Stock>()
                .setQuery(query, AdapterUtil.getParser())
                .build();
        adapter = AdapterUtil.getAdapter(options, R.layout.trending_stock_item);
        binding.trending.setAdapter(adapter);
    }
}