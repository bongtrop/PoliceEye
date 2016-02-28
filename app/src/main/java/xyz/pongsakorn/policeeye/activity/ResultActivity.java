package xyz.pongsakorn.policeeye.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.adapter.ResultPagerAdapter;
import xyz.pongsakorn.policeeye.utils.DatabaseHandler;
import xyz.pongsakorn.policeeye.utils.SketchMatchSDK;

public class ResultActivity extends AppCompatActivity {

    private final String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PoliceEye/";

    String fileName;
    String name;
    String gender;
    String note;
    ViewPager pager;
    ResultPagerAdapter pagerAdapter;

    ImageView ivSketch;
    TextView txtName;
    TextView txtGender;
    TextView btnViewNote;
    TextView txtOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        //sketchBitmap = intent.getParcelableExtra("SketchImage");
        fileName = intent.getStringExtra("fileName");
        name = intent.getStringExtra("name");
        gender = intent.getStringExtra("gender");
        note = intent.getStringExtra("note");
        Type type = new TypeToken<ArrayList<SketchMatchSDK.Person>>() {}.getType();
        final ArrayList<SketchMatchSDK.Person> people = new Gson().fromJson(getIntent().getStringExtra("people"), type);

        pager = (ViewPager) findViewById(R.id.pager);
        ivSketch = (ImageView) findViewById(R.id.ivSketch);
        txtName = (TextView) findViewById(R.id.txtName);
        txtGender = (TextView) findViewById(R.id.txtGender);
        btnViewNote = (TextView) findViewById(R.id.btnViewNote);
        txtOrder = (TextView) findViewById(R.id.txtOrder);

        Glide.with(ResultActivity.this)
                .load(new File(file_path + fileName))
                .into(ivSketch);
        //ivSketch.setImageBitmap(sketchBitmap);
        txtName.setText(name);
        txtGender.setText(gender);

        pagerAdapter = new ResultPagerAdapter(ResultActivity.this, getSupportFragmentManager(), people);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                txtOrder.setText((position + 1) + "/" + pagerAdapter.getCount());
            }
        });
        txtOrder.setText("1/" + pagerAdapter.getCount());

        if (note.length()>50) {
            btnViewNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(ResultActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_note);
                    final TextView txtNote = (TextView) dialog.findViewById(R.id.txtNote);

                    txtNote.setText(note);
                    dialog.show();
                }
            });
        } else {
            btnViewNote.setText(note);
            btnViewNote.setTextColor(getResources().getColor(R.color.colorPrimary));
        }




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
            DatabaseHandler db = new DatabaseHandler(ResultActivity.this);
            db.deleteHistory(fileName);
            Intent resultIntent = new Intent();
            setResult(HistoryActivity.RESULT_DELETE, resultIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
