package xyz.pongsakorn.policeeye.activity;

import android.content.Intent;
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
            Intent intent = new Intent(SaveActivity.this, ScanningActivity.class);
            intent.putExtra("SketchImage", sketchBitmap);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
