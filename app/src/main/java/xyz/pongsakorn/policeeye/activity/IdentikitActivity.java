package xyz.pongsakorn.policeeye.activity;

import android.graphics.Matrix;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.adapter.FaceCompositeAdapter;

public class IdentikitActivity extends AppCompatActivity {

    private int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private ImageView ivJaw;
    private ImageView ivHair;
    private ImageView ivEyebrows;
    private ImageView ivEyes;
    private ImageView ivNose;
    private ImageView ivMouth;
    private RecyclerView recycViewFaceComposite;
    private FaceCompositeAdapter faceCompositeAdapter;
    private Button btn1, btn2;
    private Matrix matrix = new Matrix();
    private float mLastTouchX;
    private float mLastTouchY;
    private float scaleJaw = 1f;
    private float scaleHair = 1f;
    private float scaleEyebrows = 1f;
    private float scaleEyes = 1f;
    private float scaleNose = 1f;
    private float scaleMouth = 1f;
    private ScaleGestureDetector SGD;
    private FacialComposite currentComposite = FacialComposite.HAIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identikit);

        ivJaw = (ImageView)findViewById(R.id.ivJaw);
        ivHair = (ImageView)findViewById(R.id.ivHair);
        ivEyebrows = (ImageView)findViewById(R.id.ivEyebrows);
        ivEyes = (ImageView)findViewById(R.id.ivEyes);
        ivNose = (ImageView)findViewById(R.id.ivNose);
        ivMouth = (ImageView)findViewById(R.id.ivMouth);
        recycViewFaceComposite =(RecyclerView)findViewById(R.id.recycViewFaceComposite);
        /*btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);*/

        recycViewFaceComposite.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        faceCompositeAdapter = new FaceCompositeAdapter(this, new FaceCompositeAdapter.AdapterListener() {
            @Override
            public void onSelect(FacialComposite composite) {
                currentComposite = composite;
                // refresh recycViewCompositeStyle
            }
        });
        recycViewFaceComposite.setAdapter(faceCompositeAdapter);
        SGD = new ScaleGestureDetector(this,new ScaleListener());

        /*btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentComposite = FacialComposite.HAIR;
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentComposite = FacialComposite.EYES;
            }
        });*/
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
        layoutParams.leftMargin = (int) (layoutParams.leftMargin + dx);
        layoutParams.topMargin = (int) (layoutParams.topMargin + dy);
        view.setLayoutParams(layoutParams);
    }

    public enum FacialComposite {
        JAW, HAIR, EYES, EYEBROWS, NOSE ,MOUTH
    }
}
