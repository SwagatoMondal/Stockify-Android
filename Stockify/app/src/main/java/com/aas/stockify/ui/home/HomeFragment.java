package com.aas.stockify.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aas.stockify.R;
import com.aas.stockify.databinding.FragmentHomeBinding;
import com.aas.stockify.entity.Stock;
import com.aas.stockify.ui.ItemClickListener;
import com.aas.stockify.ui.views.AdapterUtil;
import com.aas.stockify.ui.views.StockAdapter;
import com.aas.stockify.ui.views.StockViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment implements ItemClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private FragmentHomeBinding binding;

    private FirestoreRecyclerAdapter<Stock, StockViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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
        Log.d(TAG, "Preparing Home RecyclerView");
        binding.stocks.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO point to user level stocks only
        Query query = FirebaseFirestore.getInstance().collection("instruments").
                limit(50);
        FirestoreRecyclerOptions<Stock> options = new FirestoreRecyclerOptions.Builder<Stock>()
                .setQuery(query, AdapterUtil.getParser())
                .build();
        adapter = new StockAdapter(options, R.layout.home_stock_item, this);
        binding.stocks.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(Stock stock) {

    }
}