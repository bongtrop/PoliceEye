package xyz.pongsakorn.policeeye.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.adapter.FaceCompositeAdapter;
import xyz.pongsakorn.policeeye.adapter.FaceCompositeStyleAdapter;
import xyz.pongsakorn.policeeye.utils.Utils;

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
    private float scaleJawX;
    private float scaleJawY;
    private float scaleHairX;
    private float scaleHairY;
    private float scaleEyebrowsX;
    private float scaleEyebrowsY;
    private float scaleEyesX;
    private float scaleEyesY;
    private float scaleNoseX;
    private float scaleNoseY;
    private float scaleMouthX;
    private float scaleMouthY;
    private ScaleGestureDetector SGD;
    private boolean isScaling;
    private FacialComposite currentComposite;
    private int faceCompItemOnCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identikit);

        layoutSketch = (RelativeLayout) findViewById(R.id.layoutSketch);
        ivJaw = (ImageView) findViewById(R.id.ivJaw);
        ivHair = (ImageView) findViewById(R.id.ivHair);
        ivEyebrows = (ImageView) findViewById(R.id.ivEyebrows);
        ivEyes = (ImageView) findViewById(R.id.ivEyes);
        ivNose = (ImageView) findViewById(R.id.ivNose);
        ivMouth = (ImageView) findViewById(R.id.ivMouth);
        recycViewFaceCompStyle = (RecyclerView) findViewById(R.id.recycViewFaceCompStyle);
        recycViewFaceComposite = (RecyclerView) findViewById(R.id.recycViewFaceComposite);

        initValue();
    }

    private void initValue() {
        isScaling = false;
        currentComposite = FacialComposite.HAIR;
        faceCompItemOnCanvas = 0;
        scaleJawX = initScale;
        scaleJawY = initScale;
        scaleHairX = initScale;
        scaleHairY = initScale;
        scaleEyebrowsX = initScale;
        scaleEyebrowsY = initScale;
        scaleEyesX = initScale;
        scaleEyesY = initScale;
        scaleNoseX = initScale;
        scaleNoseY = initScale;
        scaleMouthX = initScale;
        scaleMouthY = initScale;

        matrix.setScale(initScale, initScale);
        ivJaw.setImageMatrix(matrix);
        ivHair.setImageMatrix(matrix);
        ivEyebrows.setImageMatrix(matrix);
        ivEyes.setImageMatrix(matrix);
        ivNose.setImageMatrix(matrix);
        ivMouth.setImageMatrix(matrix);

        ivJaw.setVisibility(View.GONE);
        ivHair.setVisibility(View.GONE);
        ivEyebrows.setVisibility(View.GONE);
        ivEyes.setVisibility(View.GONE);
        ivNose.setVisibility(View.GONE);
        ivMouth.setVisibility(View.GONE);

        setViewMargin(ivJaw, (int) getResources().getDimension(R.dimen.margin_left_jaw), (int) getResources().getDimension(R.dimen.margin_top_jaw));
        setViewMargin(ivHair, (int) getResources().getDimension(R.dimen.margin_left_hair), (int) getResources().getDimension(R.dimen.margin_top_hair));
        setViewMargin(ivEyebrows, (int) getResources().getDimension(R.dimen.margin_left_eyebrows), (int) getResources().getDimension(R.dimen.margin_top_eyebrows));
        setViewMargin(ivEyes, (int) getResources().getDimension(R.dimen.margin_left_eyes), (int) getResources().getDimension(R.dimen.margin_top_eyes));
        setViewMargin(ivNose, (int) getResources().getDimension(R.dimen.margin_left_nose), (int) getResources().getDimension(R.dimen.margin_top_nose));
        setViewMargin(ivMouth, (int) getResources().getDimension(R.dimen.margin_left_mouth), (int) getResources().getDimension(R.dimen.margin_top_mouth));

        recycViewFaceCompStyle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycViewFaceComposite.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        faceCompStyleAdapter = new FaceCompositeStyleAdapter(this, FacialComposite.HAIR, new FaceCompositeStyleAdapter.AdapterListener() {
            @Override
            public void onSelect(int resId, int selectedStylePos, int oldStylePos) {
                if (oldStylePos == 0 && selectedStylePos != 0) {
                    faceCompItemOnCanvas++;
                } else if (selectedStylePos == 0) {
                    faceCompItemOnCanvas--;
                }

                switch (currentComposite) {
                    case JAW:
                        setImage(ivJaw, resId);
                        break;
                    case HAIR:
                        setImage(ivHair, resId);
                        break;
                    case EYEBROWS:
                        setImage(ivEyebrows, resId);
                        break;
                    case EYES:
                        setImage(ivEyes, resId);
                        break;
                    case NOSE:
                        setImage(ivNose, resId);
                        break;
                    case MOUTH:
                        setImage(ivMouth, resId);
                        break;
                }
                faceCompositeAdapter.changeSelectedStylePos(selectedStylePos);
            }

            private void setImage(ImageView view, int resId) {
                if (resId == R.mipmap.hide)
                    view.setVisibility(View.GONE);
                else
                    view.setVisibility(View.VISIBLE);
                view.setImageResource(resId);
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
        SGD = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /*isScaling = false;
        SGD.onTouchEvent(ev);*/
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

                if (!isScaling) {
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
            float scaleX = 1f;
            float scaleY = 1f;
            isScaling = true;
            switch (currentComposite) {
                case JAW:
                    scaleX = scaleJawX;
                    scaleY = scaleJawY;
                    break;
                case HAIR:
                    scaleX = scaleHairX;
                    scaleY = scaleHairY;
                    break;
                case EYEBROWS:
                    scaleX = scaleEyebrowsX;
                    scaleY = scaleEyebrowsY;
                    break;
                case EYES:
                    scaleX = scaleEyesX;
                    scaleY = scaleEyesY;
                    break;
                case NOSE:
                    scaleX = scaleNoseX;
                    scaleY = scaleNoseY;
                    break;
                case MOUTH:
                    scaleX = scaleMouthX;
                    scaleY = scaleMouthY;
                    break;
            }
            Log.e("iden", String.valueOf(detector.getCurrentSpanX()) + " " + String.valueOf(detector.getCurrentSpanY()) + " " + String.valueOf(detector.getScaleFactor()));
            /*scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 4.0f));*/
            scaleX *= detector.getCurrentSpanX() / detector.getPreviousSpanX();
            scaleX = Math.max(0.1f, Math.min(scaleX, 4.0f));
            scaleY *= detector.getCurrentSpanY() / detector.getPreviousSpanY();
            scaleY = Math.max(0.1f, Math.min(scaleY, 4.0f));
            matrix.setScale(scaleX, scaleY);
            switch (currentComposite) {
                case JAW:
                    if (ivJaw.getVisibility() == View.GONE) return true;
                    ivJaw.setImageMatrix(matrix);
                    scaleJawX = scaleX;
                    scaleJawY = scaleY;
                    break;
                case HAIR:
                    if (ivHair.getVisibility() == View.GONE) return true;
                    ivHair.setImageMatrix(matrix);
                    scaleHairX = scaleX;
                    scaleHairY = scaleY;
                    break;
                case EYEBROWS:
                    if (ivEyebrows.getVisibility() == View.GONE) return true;
                    ivEyebrows.setImageMatrix(matrix);
                    scaleEyebrowsX = scaleX;
                    scaleEyebrowsY = scaleY;
                    break;
                case EYES:
                    if (ivEyes.getVisibility() == View.GONE) return true;
                    ivEyes.setImageMatrix(matrix);
                    scaleEyesX = scaleX;
                    scaleEyesY = scaleY;
                    break;
                case NOSE:
                    if (ivNose.getVisibility() == View.GONE) return true;
                    ivNose.setImageMatrix(matrix);
                    scaleNoseX = scaleX;
                    scaleNoseY = scaleY;
                    break;
                case MOUTH:
                    if (ivMouth.getVisibility() == View.GONE) return true;
                    ivMouth.setImageMatrix(matrix);
                    scaleMouthX = scaleX;
                    scaleMouthY = scaleY;
                    break;
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            isScaling = false;
        }
    }

    private void moveView(View view, float dx, float dy) {
        if (view.getVisibility() == View.GONE)
            return;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        int maxPadding = (int) (200 * getResources().getDisplayMetrics().density);
        layoutParams.leftMargin = Math.max(0, Math.min((int) (layoutParams.leftMargin + dx), maxPadding));
        layoutParams.topMargin = Math.max(0, Math.min((int) (layoutParams.topMargin + dy), maxPadding));
        /*float tmp = getResources().getDisplayMetrics().density;
        Log.e("id kit act", layoutParams.leftMargin / tmp + "," + layoutParams.topMargin / tmp);*/
        view.setLayoutParams(layoutParams);
    }

    private void setViewMargin(View view, int leftMargin, int topMargin) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = leftMargin;
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }

    public static int[] findEdgeForTrimBitmap(Bitmap bmp, int paddingPx) {
        int imgHeight = bmp.getHeight();
        int imgWidth = bmp.getWidth();

        //TRIM WIDTH - LEFT
        int startWidth = 0;
        for (int x = 0; x < imgWidth; x++) {
            if (startWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.WHITE) {
                        startWidth = x;
                        break;
                    }
                }
            } else break;
        }

        //TRIM WIDTH - RIGHT
        int endWidth = 0;
        for (int x = imgWidth - 1; x >= 0; x--) {
            if (endWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.WHITE) {
                        endWidth = x;
                        break;
                    }
                }
            } else break;
        }

        //TRIM HEIGHT - TOP
        int startHeight = 0;
        for (int y = 0; y < imgHeight; y++) {
            if (startHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.WHITE) {
                        startHeight = y;
                        break;
                    }
                }
            } else break;
        }

        //TRIM HEIGHT - BOTTOM
        int endHeight = 0;
        for (int y = imgHeight - 1; y >= 0; y--) {
            if (endHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.WHITE) {
                        endHeight = y;
                        break;
                    }
                }
            } else break;
        }
        startWidth -= paddingPx;
        endWidth += paddingPx;
        startHeight -= paddingPx;
        endHeight += paddingPx;
        if (startWidth < 0)
            startWidth = 0;
        if (startHeight < 0)
            startHeight = 0;
        if (endWidth > imgWidth)
            endWidth = imgWidth;
        if (endHeight > imgHeight)
            endHeight = imgHeight;
        return new int[]{startWidth, endWidth, startHeight, endHeight};
    }

    public static int[] findInsideEdgeFromEdge(Bitmap bmp, int[] edge, int paddingPx) {
        int imgHeight = bmp.getHeight();
        int imgWidth = bmp.getWidth();

        //TRIM WIDTH - LEFT
        int startWidth = 0;
        for (int x = edge[0]; x < imgWidth; x++) {
            if (startWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.WHITE) {
                        startWidth = x;
                        break;
                    }
                }
            } else break;
        }

        //TRIM WIDTH - RIGHT
        int endWidth = 0;
        for (int x = imgWidth - 1; x >= 0; x--) {
            if (endWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.WHITE) {
                        endWidth = x;
                        break;
                    }
                }
            } else break;
        }

        //TRIM HEIGHT - TOP
        int startHeight = 0;
        for (int y = 0; y < imgHeight; y++) {
            if (startHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.WHITE) {
                        startHeight = y;
                        break;
                    }
                }
            } else break;
        }

        //TRIM HEIGHT - BOTTOM
        int endHeight = 0;
        for (int y = imgHeight - 1; y >= 0; y--) {
            if (endHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.WHITE) {
                        endHeight = y;
                        break;
                    }
                }
            } else break;
        }
        startWidth -= paddingPx;
        endWidth += paddingPx;
        startHeight -= paddingPx;
        endHeight += paddingPx;
        if (startWidth < 0)
            startWidth = 0;
        if (startHeight < 0)
            startHeight = 0;
        if (endWidth > imgWidth)
            endWidth = imgWidth;
        if (endHeight > imgHeight)
            endHeight = imgHeight;
        return new int[]{startWidth, endWidth, startHeight, endHeight};
    }

    public static Bitmap trimBitmap(Bitmap bmp, int[] edge) {
        /*
        edge[0] = startWidth
        edge[1] = endWidth
        edge[2] = startHeight
        edge[3] = endHeight
        */
        return Bitmap.createBitmap(
                bmp,
                edge[0],
                edge[2],
                edge[1] - edge[0],
                edge[3] - edge[2]
        );
    }

    public enum FacialComposite {
        JAW, HAIR, EYES, EYEBROWS, NOSE, MOUTH
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
            initValue();
        } else if (id == R.id.action_save) {
            if (faceCompItemOnCanvas == faceCompositeAdapter.getItemCount()) {
                layoutSketch.setDrawingCacheEnabled(true);
                //Bitmap result = Bitmap.createBitmap(layoutSketch.getDrawingCache());
                Bitmap result = Bitmap.createScaledBitmap(layoutSketch.getDrawingCache(), 200, 220, false);

                ivJaw.setVisibility(View.GONE);
                ivHair.setVisibility(View.GONE);
                ivEyebrows.setVisibility(View.GONE);
                ivEyes.setVisibility(View.GONE);
                ivNose.setVisibility(View.GONE);
                ivMouth.setVisibility(View.GONE);

                ivJaw.setVisibility(View.VISIBLE);
                Bitmap resultJaw = Bitmap.createScaledBitmap(layoutSketch.getDrawingCache(), 200, 220, false);
                ivJaw.setVisibility(View.GONE);

                ivHair.setVisibility(View.VISIBLE);
                Bitmap resultHair = Bitmap.createScaledBitmap(layoutSketch.getDrawingCache(), 200, 220, false);
                ivHair.setVisibility(View.GONE);

                ivEyebrows.setVisibility(View.VISIBLE);
                Bitmap resultEyebrows = Bitmap.createScaledBitmap(layoutSketch.getDrawingCache(), 200, 220, false);
                ivEyebrows.setVisibility(View.GONE);

                ivEyes.setVisibility(View.VISIBLE);
                Bitmap resultEyes = Bitmap.createScaledBitmap(layoutSketch.getDrawingCache(), 200, 220, false);
                ivEyes.setVisibility(View.GONE);

                ivNose.setVisibility(View.VISIBLE);
                Bitmap resultNose = Bitmap.createScaledBitmap(layoutSketch.getDrawingCache(), 200, 220, false);
                ivNose.setVisibility(View.GONE);

                ivMouth.setVisibility(View.VISIBLE);
                Bitmap resultMouth = Bitmap.createScaledBitmap(layoutSketch.getDrawingCache(), 200, 220, false);
                ivMouth.setVisibility(View.GONE);

                int[] edge = findEdgeForTrimBitmap(result, 15);
                result = trimBitmap(result, edge);
                resultJaw = trimBitmap(resultJaw, edge);
                resultJaw = trimBitmap(resultJaw, findEdgeForTrimBitmap(resultJaw, 0));
                resultHair = trimBitmap(resultHair, edge);
                resultHair = trimBitmap(resultHair, findEdgeForTrimBitmap(resultHair, 0));
                resultEyebrows = trimBitmap(resultEyebrows, edge);
                resultEyebrows = trimBitmap(resultEyebrows, findEdgeForTrimBitmap(resultEyebrows, 0));
                resultEyes = trimBitmap(resultEyes, edge);
                resultEyes = trimBitmap(resultEyes, findEdgeForTrimBitmap(resultEyes, 0));
                resultNose = trimBitmap(resultNose, edge);
                resultNose = trimBitmap(resultNose, findEdgeForTrimBitmap(resultNose, 0));
                resultMouth = trimBitmap(resultMouth, edge);
                resultMouth = trimBitmap(resultMouth, findEdgeForTrimBitmap(resultMouth, 0));
                layoutSketch.setDrawingCacheEnabled(false);
                ivJaw.setVisibility(View.VISIBLE);
                ivHair.setVisibility(View.VISIBLE);
                ivEyebrows.setVisibility(View.VISIBLE);
                ivEyes.setVisibility(View.VISIBLE);
                ivNose.setVisibility(View.VISIBLE);
                ivMouth.setVisibility(View.VISIBLE);

                /*Utils.saveSketchCacheFile(IdentikitActivity.this, "jaw", resultJaw);
                Utils.saveSketchCacheFile(IdentikitActivity.this, "hair", resultHair);
                Utils.saveSketchCacheFile(IdentikitActivity.this, "eyebrows", resultEyebrows);
                Utils.saveSketchCacheFile(IdentikitActivity.this, "eyes", resultEyes);
                Utils.saveSketchCacheFile(IdentikitActivity.this, "nose", resultNose);
                Utils.saveSketchCacheFile(IdentikitActivity.this, "mouth", resultMouth);*/
                String fileName = Utils.createPhotoName();
                Utils.saveBitmapToJPG(IdentikitActivity.this, fileName.replace(".jpg", "") + "-jaw.jpg", resultJaw);
                Utils.saveBitmapToJPG(IdentikitActivity.this, fileName.replace(".jpg", "") + "-hair.jpg", resultHair);
                Utils.saveBitmapToJPG(IdentikitActivity.this, fileName.replace(".jpg", "") + "-eyebrows.jpg", resultEyebrows);
                Utils.saveBitmapToJPG(IdentikitActivity.this, fileName.replace(".jpg", "") + "-eyes.jpg", resultEyes);
                Utils.saveBitmapToJPG(IdentikitActivity.this, fileName.replace(".jpg", "") + "-nose.jpg", resultNose);
                Utils.saveBitmapToJPG(IdentikitActivity.this, fileName.replace(".jpg", "") + "-mouth.jpg", resultMouth);

                Intent intent = new Intent(IdentikitActivity.this, DetailActivity.class);
                intent.putExtra("SketchImage", result);
                intent.putExtra("fileName", fileName);
                /*intent.putExtra("SketchImageJaw", resultJaw);
                intent.putExtra("SketchImageHair", resultHair);
                intent.putExtra("SketchImageEyebrows", resultEyebrows);
                intent.putExtra("SketchImageEyes", resultEyes);
                intent.putExtra("SketchImageNose", resultNose);
                intent.putExtra("SketchImageMouth", resultMouth);*/
                startActivity(intent);
            } else
                Toast.makeText(this, "Some face composite are empty.", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
