package xyz.pongsakorn.policeeye.handler;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Porpeeranut on 25/2/2559.
 */
public class OkHttpHandler extends AsyncTask<String, Void, String> {

    OkHttpClient client = new OkHttpClient();
    byte[] byteImage;

    public OkHttpHandler(byte[] byteImage) {
        this.byteImage = byteImage;
    }

    @Override
    protected String doInBackground(String... params) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload", "file.png", RequestBody.create(MediaType.parse("image/png"), byteImage))
                .build();
        /*RequestBody requestBody = new FormEncodingBuilder()
                .add("name", userName)
                .add("pass", passWord)
                .build();*/
        Request request = new Request.Builder()
                .url(params[0]).post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response.toString());
            return response.body().string();

        } catch (Exception e) {
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}