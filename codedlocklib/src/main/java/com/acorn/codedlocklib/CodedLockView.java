package com.acorn.codedlocklib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Acorn on 2016/12/1.
 */

public class CodedLockView extends View {
    public CodedLockView(Context context) {
        this(context, null, 0);
    }

    public CodedLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodedLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
