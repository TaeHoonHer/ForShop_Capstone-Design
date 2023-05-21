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

        bH = Bitmap.createScaledBitmap(bH, 40, 40, false);
        bU = Bitmap.createScaledBitmap(bU, 40, 40, false);
        bL = Bitmap.createScaledBitmap(bL, 40, 40, false);

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
            paint.setTextSize(26);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setAlpha(128);
            paint.setStrokeWidth(2);
            String ratioText = String.format("%.1f", ratio*100)+"%";

            if (result.classIndex == 0) {
                canvas.drawBitmap(bH,resultLeft - 200 , result.rect.centerY() - 25, null);
                canvas.drawText(ratioText, resultLeft - 150, result.rect.centerY() + 5, paint);
            }
            else if (result.classIndex == 1) {
                canvas.drawBitmap(bU,resultLeft - 200 , result.rect.centerY() - 25, null);
                canvas.drawText(ratioText, resultLeft-150, result.rect.centerY(), paint);
            }

            else {
                canvas.drawBitmap(bL,resultLeft - 200 , result.rect.centerY() - 25, null);
                canvas.drawText(ratioText, resultLeft-150, result.rect.centerY(), paint);
            }

            float textWidth = paint.measureText(ratioText);
//            float textHeight = paint.measureText(ratioText);

            DashPathEffect dashPath = new DashPathEffect(new float[]{5,5}, 3);
            paintLine.setStyle( Paint.Style.STROKE );
            paintLine.setStrokeWidth(3);
            paintLine.setColor(Color.WHITE);
            paintLine.setPathEffect(dashPath);

            if (result.classIndex != 1) {
                canvas.drawLine(resultLeft-150, result.rect.top, resultLeft-150 + textWidth, result.rect.top , paintLine);
                canvas.drawLine(resultLeft-150, result.rect.bottom, resultLeft-150 + textWidth, result.rect.bottom , paintLine);
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
