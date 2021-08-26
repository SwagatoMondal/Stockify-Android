package com.aas.stockify.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.aas.stockify.entity.Stock;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NetworkManager {

    private static final String TAG = NetworkManager.class.getSimpleName();

    private static final String ADD_USER_URL = "http://ab/addUser";
    private static final String ADD_ALERT_URL = "http://ab/addAlert";

    public static void addUser(@NonNull Context context,
                               @NonNull NetworkListener<String> listener,
                               @NonNull FirebaseUser user,
                               @NonNull String fcmToken) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", user.getUid());
            jsonBody.put("name", user.getDisplayName());
            jsonBody.put("mail", user.getEmail());
            jsonBody.put("device_id", fcmToken);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_USER_URL,
                    listener, listener) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            Log.e(TAG, "Exception occurred while creating an user.", e);
        }
    }

    public static void addAlert(@NonNull Context context,
                                @NonNull String userId,
                                @NonNull Stock stock,
                                boolean direction,
                                @NonNull NetworkListener<String> listener) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_id", userId);
            jsonBody.put("instrument_token", stock.getInstrumentId());
            jsonBody.put("exchange_token", stock.getExchangeId());
            jsonBody.put("target_price", stock.getTargetPrice());
            jsonBody.put("direction", direction);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_ALERT_URL,
                    listener, listener) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
