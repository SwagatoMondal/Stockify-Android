package com.aas.stockify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aas.stockify.databinding.ActivitySearchBinding;
import com.aas.stockify.entity.Stock;
import com.aas.stockify.ui.views.AdapterUtil;
import com.aas.stockify.ui.views.StockViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    private ActivitySearchBinding binding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(textListener);
        return super.onCreateOptionsMenu(menu);
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

    private FirestoreRecyclerOptions<Stock> getOptions(Query query) {
        return new FirestoreRecyclerOptions.Builder<Stock>()
                .setQuery(query, AdapterUtil.getParser())
                .build();
    }

    private void prepareRecyclerView() {
        Log.d(TAG, "Preparing Search RecyclerView");
        final RecyclerView recyclerView = findViewById(R.id.stocks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query = FirebaseFirestore.getInstance().collection("instruments");
        adapter = AdapterUtil.getAdapter(getOptions(query));
        recyclerView.setAdapter(adapter);
    }

    private void updateAdapterOptions(String stockName) {
        Query query = FirebaseFirestore.getInstance()
                .collection("instruments")
                .whereGreaterThanOrEqualTo("Name", stockName);
        adapter.updateOptions(getOptions(query));
    }
}