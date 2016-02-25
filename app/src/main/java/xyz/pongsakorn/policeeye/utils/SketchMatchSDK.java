package xyz.pongsakorn.policeeye.utils;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Porpeeranut on 25/2/2559.
 */
public class SketchMatchSDK {

    String url;
    OkHttpClient client;

    public SketchMatchSDK(String url) {
        this.url = url;
        client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();
    }

    public void retrieval(byte[] image, String sex, final Listener listener) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "sketch", RequestBody.create(MediaType.parse("image/jpg"), image))
                .addFormDataPart("sex", sex)
                .build();

        Request request = new Request.Builder()
                .url(url+"/identiface")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFail(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.toString();
                try {
                    JSONObject jsonres = new JSONObject(res);
                    if (jsonres.getString("status").equals("success")) {
                        ArrayList<Person> people = new Gson().fromJson(jsonres.getJSONArray("data").toString(), new TypeToken<ArrayList<Person>>(){}.getType());
                        listener.onSuccess(people);
                    } else {
                        listener.onFail(jsonres.getString("detail"));
                    }
                } catch (JSONException e) {
                    listener.onFail(e.toString());
                }
            }
        });
    }

    public void getDataset(int skip, int limit, final Listener listener) {
        Request request = new Request.Builder()
                .url(url+"/identiface?skip="+skip+"&limit="+limit)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFail(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.toString();
                try {
                    JSONObject jsonres = new JSONObject(res);
                    if (jsonres.getString("status").equals("success")) {
                        ArrayList<Person> people = new Gson().fromJson(jsonres.getJSONArray("data").toString(), new TypeToken<ArrayList<Person>>(){}.getType());
                        listener.onSuccess(people);
                    } else {
                        listener.onFail(jsonres.getString("detail"));
                    }
                } catch (JSONException e) {
                    listener.onFail(e.toString());
                }
            }
        });
    }

    public class Person {
        String id;
        String sex;
        String name;
        double point;
    }

    public interface Listener {
        void onSuccess(ArrayList<Person> people);
        void onFail(String error);
    }
}