// Copyright (c) 2020 Facebook, Inc. and its affiliates.
// All rights reserved.
//
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree.

package org.pytorch.demo.objectdetection;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.graphics.DashPathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.content.res.Resources;
import androidx.core.content.ContextCompat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class ResultView extends View {

    private Paint paintLine;
    private Paint paint;
    private ArrayList<Result> mResults;

    public ResultView(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bH = BitmapFactory.decodeResource(getResources(), R.drawable.head);
        Bitmap bU = BitmapFactory.decodeResource(getResources(), R.drawable.up);
        Bitmap bL = BitmapFactory.decodeResource(getResources(), R.drawable.low);

        bH = Bitmap.createScaledBitmap(bH, 70, 70, false);
        bU = Bitmap.createScaledBitmap(bU, 70, 70, false);
        bL = Bitmap.createScaledBitmap(bL, 70, 70, false);

        paint = new Paint();
        paintLine = new Paint();
        double BOX_HEIGHT = 0;
        int resultLeft = 0;
        if (mResults == null) {
            paint.setColor(Color.RED);
            paint.setTextSize(50);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(3);
            canvas.drawText("인물이 인식되지 않습니다.", 270, 1000, paint);
            return;
        }

        int lineStartY = 1800;
        int lineStopY = 1000;

        int lineMinY = (int) (lineStartY - (lineStopY - lineStartY) * 0.1);
        int lineMaxY = (int) (lineStopY + (lineStopY - lineStartY) * 0.1);

        for (Result r : mResults) {
            if(r.classIndex == 1) {
                resultLeft = r.rect.left;
            }
            double h = (double)r.rect.height();
            BOX_HEIGHT += h;
        }
        for (Result result : mResults) {

            double ratio = (double)result.rect.height() / BOX_HEIGHT;
            paint.setColor(Color.WHITE);
            paint.setTextSize(45);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setAlpha(128);
            paint.setStrokeWidth(2);
            String ratioText = String.format("%.1f", ratio*100)+"%";

            if (result.classIndex == 0) {
//                canvas.drawBitmap(bH, result.rect.left - 160 , result.rect.top - 30, null);
//                canvas.drawText(ratioText, result.rect.left - 105 , result.rect.top - 5, paint);
                canvas.drawBitmap(bH, result.rect.left - 260 , result.rect.top - 50, null);
                canvas.drawText(ratioText, result.rect.left - 170 , result.rect.top - 5, paint);
            }
            else if (result.classIndex == 1) {
                canvas.drawBitmap(bU, result.rect.left - 250 , result.rect.top - 40, null);
                canvas.drawText(ratioText, result.rect.left - 160 , result.rect.top + 5, paint);
            }

            else {
                canvas.drawBitmap(bL, result.rect.left - 260 , result.rect.top - 50, null);
                canvas.drawText(ratioText, result.rect.left - 170 , result.rect.top - 5, paint);
            }

            paintLine.setStrokeWidth(5);
            paintLine.setColor(Color.RED);

            if (result.classIndex == 2) {
                if (result.rect.bottom >= 900 && result.rect.bottom <= 900) {
                    paintLine.setColor(Color.GREEN);
                }
                else
                    paintLine.setColor(Color.RED);
                canvas.drawLine(400, 1000, 700 , 1000, paintLine);
            }

            float textWidth = paint.measureText(ratioText);
//            float textHeight = paint.measureText(ratioText);

//            DashPathEffect dashPath = new DashPathEffect(new float[]{5,5}, 3);
            paintLine.setStyle( Paint.Style.STROKE );
            paintLine.setStrokeWidth(4);
            paintLine.setColor(Color.WHITE);
//            paintLine.setPathEffect(dashPath);
            if (result.classIndex == 1) {
                canvas.drawLine(result.rect.left + 10, result.rect.top + 10, result.rect.left - 10 , result.rect.top - 10, paintLine);
                canvas.drawLine(result.rect.left - 10 , result.rect.top - 10, result.rect.left - 30 , result.rect.top - 10, paintLine);
            } else {
                canvas.drawLine(result.rect.left, result.rect.top, result.rect.left - 20, result.rect.top - 20, paintLine);
                canvas.drawLine(result.rect.left - 20, result.rect.top - 20, result.rect.left - 40, result.rect.top - 20, paintLine);
            }



//            // 바운딩 박스 그리기
//            paint.setColor(Color.RED);
//            paint.setStrokeWidth(5);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setAlpha(255);
//            canvas.drawRect(x1, y1, x2, y2, paint);

//            Path mPath = new Path();
//            RectF mRectF = new RectF(result.rect.left, result.rect.top, result.rect.left + TEXT_WIDTH,  result.rect.top + TEXT_HEIGHT);
//            mPath.addRect(mRectF, Path.Direction.CW);
//            mPaintText.setColor(Color.MAGENTA);
//            canvas.drawPath(mPath, mPaintText);
//
//            mPaintText.setColor(Color.WHITE);
//            mPaintText.setStrokeWidth(0);
//            mPaintText.setStyle(Paint.Style.FILL);
//            mPaintText.setTextSize(32);
//            canvas.drawText(String.format("%s %.2f", PrePostProcessor.mClasses[result.classIndex], result.score), result.rect.left + TEXT_X, result.rect.top + TEXT_Y, mPaintText);
        }
    }

    public ResultView(Context context, AttributeSet attrs){
        super(context, attrs);
//        mPaintRectangle = new Paint();
//        mPaintRectangle.setColor(Color.YELLOW);
    }

    public void setResults(ArrayList<Result> results) {
        mResults = results;
    }
}
