package xyz.pongsakorn.policeeye.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.reflect.Type;
import java.util.ArrayList;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.adapter.ResultPagerAdapter;
import xyz.pongsakorn.policeeye.utils.SketchMatchSDK;

public class ResultActivity extends AppCompatActivity {

    Bitmap sketchBitmap;
    String inputName;
    String gender;
    String note;
    ViewPager pager;
    CirclePageIndicator pagerIndicator;
    ResultPagerAdapter pagerAdapter;

    ImageView ivSketch;
    TextView txtInputName;
    TextView txtGender;
    TextView txtNote;
    TextView txtOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        sketchBitmap = intent.getParcelableExtra("SketchImage");
        inputName = intent.getStringExtra("inputName");
        gender = intent.getStringExtra("gender");
        note = intent.getStringExtra("note");
        Type type = new TypeToken<ArrayList<SketchMatchSDK.Person>>() {}.getType();
        final ArrayList<SketchMatchSDK.Person> people = new Gson().fromJson(getIntent().getStringExtra("people"), type);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerIndicator = (CirclePageIndicator)findViewById(R.id.pagerIndicator);
        ivSketch = (ImageView) findViewById(R.id.ivSketch);
        txtInputName = (TextView) findViewById(R.id.txtInputName);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtNote = (TextView) findViewById(R.id.txtNote);
        txtOrder = (TextView) findViewById(R.id.txtOrder);

        ivSketch.setImageBitmap(sketchBitmap);

        pagerAdapter = new ResultPagerAdapter(ResultActivity.this, getSupportFragmentManager(), people);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                txtOrder.setText((position+1)+"");
            }
        });
        /*int pagerPadding = 166;
        pager.setPageMargin(20);
        pager.setClipToPadding(false);
        pager.setPadding(pagerPadding, 0, pagerPadding, 0);*/
        pagerIndicator.setViewPager(pager);
        pagerIndicator.setSnap(true);
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
