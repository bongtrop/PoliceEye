package xyz.pongsakorn.policeeye.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.utils.DatabaseHandler;
import xyz.pongsakorn.policeeye.utils.SketchMatchSDK;
import xyz.pongsakorn.policeeye.utils.Utils;

public class DetailActivity extends AppCompatActivity {

    Context context;
    Bitmap sketchBitmap;
    Bitmap sketchBitmapJaw;
    Bitmap sketchBitmapHair;
    Bitmap sketchBitmapEyebrows;
    Bitmap sketchBitmapEyes;
    Bitmap sketchBitmapNose;
    Bitmap sketchBitmapMouth;

    EditText editName;
    RadioGroup radGroupGender;
    RadioGroup radGroupAlgo;
    RadioGroup radGroupHeight;
    RadioButton radUnknown;
    RadioButton radAround;
    EditText editAroundStart;
    EditText editAroundEnd;
    EditText editNote;
    ImageView ivSketch;

    SketchMatchSDK sketchMatchSDK;
    SketchMatchSDK.Listener retrievalListener;
    Dialog dialog;

    String fileName;
    String name;
    String gender;
    String algo;
    String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = getApplicationContext();

        sketchMatchSDK = new SketchMatchSDK("http://pongsakorn.xyz:8080");

        sketchBitmap = getIntent().getParcelableExtra("SketchImage");
        fileName = getIntent().getStringExtra("fileName");
        /*sketchBitmapJaw = getSketchCacheFile(getFilesDir() + "/jaw");
        sketchBitmapHair = getSketchCacheFile(getFilesDir() + "/hair");
        sketchBitmapEyebrows = getSketchCacheFile(getFilesDir() + "/eyebrows");
        sketchBitmapEyes = getSketchCacheFile(getFilesDir() + "/eyes");
        sketchBitmapNose = getSketchCacheFile(getFilesDir() + "/nose");
        sketchBitmapMouth = getSketchCacheFile(getFilesDir() + "/mouth");*/
        sketchBitmapJaw = Utils.getSketchJpgFile(Utils.file_path, fileName.replace(".jpg", "") + "-jaw.jpg");
        sketchBitmapHair = Utils.getSketchJpgFile(Utils.file_path, fileName.replace(".jpg", "") + "-hair.jpg");
        sketchBitmapEyebrows = Utils.getSketchJpgFile(Utils.file_path, fileName.replace(".jpg", "") + "-eyebrows.jpg");
        sketchBitmapEyes = Utils.getSketchJpgFile(Utils.file_path, fileName.replace(".jpg", "") + "-eyes.jpg");
        sketchBitmapNose = Utils.getSketchJpgFile(Utils.file_path, fileName.replace(".jpg", "") + "-nose.jpg");
        sketchBitmapMouth = Utils.getSketchJpgFile(Utils.file_path, fileName.replace(".jpg", "") + "-mouth.jpg");

        editName = (EditText) findViewById(R.id.editName);
        radGroupGender = (RadioGroup) findViewById(R.id.radGroupGender);
        radGroupAlgo = (RadioGroup) findViewById(R.id.radGroupAlgo);
        radGroupHeight = (RadioGroup) findViewById(R.id.radGroupHeight);
        radUnknown = (RadioButton) findViewById(R.id.radUnknown);
        radAround = (RadioButton) findViewById(R.id.radAround);
        editAroundStart = (EditText) findViewById(R.id.editAroundStart);
        editAroundEnd = (EditText) findViewById(R.id.editAroundEnd);
        editNote = (EditText) findViewById(R.id.editNote);
        ivSketch = (ImageView) findViewById(R.id.ivSketch);

        ivSketch.setImageBitmap(sketchBitmap);

        retrievalListener = new SketchMatchSDK.Listener() {
            @Override
            public void onSuccess(ArrayList<SketchMatchSDK.Person> people) {
                dialog.dismiss();
                String jsonPeople = new Gson().toJson(people);
                if (note.equals(""))
                    note = "-";
                Utils.saveBitmapToJPG(context, fileName, sketchBitmap);
                saveHistoryToDB(fileName, name, gender, note, jsonPeople, algo);
                Intent intent = new Intent(DetailActivity.this, ResultActivity.class);
                //intent.putExtra("SketchImage", sketchBitmap);
                intent.putExtra("fileName", fileName);
                intent.putExtra("gender", gender);
                intent.putExtra("note", note);
                intent.putExtra("name", name);
                intent.putExtra("people", jsonPeople);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(String error) {
                //Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        };
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
            note = editNote.getText().toString();
            name = editName.getText().toString();
            if (!name.equals("")) {
                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_loading);

                dialog.show();

                gender = (radGroupGender.getCheckedRadioButtonId() == R.id.radMale) ? "Male" : "Female";
                algo = (radGroupAlgo.getCheckedRadioButtonId() == R.id.radSURF) ? "usurf" : "stringgrammar";

                if (algo.equals("usurf")) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    sketchBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    byte[] byteArray = stream.toByteArray();
                    sketchMatchSDK.retrievalUSURF(byteArray, gender.equalsIgnoreCase("male") ? "M" : "F", algo, retrievalListener);
                } else {
                    ByteArrayOutputStream streamJaw = new ByteArrayOutputStream();
                    ByteArrayOutputStream streamHair = new ByteArrayOutputStream();
                    ByteArrayOutputStream streamEyebrows = new ByteArrayOutputStream();
                    ByteArrayOutputStream streamEyes = new ByteArrayOutputStream();
                    ByteArrayOutputStream streamNose = new ByteArrayOutputStream();
                    ByteArrayOutputStream streamMouth = new ByteArrayOutputStream();
                    sketchBitmapJaw.compress(Bitmap.CompressFormat.JPEG, 90, streamJaw);
                    sketchBitmapHair.compress(Bitmap.CompressFormat.JPEG, 90, streamHair);
                    sketchBitmapEyebrows.compress(Bitmap.CompressFormat.JPEG, 90, streamEyebrows);
                    sketchBitmapEyes.compress(Bitmap.CompressFormat.JPEG, 90, streamEyes);
                    sketchBitmapNose.compress(Bitmap.CompressFormat.JPEG, 90, streamNose);
                    sketchBitmapMouth.compress(Bitmap.CompressFormat.JPEG, 90, streamMouth);
                    byte[] byteArrayJaw = streamJaw.toByteArray();
                    byte[] byteArrayHair = streamHair.toByteArray();
                    byte[] byteArrayEyebrows = streamEyebrows.toByteArray();
                    byte[] byteArrayEyes = streamEyes.toByteArray();
                    byte[] byteArrayNose = streamNose.toByteArray();
                    byte[] byteArrayMouth = streamMouth.toByteArray();
                    sketchMatchSDK.retrievalStringGrammar(byteArrayJaw,
                            byteArrayHair,
                            byteArrayEyebrows,
                            byteArrayEyes,
                            byteArrayNose,
                            byteArrayMouth,
                            gender.equalsIgnoreCase("male") ? "M" : "F",
                            algo,
                            retrievalListener);
                }

            } else
                Toast.makeText(context, "Please enter name.", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveHistoryToDB(String fileName, String name, String gender, String note, String jsonPeople, String algo) {
        DatabaseHandler db = new DatabaseHandler(DetailActivity.this);
        db.addHistory(fileName, name, gender, note, jsonPeople, algo);
    }
}
