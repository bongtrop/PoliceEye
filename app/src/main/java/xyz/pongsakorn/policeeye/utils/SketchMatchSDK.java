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

    public void retrievalUSURF(byte[] image, String sex, String algo, final Listener listener) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("full", "sketch", RequestBody.create(MediaType.parse("image/jpg"), image))
                .addFormDataPart("sex", sex)
                .addFormDataPart("algo", algo)
                .build();
        retrieval(requestBody, listener);
    }

    public void retrievalStringGrammar(byte[] jaw, byte[] hair, byte[] eyebrows, byte[] eyes, byte[] nose, byte[] mouth, String sex, String algo, final Listener listener) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("jaw", "jaw", RequestBody.create(MediaType.parse("image/jpg"), jaw))
                        //.addFormDataPart("hair", "hair", RequestBody.create(MediaType.parse("image/jpg"), hair))
                .addFormDataPart("eyebrows", "eyebrows", RequestBody.create(MediaType.parse("image/jpg"), eyebrows))
                .addFormDataPart("eyes", "eyes", RequestBody.create(MediaType.parse("image/jpg"), eyes))
                .addFormDataPart("nose", "nose", RequestBody.create(MediaType.parse("image/jpg"), nose))
                .addFormDataPart("mouth", "mouth", RequestBody.create(MediaType.parse("image/jpg"), mouth))
                .addFormDataPart("sex", sex)
                .addFormDataPart("algo", algo)
                .build();
        retrieval(requestBody, listener);
    }

    public void retrieval(RequestBody requestBody, final Listener listener) {
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
                String res = response.body().string();
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
                String res = response.body().string();
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
        public String id;
        public String sex;
        public String name;
        public double point;
    }

    public interface Listener {
        void onSuccess(ArrayList<Person> people);
        void onFail(String error);
    }
}