package com.acorn.codedlocklib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.acorn.codedlocklib.drawable.NumberDrawable;
import com.acorn.codedlocklib.drawable.NumberTextDrawable;
import com.acorn.codedlocklib.utils.DensityUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acorn on 2016/12/1.
 */

public class CodedLockView extends View {
    private float density; //dp

    private List<NumberDrawable> numberDrawables = new ArrayList<>();

    private double number;
    private int textSize;
    private int textColor;
    private int duration;

    public CodedLockView(Context context) {
        this(context, null, 0);
    }

    public CodedLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodedLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = context.getResources().getDisplayMetrics().density;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CodedLockView);
        try {
            this.number = Double.parseDouble(ta.getString(R.styleable.CodedLockView_numberText));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("attr numberText must be double type");
        }
        this.textSize = (int) ta.getDimension(R.styleable.CodedLockView_textSize, 14);
        this.textColor = ta.getColor(R.styleable.CodedLockView_textColor, 0xff000000);
        this.duration = ta.getInteger(R.styleable.CodedLockView_duration, 1000);
        ta.recycle();
        String numberStr = String.valueOf(number);
        for (int i = 0; i < numberStr.length(); i++) {
            NumberDrawable numberDrawable = new NumberDrawable(numberStr.substring(i, i + 1),
                    textSize, textColor, duration);
            numberDrawable.setCallback(this);
            numberDrawable.setBounds(0, 0, numberDrawable.getIntrinsicWidth(),
                    numberDrawable.getIntrinsicHeight());
            numberDrawables.add(numberDrawable);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int mWidth = 0, mHeight = 0;
        if (widthSpecMode == MeasureSpec.EXACTLY) { //match_parent
            mWidth = widthSpecSize;
        } else {
            mWidth = numberDrawables.size() > 0 ?
                    numberDrawables.get(0).getIntrinsicWidth() * numberDrawables.size() : 0;
            if (widthSpecMode == MeasureSpec.AT_MOST) { //wrap_content
                mWidth = Math.min(widthSpecSize, mWidth);
            }
        }
        if (heightSpecMode == MeasureSpec.EXACTLY) { //match_parent
            mHeight = heightSpecSize;
        } else {
            mHeight = numberDrawables.size() > 0 ? numberDrawables.get(0).getIntrinsicHeight() : 0;
            if (heightSpecMode == MeasureSpec.AT_MOST) { //wrap_content
                mHeight = Math.min(heightSpecSize, mHeight);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int drawableLength = numberDrawables.size();
        if (drawableLength == 0) {
            return;
        }
        for (int i = 0; i < drawableLength; i++) {
            canvas.save();
            canvas.translate(i * numberDrawables.get(i).getIntrinsicWidth(), 0);
            numberDrawables.get(i).draw(canvas);
            canvas.restore();
            if (i == 2) {
                Log.i("df", "draw2");
            }
        }
    }

    public void startAddToAnim(String str) {
    }

    public void startFullScrollAnim() {
        for (NumberDrawable drawable : numberDrawables) {
            int number;
            try {
                number = Integer.parseInt(drawable.getCodeStr());
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number != -1) {
                int beginNumber = number + 1;
                beginNumber = beginNumber >= 10 ? 0 : beginNumber;
                drawable.setBeginAnimNumber(beginNumber,duration);
            }
        }
        startAnim();
    }

    private void startAnim() {
        int length = numberDrawables.size();
        for (int i = 0; i < length; i++) {
            numberDrawables.get(i).startAnim(i * duration / 20);
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getNumber() {
        return number;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        boolean isVerify = false;
        int length = numberDrawables.size();
        for (int i = 0; i < length; i++) {
            if (who == numberDrawables.get(i)) {
                isVerify = true;
                break;
            }
        }
        return isVerify || super.verifyDrawable(who);
    }
}
