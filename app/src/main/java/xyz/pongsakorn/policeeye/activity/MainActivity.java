package xyz.pongsakorn.policeeye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import xyz.pongsakorn.policeeye.R;

public class MainActivity extends AppCompatActivity {

    ImageView imLogo;
    LinearLayout layoutMenu;
    LinearLayout btnSketch;
    LinearLayout btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imLogo = (ImageView) findViewById(R.id.imLogo);
        layoutMenu = (LinearLayout) findViewById(R.id.layoutMenu);
        btnSketch = (LinearLayout) findViewById(R.id.btnSketch);
        btnHistory = (LinearLayout) findViewById(R.id.btnHistory);

        final Animation slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
        slideup.setDuration(2000);

        imLogo.startAnimation(slideup);

        layoutMenu.setAlpha(0.0f);
        layoutMenu.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutMenu.animate().alpha(1.0f).setDuration(1500).start();
            }
        }, 1500);

        btnSketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IdentikitActivity.class);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

    }

}
