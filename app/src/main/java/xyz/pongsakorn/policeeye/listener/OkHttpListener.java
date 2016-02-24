package xyz.pongsakorn.policeeye.listener;

import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Porpeeranut on 25/2/2559.
 */

public abstract class OkHttpListener<T> {
    public abstract void onStart();
    public abstract void onSuccess(T response);
    public abstract void onInternetDown();
    public abstract void onFailed(int statusCode, String error);
}

