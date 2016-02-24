package xyz.pongsakorn.policeeye.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import xyz.pongsakorn.policeeye.R;

public class ResultActivity extends AppCompatActivity {

    Bitmap sketchBitmap;

    ImageView ivSketch;
    TextView txtInputName;
    TextView txtGender;
    TextView txtNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        sketchBitmap = getIntent().getParcelableExtra("SketchImage");

        ivSketch = (ImageView) findViewById(R.id.ivSketch);
        txtInputName = (TextView) findViewById(R.id.txtInputName);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtNote = (TextView) findViewById(R.id.txtNote);

        ivSketch.setImageBitmap(sketchBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {

        }

        return super.onOptionsItemSelected(item);
    }
}
