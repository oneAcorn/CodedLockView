package com.acorn.codedlockview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.acorn.codedlocklib.CodedLockView;
import com.acorn.codedlocklib.drawable.CodedLockDrawable;
import com.acorn.codedlocklib.drawable.NumberTextDrawable;
import com.acorn.codedlocklib.utils.DensityUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView testV;
    private Button addBtn, fullScrollBtn;
    private EditText durationEt;

    private CodedLockView codedLockView;
    private Button addBtn2, fullScrollBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testV = (ImageView) findViewById(R.id.test1);
        addBtn = (Button) findViewById(R.id.add_btn);
        fullScrollBtn = (Button) findViewById(R.id.full_scroll_btn);
        durationEt = (EditText) findViewById(R.id.duration_et);
        codedLockView = (CodedLockView) findViewById(R.id.clv);
        addBtn2 = (Button) findViewById(R.id.add_btn2);
        fullScrollBtn2 = (Button) findViewById(R.id.full_scroll_btn2);

        setCodedDrawable("13989.54", testV);
        addBtn.setOnClickListener(this);
        fullScrollBtn.setOnClickListener(this);
        addBtn2.setOnClickListener(this);
        fullScrollBtn2.setOnClickListener(this);
    }


    private void setCodedDrawable(String number, ImageView targetView) {
        final NumberTextDrawable numberTextDrawable = new NumberTextDrawable(number,
                DensityUtils.sp2px(this, 60), 0xff3d2800, 10000);
        numberTextDrawable.setBounds(0, 0, numberTextDrawable.getIntrinsicWidth(),
                numberTextDrawable.getIntrinsicHeight());
        ViewGroup.LayoutParams lp = targetView.getLayoutParams();
        lp.width = numberTextDrawable.getIntrinsicWidth();
        lp.height = numberTextDrawable.getIntrinsicHeight();
        targetView.setLayoutParams(lp);
        targetView.setImageDrawable(numberTextDrawable);
//        numberTextDrawable.startAnim();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                numberTextDrawable.startAnim();
//            }
//        }, 1500);
    }

    @Override
    public void onClick(View view) {
        NumberTextDrawable drawable = (NumberTextDrawable) testV.getDrawable();
        int duration;
        try {
            duration = Integer.parseInt(durationEt.getText().toString());
        } catch (NumberFormatException e) {
            duration = 0;
        }
        if (duration != 0) {
            drawable.setDuration(duration);
            codedLockView.setDuration(duration);
        }

        if (view == addBtn) {
            double number = Double.parseDouble(drawable.getNumberStr());
            number += 178.32d;
            drawable.startAddToAnim(String.valueOf(number));
        } else if (view == fullScrollBtn) {
            drawable.startFullScrollAnim();
        } else if (view == addBtn2) {
//            codedLockView.startAddToAnim();
        } else if (view == fullScrollBtn2) {
            codedLockView.startFullScrollAnim();
        }
    }
}
