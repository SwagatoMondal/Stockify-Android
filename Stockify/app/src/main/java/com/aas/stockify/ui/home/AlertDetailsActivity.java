package com.aas.stockify.ui.home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aas.stockify.R;
import com.aas.stockify.entity.Stock;
import com.aas.stockify.network.NetworkListener;
import com.aas.stockify.network.NetworkManager;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class AlertDetailsActivity extends AppCompatActivity implements NetworkListener<JSONObject> {

    private static final String TAG = AlertDetailsActivity.class.getSimpleName();

    private ProgressDialog dialog;
    private boolean created;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        dialog = new ProgressDialog(this);
        dialog.setTitle(R.string.loading);

        final Intent intent = getIntent();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && intent != null) {
            dialog.show();

            String name = intent.getStringExtra("name");
            String symbol = intent.getStringExtra("symbol");
            String instrumentId = intent.getStringExtra("instrumentId");
            String exchange = intent.getStringExtra("exchange");
            String exchangeId = intent.getStringExtra("exchangeId");
            this.created = intent.getBooleanExtra("created", false);

            Button button = findViewById(R.id.action);
            if (created) {
                button.setText(R.string.remove_alert);
            }
            button.setOnClickListener(v -> {
                if (created) {
                    removeDialog();
                } else {
                    showCreateDialog(user.getUid(), instrumentId, exchangeId);
                }
            });

            TextView nameTv = findViewById(R.id.name);
            nameTv.setText(name);

            NetworkManager.fetchStockDetails(this, exchange, symbol, this);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        dialog.dismiss();
        Log.w(TAG, "Error fetching stock details", error);
        Toast.makeText(this, R.string.other_failure, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onResponse(JSONObject response) {
        System.out.println(response.toString());
        TextView price = findViewById(R.id.price);
        TextView open = findViewById(R.id.open);
        TextView close = findViewById(R.id.close);
        TextView volume = findViewById(R.id.volume);
        TextView high = findViewById(R.id.high);
        TextView low = findViewById(R.id.low);
        TextView avg_price = findViewById(R.id.avg_price);
        TextView last_quantity = findViewById(R.id.last_quantity);
        TextView buy_quantity = findViewById(R.id.buy_quantity);
        TextView lower_circuit = findViewById(R.id.lower_circuit);
        TextView upper_circuit = findViewById(R.id.upper_circuit);

        try {
            response = response.getJSONObject("message");
            price.setText(response.getString("last_price"));

            final JSONObject oHLC = response.getJSONObject("ohlc");
            open.setText(oHLC.getString("open"));
            close.setText(oHLC.getString("close"));
            high.setText(oHLC.getString("high"));
            low.setText(oHLC.getString("low"));

            volume.setText(response.getString("volume"));
            avg_price.setText(response.getString("average_price"));
            last_quantity.setText(response.getString("last_quantity"));
            buy_quantity.setText(response.getString("buy_quantity"));
            lower_circuit.setText(response.getString("lower_circuit_limit"));
            upper_circuit.setText(response.getString("upper_circuit_limit"));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            dialog.dismiss();
        }
    }

    private void showCreateDialog(String userId, String instrumentId, String exchangeId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_alert);
        final View root = LayoutInflater.from(this).inflate(R.layout.layout_add_alert, null, false);
        builder.setView(root);

        Spinner spinner = root.findViewById(R.id.operators);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.operators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setPositiveButton(R.string.create, (dialog, which) -> {

            AlertDetailsActivity.this.dialog.show();
            boolean direction = spinner.getSelectedItemPosition() == 0;
            Stock stock = new Stock();
            stock.setInstrumentId(instrumentId);
            stock.setExchangeId(exchangeId);

            TextInputEditText priceEdit = root.findViewById(R.id.price);
            stock.setTargetPrice(Float.parseFloat(priceEdit.getText().toString()));

            NetworkManager.addAlert(AlertDetailsActivity.this,
                    userId, stock, direction, new NetworkListener<String>() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AlertDetailsActivity.this.dialog.dismiss();
                            Log.w(TAG, "Error creating alert", error);
                        }

                        @Override
                        public void onResponse(String response) {
                            AlertDetailsActivity.this.dialog.dismiss();
                        }
                    });
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.show();
    }

    private void removeDialog() {

    }
}