package xyz.pongsakorn.policeeye.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.adapter.FaceCompositeAdapter;
import xyz.pongsakorn.policeeye.adapter.FaceCompositeStyleAdapter;
import xyz.pongsakorn.policeeye.handler.OkHttpHandler;
import xyz.pongsakorn.policeeye.listener.OkHttpListener;
import xyz.pongsakorn.policeeye.utils.OkHttpUtils;

public class SaveActivity extends AppCompatActivity {

    Bitmap sketchBitmap;

    EditText editName;
    RadioGroup radGroupGender;
    RadioButton radMale;
    RadioButton radFemale;
    RadioGroup radGroupHeight;
    RadioButton radUnknown;
    RadioButton radAround;
    EditText editAroundStart;
    EditText editAroundEnd;
    EditText editNote;
    ImageView ivSketch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        sketchBitmap = getIntent().getParcelableExtra("SketchImage");

        editName = (EditText) findViewById(R.id.editName);
        radGroupGender = (RadioGroup) findViewById(R.id.radGroupGender);
        radMale = (RadioButton) findViewById(R.id.radMale);
        radFemale = (RadioButton) findViewById(R.id.radFemale);
        radGroupHeight = (RadioGroup) findViewById(R.id.radGroupHeight);
        radUnknown = (RadioButton) findViewById(R.id.radUnknown);
        radAround = (RadioButton) findViewById(R.id.radAround);
        editAroundStart = (EditText) findViewById(R.id.editAroundStart);
        editAroundEnd = (EditText) findViewById(R.id.editAroundEnd);
        editNote = (EditText) findViewById(R.id.editNote);
        ivSketch = (ImageView) findViewById(R.id.ivSketch);

        ivSketch.setImageBitmap(sketchBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
            /*String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                    "/PoliceEye";
            File dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, createPhotoName());
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
                sketchBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
                Toast.makeText(this, "Save Done", Toast.LENGTH_SHORT).show();
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            } catch (IOException e) {
                Toast.makeText(this, "Save Fail", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }*/

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            sketchBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            OkHttpUtils.uploadImage("http://192.168.0.4/receivefile.php", byteArray, createPhotoName(), new OkHttpListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(Object response) {
                    String responseBody = ((Response)response).body().toString();
                    Log.e("save", responseBody);
                }

                @Override
                public void onInternetDown() {

                }

                @Override
                public void onFailed(int statusCode, String error) {
                    Log.e("save", "fail");
                }
            });


            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            sketchBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            //Base64.encodeToString(byteArray, Base64.DEFAULT)
            OkHttpHandler handler = new OkHttpHandler(byteArray);
            String result = null;
            try {
                handler.execute("http://192.168.0.4/receivefile.php").get();
                Log.e("save", result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }*/

            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            sketchBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            RequestBody requestBody = RequestBody.create(MediaType.parse("image*//*"), byteArray);
            //RequestBody requestBody = RequestBody.create(MediaType.parse("image*//*"), sketchBitmap);
            //FileUploadService service = new FileUploadService();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.4")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FileUploadService service = retrofit.create(FileUploadService.class);

            Call<JsonObject> call = service.uploadImage(requestBody);


            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("save", "res");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("save", "fail");
                }
            });*/
        }

        return super.onOptionsItemSelected(item);
    }

    public String createPhotoName() {
        return new SimpleDateFormat("yyyyMMdd-hhmmss'.jpg'").format(new Date());
    }

    /*public interface FileUploadService {
        @Multipart
        @POST("receivefile.php")
        Call<JsonObject> uploadImage(@Part("upload\"; filename=\"1\" ") RequestBody file);
    }*/
}
