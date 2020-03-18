package com.olabode.wilson.daggernoteapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.olabode.wilson.daggernoteapp.R;

/**
 * Created by OLABODE WILSON on 2019-06-27.
 */

/**
 * custom class for drawing note lines on an edit text
 */
public class LinedEditText extends AppCompatEditText {

    private Rect mRect;
    private Paint mPaint;

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(getResources().getColor(R.color.note_text_line));


    }


    @Override
    protected void onDraw(Canvas canvas) {
        int height = ((View) this.getParent()).getHeight();
        int lineHeight = getLineHeight();
        int numberOfLines = height / lineHeight;

        Rect r = mRect;
        Paint paint = mPaint;

        int baseLine = getLineBounds(0, r);

        for (int i = 0; i < numberOfLines; i++) {
            canvas.drawLine(r.left, baseLine + 1, r.right, baseLine + 1, paint);

            baseLine += lineHeight;

        }

        super.onDraw(canvas);

    }

}
