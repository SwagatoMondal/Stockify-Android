package com.aas.stockify.ui.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aas.stockify.R;
import com.aas.stockify.databinding.FragmentSearchBinding;
import com.aas.stockify.entity.Stock;
import com.aas.stockify.ui.ItemClickListener;
import com.aas.stockify.ui.views.AdapterUtil;
import com.aas.stockify.ui.views.StockAdapter;
import com.aas.stockify.ui.views.StockViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.lang.ref.WeakReference;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = FirebaseFirestore.getInstance().collection("test-trending");
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
        final Context context = getContext();
        if (null == context) {
            // TODO show Toast
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.add_alert);
        final View root = LayoutInflater.from(context).inflate(R.layout.layout_add_alert, null, false);
        builder.setView(root);

        Spinner spinner = root.findViewById(R.id.operators);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.operators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setPositiveButton(R.string.create, (dialog, which) -> {

        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.show();
    }
}