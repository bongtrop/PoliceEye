package xyz.pongsakorn.policeeye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DrawingView drawingView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawingView = (DrawingView) findViewById(R.id.drawingView);
        fab = (FloatingActionButton) findViewById(R.id.fabTool);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawingView.getTool()==DrawingView.PENCIL) {
                    drawingView.setTool(DrawingView.ERASER);
                    fab.setImageResource(R.mipmap.ic_eraser);
                } else {
                    drawingView.setTool(DrawingView.PENCIL);
                    fab.setImageResource(R.mipmap.ic_pencil);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new) {
            drawingView.clear();
        }
        else if (id == R.id.action_open) {
            Toast.makeText(this,"Cooming Soon",Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.action_save) {
            Bitmap result = drawingView.getBitmap();

            if (result==null)
                Toast.makeText(this, "Draw it first", Toast.LENGTH_SHORT).show();

            String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+
                    "/PoliceEye";
            File dir = new File(file_path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, createPhotoName());
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
                result.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
                Toast.makeText(this, "Save Done", Toast.LENGTH_SHORT).show();
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            } catch (IOException e) {
                Toast.makeText(this, "Save Fail", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }

        return super.onOptionsItemSelected(item);
    }

    public String createPhotoName() {
        return new SimpleDateFormat("yyyyMMdd-hhmmss'.jpg'").format(new Date());
    }
}
