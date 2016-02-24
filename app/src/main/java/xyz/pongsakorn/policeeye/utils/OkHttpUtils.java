package xyz.pongsakorn.policeeye.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.pongsakorn.policeeye.listener.OkHttpListener;

/**
 * Created by Porpeeranut on 25/2/2559.
 */
public class OkHttpUtils {
    public static void uploadImage(String url, byte[] byteImage, String fileName, final OkHttpListener listener){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload", fileName, RequestBody.create(MediaType.parse("image/png"), byteImage))
                .build();
        Request request = new Request.Builder()
                .url(url).post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailed(0, e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onSuccess(response);
            }
        });
    }
}
