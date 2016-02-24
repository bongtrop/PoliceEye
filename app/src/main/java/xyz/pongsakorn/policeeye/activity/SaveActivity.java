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
        }

        return super.onOptionsItemSelected(item);
    }

    public String createPhotoName() {
        return new SimpleDateFormat("yyyyMMdd-hhmmss'.jpg'").format(new Date());
    }
}
