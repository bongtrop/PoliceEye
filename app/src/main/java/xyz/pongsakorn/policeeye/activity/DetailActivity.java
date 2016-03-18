package xyz.pongsakorn.policeeye.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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

public class DetailActivity extends AppCompatActivity {

    Bitmap sketchBitmap;

    EditText editName;
    RadioGroup radGroupGender;
    RadioGroup radGroupHeight;
    RadioButton radUnknown;
    RadioButton radAround;
    EditText editAroundStart;
    EditText editAroundEnd;
    EditText editNote;
    ImageView ivSketch;

    SketchMatchSDK sketchMatchSDK;

    String name;
    String gender;
    String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sketchMatchSDK = new SketchMatchSDK("http://pongsakorn.xyz:8080");

        sketchBitmap = getIntent().getParcelableExtra("SketchImage");

        editName = (EditText) findViewById(R.id.editName);
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
            note = editNote.getText().toString();
            name = editName.getText().toString();
            if (!name.equals("")) {
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_loading);

                dialog.show();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                sketchBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

                byte[] byteArray = stream.toByteArray();

                if (radGroupGender.getCheckedRadioButtonId() == R.id.radMale)
                    gender = "Male";
                else
                    gender = "Female";

                sketchMatchSDK.retrieval(byteArray, gender.equalsIgnoreCase("male") ? "M" : "F", new SketchMatchSDK.Listener() {
                    @Override
                    public void onSuccess(ArrayList<SketchMatchSDK.Person> people) {
                        dialog.dismiss();

                        String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                                "/PoliceEye";
                        File dir = new File(file_path);
                        if (!dir.exists())
                            dir.mkdirs();

                        String fileName = createPhotoName();
                        File file = new File(dir, fileName);
                        FileOutputStream fOut = null;
                        try {
                            fOut = new FileOutputStream(file);
                            sketchBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
                            fOut.flush();
                            fOut.close();
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

                            String jsonPeople = new Gson().toJson(people);
                            if (note.equals(""))
                                note = "-";
                            saveHistoryToDB(fileName, name, gender, note, jsonPeople);

                            Intent intent = new Intent(DetailActivity.this, ResultActivity.class);
                            //intent.putExtra("SketchImage", sketchBitmap);
                            intent.putExtra("fileName", fileName);
                            intent.putExtra("gender", gender);
                            intent.putExtra("note", note);
                            intent.putExtra("name", name);
                            intent.putExtra("people", jsonPeople);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }

                    @Override
                    public void onFail(String error) {
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            } else
                Toast.makeText(getApplicationContext(), "Please enter name.", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveHistoryToDB(String fileName, String name, String gender, String note, String jsonPeople) {
        DatabaseHandler db = new DatabaseHandler(DetailActivity.this);
        db.addHistory(fileName, name, gender, note, jsonPeople);
    }

    public String createPhotoName() {
        return new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss'.jpg'").format(new Date());
    }
}
