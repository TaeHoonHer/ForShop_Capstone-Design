package org.pytorch.demo.objectdetection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FocusCircleView extends View {
    private PointF focusPoint;
    private Paint paint;

    public FocusCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
    }

    public void setFocusPoint(PointF point) {
        focusPoint = point;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = event.getY();
            setFocusPoint(new PointF(x, y));
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (focusPoint != null) {
            canvas.drawCircle(focusPoint.x, focusPoint.y, 100f, paint);
        }
    }
}
