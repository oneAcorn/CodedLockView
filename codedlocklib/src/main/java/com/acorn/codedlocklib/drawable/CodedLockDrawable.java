package com.acorn.codedlocklib.drawable;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acorn on 2016/12/1.
 */

public class CodedLockDrawable extends Drawable {
    private static final int DEFAULT_DURATION = 1000;
    private int mDuration = DEFAULT_DURATION;

    private int toNum;
    private String animStr;
    private int textAlpha = 255;
    //取数字7的高度和宽度
    private int commonNumWidth, commonNumHeight;
    private Paint textPaint;
    private boolean isAnimating = false;
    private static final int LINE_SPACING = 10;
    private int offsetY;
    private ValueAnimator textValueAnimator;
    private char[] numChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private List<String[]> codeList=new ArrayList<>();

    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
