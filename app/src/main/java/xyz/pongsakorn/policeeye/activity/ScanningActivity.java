package xyz.pongsakorn.policeeye.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Response;
import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.listener.OkHttpListener;
import xyz.pongsakorn.policeeye.utils.OkHttpUtils;

public class ScanningActivity extends AppCompatActivity {

    Bitmap sketchBitmap;

    ImageView ivSketch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        sketchBitmap = getIntent().getParcelableExtra("SketchImage");

        ivSketch = (ImageView) findViewById(R.id.ivSketch);
        ivSketch.setImageBitmap(sketchBitmap);

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
                String responseBody = ((Response) response).toString();
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
    }

    public String createPhotoName() {
        return new SimpleDateFormat("yyyyMMdd-hhmmss'.jpg'").format(new Date());
    }
}