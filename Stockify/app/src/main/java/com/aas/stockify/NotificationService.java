package com.aas.stockify;

import android.util.Log;

import androidx.annotation.NonNull;

import com.aas.stockify.network.NetworkListener;
import com.aas.stockify.network.NetworkManager;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class NotificationService extends FirebaseMessagingService implements NetworkListener<String> {

    private static final String TAG = NotificationService.class.getSimpleName();

    @Override
    public void onNewToken(@NonNull String s) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            NetworkManager.addUser(this, this, user, s);
        } else {
            Log.e(TAG, "UserDetails successfully written!");
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {

    }
}
