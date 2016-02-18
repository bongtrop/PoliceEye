package xyz.pongsakorn.policeeye.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.adapter.FaceCompositeAdapter;
import xyz.pongsakorn.policeeye.adapter.FaceCompositeStyleAdapter;

public class IdentikitActivity extends AppCompatActivity {

    private int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private RelativeLayout layoutSketch;
    private ImageView ivJaw;
    private ImageView ivHair;
    private ImageView ivEyebrows;
    private ImageView ivEyes;
    private ImageView ivNose;
    private ImageView ivMouth;
    private RecyclerView recycViewFaceComposite;
    private RecyclerView recycViewFaceCompStyle;
    private FaceCompositeAdapter faceCompositeAdapter;
    private FaceCompositeStyleAdapter faceCompStyleAdapter;
    private Matrix matrix = new Matrix();
    private float mLastTouchX;
    private float mLastTouchY;
    private float initScale = 2f;
    private float scaleJaw = initScale;
    private float scaleHair = initScale;
    private float scaleEyebrows = initScale;
    private float scaleEyes = initScale;
    private float scaleNose = initScale;
    private float scaleMouth = initScale;
    private ScaleGestureDetector SGD;
    private FacialComposite currentComposite = FacialComposite.HAIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identikit);

        layoutSketch = (RelativeLayout)findViewById(R.id.layoutSketch);
        ivJaw = (ImageView)findViewById(R.id.ivJaw);
        ivHair = (ImageView)findViewById(R.id.ivHair);
        ivEyebrows = (ImageView)findViewById(R.id.ivEyebrows);
        ivEyes = (ImageView)findViewById(R.id.ivEyes);
        ivNose = (ImageView)findViewById(R.id.ivNose);
        ivMouth = (ImageView)findViewById(R.id.ivMouth);
        recycViewFaceCompStyle =(RecyclerView)findViewById(R.id.recycViewFaceCompStyle);
        recycViewFaceComposite =(RecyclerView)findViewById(R.id.recycViewFaceComposite);

        matrix.setScale(initScale, initScale);
        ivJaw.setImageMatrix(matrix);
        ivHair.setImageMatrix(matrix);
        ivEyebrows.setImageMatrix(matrix);
        ivEyes.setImageMatrix(matrix);
        ivNose.setImageMatrix(matrix);
        ivMouth.setImageMatrix(matrix);
        recycViewFaceCompStyle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycViewFaceComposite.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        faceCompStyleAdapter = new FaceCompositeStyleAdapter(this, FacialComposite.HAIR, new FaceCompositeStyleAdapter.AdapterListener() {
            @Override
            public void onSelect(int resId, int selectedStylePos) {
                switch (currentComposite) {
                    case JAW:
                        ivJaw.setImageResource(resId);
                        break;
                    case HAIR:
                        ivHair.setImageResource(resId);
                        break;
                    case EYEBROWS:
                        ivEyebrows.setImageResource(resId);
                        break;
                    case EYES:
                        ivEyes.setImageResource(resId);
                        break;
                    case NOSE:
                        ivNose.setImageResource(resId);
                        break;
                    case MOUTH:
                        ivMouth.setImageResource(resId);
                        break;
                }
                faceCompositeAdapter.changeSelectedStylePos(selectedStylePos);
            }
        });
        faceCompositeAdapter = new FaceCompositeAdapter(this, new FaceCompositeAdapter.AdapterListener() {
            @Override
            public void onSelect(FacialComposite composite, int selectedStylePos) {
                currentComposite = composite;
                faceCompStyleAdapter.initDataset(composite, selectedStylePos);
            }
        });
        recycViewFaceCompStyle.setAdapter(faceCompStyleAdapter);
        recycViewFaceComposite.setAdapter(faceCompositeAdapter);
        SGD = new ScaleGestureDetector(this,new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                /*TranslateAnimation anim = new TranslateAnimation(0, 0,
                        TranslateAnimation.ABSOLUTE, dx, 0, 0,
                        TranslateAnimation.ABSOLUTE, dy);
                //TranslateAnimation anim = new TranslateAnimation(mLastTouchX, dx, mLastTouchY, dy);
                anim.setFillAfter(true);
                anim.setDuration(0);
                ivHair.startAnimation(anim);*/

                switch (currentComposite) {
                    case JAW:
                        moveView(ivJaw, dx, dy);
                        break;
                    case HAIR:
                        moveView(ivHair, dx, dy);
                        break;
                    case EYEBROWS:
                        moveView(ivEyebrows, dx, dy);
                        break;
                    case EYES:
                        moveView(ivEyes, dx, dy);
                        break;
                    case NOSE:
                        moveView(ivNose, dx, dy);
                        break;
                    case MOUTH:
                        moveView(ivMouth, dx, dy);
                        break;
                }

                /*ivHair.animate()
                        .translationX(dx)
                        .translationY(dy)
                        .setDuration(0)
                        .start();*/

                /*ivHair.setTranslationX(dx);
                ivHair.setTranslationY(dy);*/

                /*mPosX += dx;
                mPosY += dy;*/

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        SGD.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = 1f;
            switch (currentComposite) {
                case JAW:
                    scale = scaleJaw;
                    break;
                case HAIR:
                    scale = scaleHair;
                    break;
                case EYEBROWS:
                    scale = scaleEyebrows;
                    break;
                case EYES:
                    scale = scaleEyes;
                    break;
                case NOSE:
                    scale = scaleNose;
                    break;
                case MOUTH:
                    scale = scaleMouth;
                    break;
            }
            scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 4.0f));
            matrix.setScale(scale, scale);
            switch (currentComposite) {
                case JAW:
                    ivJaw.setImageMatrix(matrix);
                    scaleJaw = scale;
                    break;
                case HAIR:
                    ivHair.setImageMatrix(matrix);
                    scaleHair = scale;
                    break;
                case EYEBROWS:
                    ivEyebrows.setImageMatrix(matrix);
                    scaleEyebrows = scale;
                    break;
                case EYES:
                    ivEyes.setImageMatrix(matrix);
                    scaleEyes = scale;
                    break;
                case NOSE:
                    ivNose.setImageMatrix(matrix);
                    scaleNose = scale;
                    break;
                case MOUTH:
                    ivMouth.setImageMatrix(matrix);
                    scaleMouth = scale;
                    break;
            }
            return true;
        }
    }

    private void moveView(View view, float dx, float dy) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        int maxPadding = (int) (360 * getResources().getDisplayMetrics().density);
        layoutParams.leftMargin = Math.max(0, Math.min((int) (layoutParams.leftMargin + dx), maxPadding));
        layoutParams.topMargin = Math.max(0, Math.min((int) (layoutParams.topMargin + dy), maxPadding));
        /*float tmp = getResources().getDisplayMetrics().density;
        Log.e("id kit act", layoutParams.leftMargin / tmp+","+layoutParams.topMargin / tmp);*/
        view.setLayoutParams(layoutParams);
    }

    public enum FacialComposite {
        JAW, HAIR, EYES, EYEBROWS, NOSE ,MOUTH
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_identikit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new) {
            //drawingView.clear();
        } else if (id == R.id.action_save) {
            layoutSketch.setDrawingCacheEnabled(true);
            Bitmap result = Bitmap.createBitmap(layoutSketch.getDrawingCache());
            layoutSketch.setDrawingCacheEnabled(false);

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
