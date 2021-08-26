package com.aas.stockify.network;

import com.android.volley.Response;

public interface NetworkListener<T> extends Response.Listener<T>, Response.ErrorListener {
}
