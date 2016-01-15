package xyz.pongsakorn.policeeye;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pongsakorn on 1/15/2016.
 */
public class DrawingView extends View {

    public static int PENCIL = 0;
    public static int ERASER = 1;

    private int drawColor = Color.BLACK;
    private int bgColor = Color.WHITE;
    private Paint drawPaint;
    private Paint erasePaint;
    private Path drawPath;
    private List<Point> erasePoint;
    private int tool;
    private Bitmap now;

    public DrawingView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attributeSet, int defStyleAttr) {
        setDrawingCacheEnabled(true);
        setupPaint();
        drawPath = new Path();
        erasePoint = new ArrayList<>();

        tool = PENCIL;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.DrawingView,
                0, 0);

        try {
            drawColor = a.getColor(R.styleable.DrawingView_drawColor, Color.BLACK);
            bgColor = a.getColor(R.styleable.DrawingView_backgroundColor, Color.WHITE);
        } finally {
            a.recycle();
        }

        now = getDrawingCache();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (now!=null)
            canvas.drawBitmap(now,0,0,drawPaint);
        else
            canvas.drawColor(bgColor);

        canvas.drawPath(drawPath, drawPaint);

        for (Point p : erasePoint){
            canvas.drawCircle(p.x, p.y, 40, erasePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if (tool==PENCIL) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                default:
                    return false;
            }
        }
        else if (tool==ERASER) {
            erasePoint.add(new Point(Math.round(touchX), Math.round(touchY)));
        }

        invalidate();
        return true;
    }

    private void setupPaint() {
        drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeWidth(5f);
        drawPaint.setColor(drawColor);

        erasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        erasePaint.setStyle(Paint.Style.FILL);
        erasePaint.setColor(bgColor);

    }

    public void setTool(int tool) {
        this.tool = tool;
        Bitmap tmp = getDrawingCache();
        now = tmp.copy(tmp.getConfig(), false);
        setDrawingCacheEnabled(false);
        setDrawingCacheEnabled(true);
        drawPath.reset();
        erasePoint.clear();
    }

    public int getTool() {
        return tool;
    }

    public void clear() {
        now = null;
        drawPath.reset();
        erasePoint.clear();
        invalidate();
    }

    public Bitmap getBitmap() {
        Bitmap tmp = getDrawingCache();
        Bitmap res = tmp.copy(tmp.getConfig(), false);
        setDrawingCacheEnabled(false);
        setDrawingCacheEnabled(true);

        if (res.isRecycled())
            return null;

        return res;
    }
}
