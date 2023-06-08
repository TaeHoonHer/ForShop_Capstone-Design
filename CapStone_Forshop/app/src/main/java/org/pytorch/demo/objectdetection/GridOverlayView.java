package org.pytorch.demo.objectdetection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GridOverlayView extends View {
    private Paint mGridPaint;

    public GridOverlayView(Context context) {
        super(context);
        init();
    }

    public GridOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mGridPaint = new Paint();
        mGridPaint.setColor(Color.WHITE);
        mGridPaint.setStrokeWidth(2); // 선의 굵기를 2로 설정
        mGridPaint.setStyle(Paint.Style.STROKE);
        // 점선 대신 실선으로 설정
        mGridPaint.setPathEffect(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        int rowCount = 3;
        int columnCount = 3;

        int rowHeight = height / rowCount;
        int columnWidth = width / columnCount;

        // 가로선 그리기
        for (int i = 1; i < rowCount; i++) {
            int y = i * rowHeight;
            canvas.drawLine(0, y, width, y, mGridPaint);
        }

        // 세로선 그리기
        for (int i = 1; i < columnCount; i++) {
            int x = i * columnWidth;
            canvas.drawLine(x, 0, x, height, mGridPaint);
        }
    }
}
