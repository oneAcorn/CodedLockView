package com.acorn.codedlocklib.drawable;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Acorn on 2016/10/31.
 */
public class NumberDrawable extends Drawable {
    private static final int DEFAULT_DURATION = 1000;

    private String codeStr;
    //最后动画10%用到的透明度
//    private int lastTextAlpha = 255;
    //取数字7的高度和宽度pri
    private int commonNumWidth, commonNumHeight;
    private Paint textPaint;
    private boolean isAnimating = false;
    private static final int LINE_SPACING = 10;
    private int offsetY;
    private ValueAnimator textValueAnimator;
    private String[] codeList;

    private int centerY;

    /**
     * 是否在密码列表中
     */
    private boolean isContainsInCodes = false;
    //在密码表中的第几位
    private int codesIndex;

    private OnUpdateListener onUpdateListener;

    NumberDrawable(String codeStr, int textSize, int textColor) {
        this(codeStr, textSize, textColor, DEFAULT_DURATION, -1);
    }

    public NumberDrawable(String codeStr, int textSize, int textColor, int duration) {
        this(codeStr, textSize, textColor, duration, -1);
    }

    public NumberDrawable(String codeStr, int textSize, int textColor, int duration, int beginAnimNumber) {
        this.codeStr = codeStr;

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        Rect textRect = new Rect();
        textPaint.getTextBounds("7", 0, 1, textRect);
        commonNumHeight = textRect.height();
        textPaint.getTextBounds("4", 0, 1, textRect);
        commonNumWidth = textRect.width();

        this.centerY = getIntrinsicHeight() / 2 + commonNumHeight / 2;

        setBeginAnimNumber(beginAnimNumber,duration);
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    boolean isContainInCodes(String str) {
        boolean isContain = false;
        for (String code : codeList) {
            if (code.equals(str)) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    public String getCodeStr() {
        return codeStr;
    }

    /**
     * 从密码表第几位开始向上移动
     *
     * @param beginAnimNumber 第一个出现在动画底部的数字
     */
    public void setBeginAnimNumber(int beginAnimNumber,int duration) {
        if (beginAnimNumber < 0)
            return;
        int codeListLength = 11;
        if (beginAnimNumber > codeListLength - 1) {
            return;
        }
        codeList = new String[codeListLength];
        codeList[0] = String.valueOf(beginAnimNumber - 1 < 0 ? 9 : beginAnimNumber - 1);
        for (int i = 1; i < codeList.length; i++) {
            codeList[i] = String.valueOf((beginAnimNumber - 1 + i) <= 9 ? beginAnimNumber - 1 + i :
                    beginAnimNumber - 1 + i - 10);
            if (codeList[i].equals(codeStr)) {
                this.isContainsInCodes = true;
                this.codesIndex = i;
            }
        }

        createAnimator(duration);
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        if (isAnimating && isContainsInCodes) {
            for (int i = 0; i <= 10; i++) {
                if (codesIndex >= i) {
                    int y = getYByNum(i);
                    textPaint.setAlpha(codesIndex == i ? 255 :
                            getTextAlpha(y, i));
                    canvas.drawText(codeList[i], 0, y, textPaint);
                }
            }
        } else {
            //drawText的y值显然指文字底部的y
            int centerY = getIntrinsicHeight() / 2 + commonNumHeight / 2;
            canvas.drawText(codeStr, 0, centerY, textPaint);
        }
    }

    private int getTextAlpha(int y, int index) {
        if (index == codesIndex - 1 && y < centerY) { //最后一个滚动数字平滑消失
            return (int) Math.max(((1f - Math.min(Math.abs((float) y - (float) centerY) / 255f, 1f))
                    * 255) - 100, 0);
        }
        return (int) ((1f - Math.min(Math.abs((float) y - (float) centerY) / 255f, 1f)) * 255);
    }

    private int getYByNum(int num) {
        return getTotalHeight(num) + offsetY;
    }

    private int getTotalHeight(int num) {
        return num * commonNumHeight + num * LINE_SPACING;
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

    @Override
    public int getIntrinsicHeight() {
        return commonNumHeight * 2;
    }

    @Override
    public int getIntrinsicWidth() {
        return commonNumWidth;
    }

    private void createAnimator(int duration) {
        PropertyValuesHolder offsetHolder = PropertyValuesHolder.ofInt("offset",
                centerY, -getTotalHeight(codesIndex)
                        + getIntrinsicHeight() / 2 + commonNumHeight / 2);
//        Keyframe alphaKeyframe0 = Keyframe.ofInt(0f, 255);
//        Keyframe alphaKeyframe1 = Keyframe.ofInt(0.9f, 255); //前90%都是255
//        Keyframe alphaKeyframe2 = Keyframe.ofInt(1f, 0); //最后10%从255到0
//        PropertyValuesHolder alphaHolder = PropertyValuesHolder.
//                ofKeyframe("textAlpha", alphaKeyframe0, alphaKeyframe1, alphaKeyframe2);
        textValueAnimator = ValueAnimator.ofPropertyValuesHolder(offsetHolder);
        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 变化因子(0~1)
//                float factor = animation.getAnimatedFraction();
                NumberDrawable.this.offsetY = ((Number) animation.getAnimatedValue("offset"))
                        .intValue();
//                NumberDrawable.this.lastTextAlpha = ((Number) animation.getAnimatedValue("textAlpha"))
//                        .intValue();

                if (null != onUpdateListener)
                    onUpdateListener.onUpdate();
                if(codeStr.equals("5")){
                    Log.i("sdf","update:"+offsetY);
                }
                invalidateSelf();
            }
        };
        textValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        textValueAnimator.addUpdateListener(updateListener);
        textValueAnimator.setDuration(duration);
        textValueAnimator.setInterpolator(new DecelerateInterpolator());
    }

    public void setDuration(int duration) {
        if (null == textValueAnimator)
            return;
        textValueAnimator.setDuration(duration);
    }

    public void startAnim(long delay) {
        if (null == textValueAnimator)
            return;
        if (!isContainsInCodes)
            return;
//        if (textValueAnimator.isRunning())
//            textValueAnimator.cancel();
        textValueAnimator.setStartDelay(delay);
        textValueAnimator.start();
    }

    void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    interface OnUpdateListener {
        void onUpdate();
    }
}
