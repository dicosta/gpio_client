package com.dicosta.gpioclient.net;

import android.os.Handler;
import android.os.Looper;

import com.dicosta.gpioclient.BuildConfig;
import com.dicosta.gpioclient.domain.Light;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

@Singleton
public class WSClient {
    private OkHttpClient mOkHttpClient;
    private Gson mGson;
    private Request mWsRequest;
    private WebSocket mWebSocket;
    private boolean mConnected;
    private WeakReference<WSClientListener> mListenerWeakReference;

    public interface WSClientListener {
        void onLightsReceived(List<Light> lights);
    }

    @Inject
    WSClient(HttpClientProvider httpClientProvider, SerializerProvider serializerProvider) {
        mOkHttpClient = httpClientProvider.getHTTPClient();
        mGson = serializerProvider.getGson();
        mWsRequest = new Request.Builder().url(BuildConfig.WS).build();
    }

    public void connect(WSClientListener listener) {
        if (mConnected) {
            disconnect();
        }

        if (!mConnected) {
            mListenerWeakReference = new WeakReference<>(listener);
            mWebSocket = mOkHttpClient.newWebSocket(mWsRequest, new LightWebSocketListener(mListenerWeakReference.get(), mGson));
            mConnected = true;
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

    public void send(int id, @Light.LightState String state) {
        if (mConnected) {
            mWebSocket.send(mGson.toJson(new LightWriteAction(id, state)));
        }
    }

    static class LightWebSocketListener extends WebSocketListener {

        private Gson mGson;
        private WeakReference<WSClientListener> mListener;
        private Handler mMainHandler = new Handler(Looper.getMainLooper());

        LightWebSocketListener(WSClientListener listener, Gson gson) {
            mListener = new WeakReference<>(listener);
            mGson = gson;
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

                Runnable myRunnable = () -> mListener.get().onLightsReceived(retList);
                mMainHandler.post(myRunnable);
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
            //TODO: Retry connection?
            t.printStackTrace();
        }
    }

    static class LightReadAction {

        @SerializedName("action")
        private String action = "READ";


        @SerializedName("id")
        private Integer id;

        LightReadAction() {
        }

        public LightReadAction(int id) {
            this.id = id;
        }
    }

    static class LightWriteAction {
        @SerializedName("action")
        private String action = "WRITE";

        @SerializedName("id")
        private int id;

        @SerializedName("state")
        private @Light.LightState String state;

        LightWriteAction(int id, @Light.LightState String state) {
            this.id = id;
            this.state = state;
        }
    }
}
