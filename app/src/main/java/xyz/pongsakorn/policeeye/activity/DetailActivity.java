package xyz.pongsakorn.policeeye.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.utils.SketchMatchSDK;

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

    SketchMatchSDK sketchMatchSDK;

    String inputName;
    String gender;
    String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sketchMatchSDK = new SketchMatchSDK("http://pongsakorn.xyz:8080");

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
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_loading);

            dialog.show();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            sketchBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

            byte[] byteArray = stream.toByteArray();



            if (radGroupGender.getCheckedRadioButtonId() == R.id.radMale)
                gender = "male";
            else
                gender = "female";

            note = editNote.getText().toString();
            inputName = editInputName.getText().toString();

            sketchMatchSDK.retrieval(byteArray, gender.equals("male") ? "M" : "F", new SketchMatchSDK.Listener() {
                @Override
                public void onSuccess(ArrayList<SketchMatchSDK.Person> people) {
                    dialog.dismiss();
                    Intent intent = new Intent(DetailActivity.this, ResultActivity.class);
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
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
