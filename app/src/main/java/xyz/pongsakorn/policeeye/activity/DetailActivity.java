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

import xyz.pongsakorn.policeeye.R;

public class DetailActivity extends AppCompatActivity {

    Bitmap sketchBitmap;

    EditText editInputName;
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
        setContentView(R.layout.activity_detail);

        sketchBitmap = getIntent().getParcelableExtra("SketchImage");

        editInputName = (EditText) findViewById(R.id.editInputName);
        radGroupGender = (RadioGroup) findViewById(R.id.radGroupGender);
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
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
            Intent intent = new Intent(DetailActivity.this, ScanningActivity.class);
            intent.putExtra("SketchImage", sketchBitmap);
            if (radGroupGender.getCheckedRadioButtonId() == R.id.radMale)
                intent.putExtra("gender", "male");
            else
                intent.putExtra("gender", "female");
            intent.putExtra("note", editNote.getText().toString());
            intent.putExtra("inputName", editInputName.getText().toString());
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
