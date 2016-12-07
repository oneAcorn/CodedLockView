package com.acorn.codedlocklib.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by Acorn on 2016/10/31.
 */
public class NumberTextDrawable extends Drawable implements NumberDrawable.OnUpdateListener {
    private NumberDrawable[] numberDrawables;
    private static final int DEFAULT_DURATION = 1000;
    private int textSize;
    private int textColor;
    private int mDuration;

    public NumberTextDrawable(String str, int textSize, int textColor) {
        this(str, textSize, textColor, DEFAULT_DURATION);
    }

    public NumberTextDrawable(String str, int textSize, int textColor, int mDuration) {
        this.textSize = textSize;
        this.textColor = textColor;
        this.mDuration = mDuration;

        numberDrawables = new NumberDrawable[str.length()];
        for (int i = 0; i < numberDrawables.length; i++) {
            numberDrawables[i] = new NumberDrawable(
                    str.substring(i, i + 1), textSize, textColor, mDuration);
        }
    }

    public void startAddToAnim(String str) {
        NumberDrawable[] lastStr = numberDrawables.clone();
        numberDrawables = new NumberDrawable[str.length()];
        for (int i = 0; i < numberDrawables.length; i++) {
            int number, lastNumber;
            try {
                number = Integer.parseInt(str.substring(i, i + 1));
                lastNumber = Integer.parseInt(lastStr[i].getCodeStr());
            } catch (NumberFormatException e) {
                number = -1;
                lastNumber = -1;
            }
            if (number != -1 && lastNumber != -1) {
                numberDrawables[i] = new NumberDrawable(
                        str.substring(i, i + 1), textSize, textColor, mDuration,
                        number != lastNumber ?
                                lastNumber + 1 > 9 ? 0 : lastNumber + 1
                                : -1);
            } else {
                numberDrawables[i] = new NumberDrawable(
                        str.substring(i, i + 1), textSize, textColor, mDuration);
            }
        }
        numberDrawables[numberDrawables.length - 1].setOnUpdateListener(this);
        startAnim();
    }

    public void startFullScrollAnim() {
        NumberDrawable[] lastStr = numberDrawables.clone();
        for (int i = 0; i < numberDrawables.length; i++) {
            int lastNumber;
            try {
                lastNumber = Integer.parseInt(lastStr[i].getCodeStr());
            } catch (NumberFormatException e) {
                lastNumber = -1;
            }
            if (lastNumber != -1) {
                numberDrawables[i] = new NumberDrawable(
                        lastStr[i].getCodeStr(), textSize, textColor, mDuration,
                        lastNumber + 1 > 9 ? 0 : lastNumber + 1);
            } else {
                numberDrawables[i] = lastStr[i];
            }
        }
        numberDrawables[numberDrawables.length - 1].setOnUpdateListener(this);
        startAnim();
    }

    @Override
    public int getIntrinsicHeight() {
        return numberDrawables[0].getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return numberDrawables[0].getIntrinsicWidth() * numberDrawables.length;
    }

    @Override
    public void draw(Canvas canvas) {
        if (numberDrawables.length == 0)
            return;
        for (int i = 0; i < numberDrawables.length; i++) {
            canvas.save();
            canvas.translate(i * numberDrawables[i].getIntrinsicWidth(), 0);
            numberDrawables[i].draw(canvas);
            canvas.restore();
        }
//        if (numberDrawables[numberDrawables.length - 1].isAnimating())
//            invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void startAnim() {
        for (int i = 0; i < numberDrawables.length; i++) {
            numberDrawables[i].startAnim(i * 30);
        }
//        invalidateSelf();
    }

    @Override
    public void onUpdate() {
        invalidateSelf();
    }
}
