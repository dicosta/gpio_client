package com.dicosta.gpioclient.api;

import com.dicosta.gpioclient.domain.Light;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by diego on 22/01/18.
 */

public class WSClient {

    public interface WSLightClientListener {
        void onLightsReceived(List<Light> lights);
    }

    private OkHttpClient mOkHttpClient;
    private boolean mConnected;
    private LightWebSocketListener mWSListener;
    private WebSocket mWebSocket;
    private WSLightClientListener mListener;
    private Gson mGson;

    public WSClient() {
        mOkHttpClient = new OkHttpClient();
        mGson = new Gson();
    }

    public void connect(WSLightClientListener listener) {
        if (!mConnected) {
            mListener = listener;
            Request request = new Request.Builder().url("ws://192.168.1.10:8080").build();
            mWSListener  = new LightWebSocketListener(mListener);
            mWebSocket = mOkHttpClient.newWebSocket(request, mWSListener);
            mConnected = true;
        }
    }

    public void send(int id, @Light.LightState String state) {
        if (mConnected) {
            mWebSocket.send(mGson.toJson(new LightWriteAction(id, state)));
        }
    }

    public void disconnect() {
        // Trigger shutdown of the dispatcher's executor so this process can
        // exit cleanly.
        //mOkHttpClient.dispatcher().executorService().shutdown();

        if (mConnected) {
            mWebSocket.close(1000, "Disconnecting");
            mConnected = false;
        }
    }

    static class LightWebSocketListener extends WebSocketListener {
        private Gson mGson;
        private WeakReference<WSLightClientListener> mListener;

        public LightWebSocketListener(WSLightClientListener listener) {
            mListener = new WeakReference<>(listener);
            mGson = new Gson();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send(mGson.toJson(new LightReadAction()));
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            if (mListener.get() != null) {
                Type listType = new TypeToken<ArrayList<Light>>(){}.getType();
                List<Light> retList = mGson.fromJson(text, listType);
                mListener.get().onLightsReceived(retList);
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("Receiving: " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            System.out.println("Closing: " + code + " " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            t.printStackTrace();
        }
    }
}
