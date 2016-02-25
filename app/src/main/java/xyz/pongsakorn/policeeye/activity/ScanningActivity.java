package xyz.pongsakorn.policeeye.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.utils.SketchMatchSDK;

public class ScanningActivity extends AppCompatActivity {

    Bitmap sketchBitmap;
    String inputName;
    String gender;
    String note;

    ImageView ivSketch;
    ImageView ivLaser;

    SketchMatchSDK sketchMatchSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        Intent intent = getIntent();
        sketchBitmap = intent.getParcelableExtra("SketchImage");
        inputName = intent.getStringExtra("inputName");
        gender = intent.getStringExtra("gender");
        note = intent.getStringExtra("note");
        sketchMatchSDK = new SketchMatchSDK("http://pongsakorn.xyz:8080");

        ivSketch = (ImageView) findViewById(R.id.ivSketch);
        ivLaser = (ImageView) findViewById(R.id.ivLaser);
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
        sketchBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

        byte[] byteArray = stream.toByteArray();

        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.8f);
        mAnimation.setDuration(3000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        ivLaser.setAnimation(mAnimation);

        sketchMatchSDK.retrieval(byteArray, gender.equalsIgnoreCase("male") ? "M" : "F", new SketchMatchSDK.Listener() {
            @Override
            public void onSuccess(ArrayList<SketchMatchSDK.Person> people) {
                Intent intent = new Intent(ScanningActivity.this, ResultActivity.class);
                intent.putExtra("SketchImage", sketchBitmap);
                intent.putExtra("gender", gender);
                intent.putExtra("note", note);
                intent.putExtra("inputName", inputName);
                intent.putExtra("people", new Gson().toJson(people));
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(String error) {
                Log.e("scan", error);
            }
        });
    }

    public String createPhotoName() {
        return new SimpleDateFormat("yyyyMMdd-hhmmss'.jpg'").format(new Date());
    }
}
