package com.aas.stockify.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aas.stockify.R;
import com.aas.stockify.databinding.FragmentSearchBinding;
import com.aas.stockify.entity.Stock;
import com.aas.stockify.ui.ItemClickListener;
import com.aas.stockify.ui.home.AlertDetailsActivity;
import com.aas.stockify.ui.views.AdapterUtil;
import com.aas.stockify.ui.views.StockAdapter;
import com.aas.stockify.ui.views.StockLayoutManager;
import com.aas.stockify.ui.views.StockViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchFragment extends Fragment implements ItemClickListener {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private FragmentSearchBinding binding;
    private FirestoreRecyclerAdapter<Stock, StockViewHolder> adapter;

    private final SearchView.OnQueryTextListener textListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            updateAdapterOptions(s);
            return false;
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        prepareRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.searchView.setOnQueryTextListener(textListener);

        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    private FirestoreRecyclerOptions<Stock> getOptions(Query query) {
        return new FirestoreRecyclerOptions.Builder<Stock>()
                .setQuery(query, AdapterUtil.getParser())
                .build();
    }

    private void prepareRecyclerView() {
        Log.d(TAG, "Preparing Search RecyclerView");
        final RecyclerView recyclerView = binding.stocks;
        recyclerView.setLayoutManager(new StockLayoutManager(getContext()));
        Query query = FirebaseFirestore.getInstance().collection("instruments").limit(100);
        adapter = new StockAdapter(getOptions(query), R.layout.home_stock_item, this);
        recyclerView.setAdapter(adapter);
    }

    private void updateAdapterOptions(String stockName) {
        stockName = stockName.toUpperCase();
        Query query = FirebaseFirestore.getInstance()
                .collection("test-trending")
                .whereEqualTo("Name", stockName)
                .whereGreaterThanOrEqualTo("Name", stockName)
                .whereLessThanOrEqualTo("Name", stockName + '\uf8ff');
        adapter.updateOptions(getOptions(query));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(Stock stock) {
        Intent intent = new Intent(getContext(), AlertDetailsActivity.class);
        intent.putExtra("name", stock.getName());
        intent.putExtra("exchange", stock.getExchange());
        intent.putExtra("symbol", stock.getSymbol());
        intent.putExtra("instrumentId", stock.getInstrumentId());
        intent.putExtra("exchangeId", stock.getExchangeId());
        intent.putExtra("created", false);
        startActivity(intent);
    }
}